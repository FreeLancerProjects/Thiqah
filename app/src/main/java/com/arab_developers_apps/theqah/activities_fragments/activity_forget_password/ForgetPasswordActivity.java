package com.arab_developers_apps.theqah.activities_fragments.activity_forget_password;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.ActivityForgetPasswordBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class ForgetPasswordActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityForgetPasswordBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        initView();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        binding.btnSend.setOnClickListener(view -> checkData());

    }

    private void checkData() {

        String email = binding.edtEmail.getText().toString().trim();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError(null);
            Common.CloseKeyBoard(this, binding.edtEmail);
            send(email);
        } else if (email.isEmpty()) {
            binding.edtEmail.setError(getString(R.string.field_req));
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.edtEmail.setError(getString(R.string.inv_email));

        }
    }

    private void send(String email) {
    }


    @Override
    public void back() {
        finish();
    }

}
