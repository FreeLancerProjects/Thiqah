package com.arab_developers_apps.theqah.activities_fragments.activity_code_verification.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_code_verification.CodeVerificationActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_forget_password.ForgetPasswordActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_sign_in.SignInActivity;
import com.arab_developers_apps.theqah.databinding.FragmentNewPasswordBinding;
import com.arab_developers_apps.theqah.databinding.FragmentSignInBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.models.LoginModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;
import com.arab_developers_apps.theqah.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_New_Password extends Fragment {
    private static final String TAG1 = "DATA1";
    private static final String TAG2 = "DATA2";

    private FragmentNewPasswordBinding binding;
    private CodeVerificationActivity activity;
    private String lang;
    private String phone_code;
    private String phone;


    public static Fragment_New_Password newInstance(String phone_code, String phone) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG1, phone_code);
        bundle.putSerializable(TAG2, phone);

        Fragment_New_Password fragment_new_password = new Fragment_New_Password();
        fragment_new_password.setArguments(bundle);
        return fragment_new_password;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_password, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        activity = (CodeVerificationActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        Bundle bundle = getArguments();
        if (bundle != null) {
            phone_code = bundle.getString(TAG1);
            phone = bundle.getString(TAG2);

        }


        binding.btnSend.setOnClickListener(view -> {
            checkData();
        });


    }

    private void checkData() {
        String password = binding.edtPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(password)) {
            Common.CloseKeyBoard(activity, binding.edtPassword);
            binding.edtPassword.setError(null);
            updatePassword(password);

        } else {
            binding.edtPassword.setError(getString(R.string.field_req));
        }
    }

    private void updatePassword(String password) {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(lang)
                    .updatePassword(phone_code, phone, password)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                activity.finish();

                            } else {
                                 if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }
    }


}
