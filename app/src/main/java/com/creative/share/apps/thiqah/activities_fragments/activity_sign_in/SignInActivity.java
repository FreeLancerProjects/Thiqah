package com.creative.share.apps.thiqah.activities_fragments.activity_sign_in;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.ActivitySignInBinding;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.SignUpModel;
import com.creative.share.apps.thiqah.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private int fragment_count = 0;
    private FragmentManager manager;
    private Fragment_Language fragment_language;
    private Fragment_Sign_In fragment_sign_in;
    private Fragment_Sign_Up fragment_sign_up;
    private Fragment_Code_Verification fragment_code_verification;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        if (savedInstanceState == null) {
            if (preferences.isLangSelected(this)) {
                displayFragmentSignIn();

            } else {
                displayFragmentLanguage();
            }
        }
    }

    private void displayFragmentLanguage() {
        fragment_language = Fragment_Language.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();

    }

    public void displayFragmentCodeVerification(SignUpModel signUpModel) {
        fragment_count ++;
        fragment_code_verification = Fragment_Code_Verification.newInstance(signUpModel);

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_code_verification, "fragment_code_verification").addToBackStack("fragment_code_verification").commit();

    }

    public void displayFragmentSignIn() {
        fragment_count++;
        fragment_sign_in = Fragment_Sign_In.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();

    }

    public void displayFragmentSignUp() {
        fragment_count++;

        fragment_sign_up = Fragment_Sign_Up.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up, "fragment_sign_up").addToBackStack("fragment_sign_up").commit();

    }

    public void refreshActivity(String lang) {
        Paper.init(this);
        Paper.book().write("lang", lang);
        preferences.selectedLanguage(this, lang);
        preferences.saveSelectedLanguage(this);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        if (fragment_count > 1) {
            fragment_count--;
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
