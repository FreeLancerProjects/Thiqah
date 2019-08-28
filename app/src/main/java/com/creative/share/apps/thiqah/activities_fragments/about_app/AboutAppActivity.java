package com.creative.share.apps.thiqah.activities_fragments.about_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.ActivityAboutAppBinding;
import com.creative.share.apps.thiqah.interfaces.Listeners;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.AboutAppModel;
import com.creative.share.apps.thiqah.remote.Api;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAppActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAboutAppBinding binding;
    private String lang;
    private int type;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_app);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
           type = intent.getIntExtra("type",1);
            if (type == 1)
            {
                binding.tvTitle.setText(getString(R.string.terms_and_conditions));
            }else if (type == 2)
            {
                binding.tvTitle.setText(getString(R.string.about_app));

            }
            else if (type == 3)
            {
                binding.tvTitle.setText(getString(R.string.guarantees));

            }


            getAppData();
        }
    }

    private void getAppData() {

        try {

            Api.getService(lang)
                    .appData()
                    .enqueue(new Callback<AboutAppModel>() {
                        @Override
                        public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                if (type ==1)
                                {
                                    binding.tvContent.setText(response.body().getTerms());
                                }else if (type==2)
                                {
                                    binding.tvContent.setText(response.body().getAbout_us());

                                }
                                else if (type==3)
                                {
                                    binding.tvContent.setText(response.body().getGuarantees());

                                }
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(AboutAppActivity.this,"Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(AboutAppActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(AboutAppActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AboutAppActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AboutAppActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void back() {
        finish();
    }

}
