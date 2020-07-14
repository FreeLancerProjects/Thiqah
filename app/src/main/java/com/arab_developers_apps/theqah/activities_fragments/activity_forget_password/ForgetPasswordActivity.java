package com.arab_developers_apps.theqah.activities_fragments.activity_forget_password;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_code_verification.CodeVerificationActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.databinding.ActivityForgetPasswordBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.UserModel;
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

public class ForgetPasswordActivity extends AppCompatActivity implements Listeners.BackListener,Listeners.ShowCountryDialogListener , OnCountryPickerListener {
    private ActivityForgetPasswordBinding binding;
    private String lang;
    private int type;
    private CountryPicker countryPicker;
    private String phone_code="00966";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        initView();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setShowCountryListener(this);
        createCountryDialog();
        binding.btnSend.setOnClickListener(view -> checkData());
    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if (countryPicker.getCountryFromSIM()!=null)
            {
                updatePhoneCode(countryPicker.getCountryFromSIM());
            }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
            {
                updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
            }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
            {
                updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
            }else
            {
                String code = "+966";
                binding.tvCode.setText(code);
                phone_code = "00966";

            }
        }catch (Exception e){
            phone_code = "00966";

        }


    }


    @Override
    public void showDialog() {
        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        phone_code = country.getDialCode().replace("+","00");

    }

    private void checkData() {
        String phone = binding.edtPhone.getText().toString().trim();
        if (!phone.isEmpty()){
            binding.edtPhone.setError(null);
            Common.CloseKeyBoard(this,binding.edtPhone);
            send(phone_code, Tags.convertArabicNumberToEnglish(phone));
        }else {
            binding.edtPhone.setError(getString(R.string.field_req));
        }

    }

    private void send(String phone_code,String phone) {

        try {
            ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang)
                    .sendPhoneNumberToSendCode(phone_code,phone)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {

                                Intent intent = new Intent(ForgetPasswordActivity.this, CodeVerificationActivity.class);
                                intent.putExtra("phone_code",phone_code);
                                intent.putExtra("phone",phone);
                                startActivity(intent);
                            }else
                            {

                                if (response.code() == 422) {
                                    Toast.makeText(ForgetPasswordActivity.this, R.string.phone_not_exist, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
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
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(ForgetPasswordActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(ForgetPasswordActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){}



    }



    @Override
    public void back() {
        finish();
    }

}
