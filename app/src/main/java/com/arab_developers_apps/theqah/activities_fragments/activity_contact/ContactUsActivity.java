package com.arab_developers_apps.theqah.activities_fragments.activity_contact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.ActivityContactUsBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.AboutAppModel;
import com.arab_developers_apps.theqah.models.ContactModel;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity  implements Listeners.BackListener ,Listeners.ContactListener,Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private ActivityContactUsBinding binding;
    private String lang;
    private ContactModel contactModel;
    private CountryPicker countryPicker;
    private String phone="";
    private String email="";
    private static final int REQUEST_PHONE_CALL = 1;

    Intent intent;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        initView();

    }



    private void initView() {
        contactModel = new ContactModel();
        Paper.init(this);
        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","+"+ binding.call.getText().toString(), null));

        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setContactListener(this);
        binding.setShowCountryListener(this);
        binding.setContactModel(contactModel);
        createCountryDialog();
        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(intent!=null) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(ContactUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ContactUsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivity(intent);
                    }
                } }
        });
        binding.imageWhats.setOnClickListener(view -> {
            if (!phone.isEmpty())
            {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("whatsapp://send?phone="+phone));

                if (sendIntent.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(ContactUsActivity.this, "Error\n" + "", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(sendIntent);
            }else
            {
                Toast.makeText(this, R.string.inv_now, Toast.LENGTH_SHORT).show();
            }


        });

        binding.imageGmail.setOnClickListener(view -> {

            if (!email.isEmpty())
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                PackageManager pm = getPackageManager();
                List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                        best = info;

                    }
                }

                if (best != null) {
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                }

                startActivity(intent);
            }else
                {
                    Toast.makeText(this, R.string.inv_now, Toast.LENGTH_SHORT).show();
                }

        });

        getAppData();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                }
                else {

                }
                return;
            }
        }
    }

    private void getAppData() {
        try {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(lang)
                .appData()
                .enqueue(new Callback<AboutAppModel>() {
                    @Override
                    public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            email = response.body().getContact_email();
                            phone = response.body().getPhone_number();

                        } else {

                            if (response.code() == 422) {
                                Toast.makeText(ContactUsActivity.this, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 500) {
                                Toast.makeText(ContactUsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            }else
                            {
                                Toast.makeText(ContactUsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AboutAppModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ContactUsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ContactUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    } catch (Exception e) {
    }
    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

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
            contactModel.setPhone_code(code.replace("+","00"));

        }

    }


    @Override
    public void sendContact(String name, String email, String phone_code,String phone, String message) {
        if (phone.startsWith("0"))
        {
            phone = phone.replaceFirst("0","");
        }
        contactModel = new ContactModel(name,email,message,phone_code,phone);
        binding.setContactModel(contactModel);
        if (contactModel.isDataValid(this))
        {
            send(name,email,phone_code,phone,message);
        }
    }

    private void send(String name, String email, String phone_code,String phone, String message) {

        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang)
                    .contact(name,phone_code+""+phone,email,message)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(ContactUsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(ContactUsActivity.this,"Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(ContactUsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(ContactUsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ContactUsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ContactUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        contactModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    @Override
    public void showDialog() {
        countryPicker.showDialog(this);
    }

    @Override
    public void back() {
        finish();
    }
}
