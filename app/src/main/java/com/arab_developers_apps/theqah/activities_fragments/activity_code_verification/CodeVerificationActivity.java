package com.arab_developers_apps.theqah.activities_fragments.activity_code_verification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_code_verification.fragments.Fragment_Code_Verification;
import com.arab_developers_apps.theqah.activities_fragments.activity_code_verification.fragments.Fragment_New_Password;
import com.arab_developers_apps.theqah.databinding.ActivityCodeVerificationBinding;


public class CodeVerificationActivity extends AppCompatActivity {
    private ActivityCodeVerificationBinding binding;
    private int fragment_count = 0;
    private FragmentManager manager;
    private Fragment_Code_Verification fragment_code_verification;
    private Fragment_New_Password fragment_new_password;
    private String phone;
    private String phone_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_code_verification);
        manager = getSupportFragmentManager();
        getDataFromIntent();
        initView();
        if (savedInstanceState == null) {
            displayFragmentCodeVerification();
        }
    }

    private void initView() {


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        phone_code = intent.getStringExtra("phone_code");
        phone = intent.getStringExtra("phone");

    }

    public void displayFragmentCodeVerification() {
        fragment_count ++;
        fragment_code_verification = Fragment_Code_Verification.newInstance(phone_code,phone);
        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_code_verification, "fragment_code_verification").addToBackStack("fragment_code_verification").commit();

    }

    public void displayFragmentNewPassword() {
        fragment_count ++;
        fragment_new_password = Fragment_New_Password.newInstance(phone_code,phone);
        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_new_password, "fragment_new_password").addToBackStack("fragment_new_password").commit();

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