package com.arab_developers_apps.theqah.activities_fragments.activity_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.ActivityImageBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class ImageActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityImageBinding binding;
    private String lang;
    private String endPoint;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_image);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            endPoint = intent.getStringExtra("endPoint");
            binding.setEndPoint(endPoint);

        }
    }


    @Override
    public void back() {
        finish();
    }

}
