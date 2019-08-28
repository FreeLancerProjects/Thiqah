package com.creative.share.apps.thiqah.activities_fragments.activity_notification;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.ActivityNotificationBinding;
import com.creative.share.apps.thiqah.interfaces.Listeners;
import com.creative.share.apps.thiqah.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class NotificationActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityNotificationBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        initView();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
    }


    @Override
    public void back() {
        finish();
    }

}

