package com.creative.share.apps.thiqah.activities_fragments.activity_sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.thiqah.databinding.FragmentCodeVerificationBinding;
import com.creative.share.apps.thiqah.models.SignUpModel;
import com.creative.share.apps.thiqah.models.UserModel;
import com.creative.share.apps.thiqah.preferences.Preferences;
import com.creative.share.apps.thiqah.remote.Api;
import com.creative.share.apps.thiqah.share.Common;
import com.creative.share.apps.thiqah.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Code_Verification extends Fragment {
    private static final String TAG ="DATA";
    private SignInActivity activity;
    private FragmentCodeVerificationBinding binding;
    private boolean canResend = true;
    private SignUpModel signUpModel;
    private CountDownTimer countDownTimer;
    private String lang;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(SignUpModel signUpModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,signUpModel);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {

        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.btnConfirm.setOnClickListener(v -> checkData());

        binding.btnResend.setOnClickListener(v -> {

            if (canResend)
            {
                reSendSMSCode();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
           signUpModel = (SignUpModel) bundle.getSerializable(TAG);

        }

        startCounter();

    }

    private void checkData() {
        String code = binding.edtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code))
        {
            Common.CloseKeyBoard(activity,binding.edtCode);
            ValidateCode(code);
        }else
            {
                binding.edtCode.setError(getString(R.string.field_req));
            }
    }

    private void ValidateCode(String code)
    {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang)
                    .verifyCode(signUpModel.getPhone_code(),signUpModel.getPhone(),"1",signUpModel.getPassword(),code,signUpModel.getName(),signUpModel.getEmail(),signUpModel.getCity_id())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                preferences.create_update_userData(activity,response.body());
                                preferences.createSession(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            }else
                            {

                                if (response.code() == 422) {
                                    Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){}
    }

    private void startCounter()
    {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                canResend = false;

                int AllSeconds = (int) (millisUntilFinished / 1000);
                int seconds= AllSeconds%60;


                binding.btnResend.setText("00:"+seconds);
            }

            @Override
            public void onFinish() {
                canResend = true;
                binding.btnConfirm.setText(getString(R.string.resend));
            }
        }.start();
    }

    private void reSendSMSCode() {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .reSendCode(signUpModel.getPhone_code(),signUpModel.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        dialog.dismiss();

                        if (response.isSuccessful())
                        {
                            startCounter();

                        }else
                        {
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code()==422)
                            {
                                Common.CreateDialogAlert(activity,getString(R.string.inc_code_verification));
                            }else if (response.code()==500)
                            {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }else 
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());


                        }catch (Exception e){}
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
    }
}
