package com.creative.share.apps.thiqah.activities_fragments.bank_account;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.adapters.BankAdapter;
import com.creative.share.apps.thiqah.databinding.ActivityBankBinding;
import com.creative.share.apps.thiqah.interfaces.Listeners;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.Cities_Payment_Bank_Model;
import com.creative.share.apps.thiqah.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityBankBinding binding;
    private String lang;
    private List<Cities_Payment_Bank_Model.Bank> bankList;
    private BankAdapter adapter;
    private LinearLayoutManager manager;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bank);
        initView();
    }



    private void initView() {
        bankList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new BankAdapter(bankList,this);
        binding.recView.setAdapter(adapter);
        getBankData();
    }


    private void getBankData() {

        try {
            Api.getService(lang)
                    .getCity_Payment_Bank()
                    .enqueue(new Callback<Cities_Payment_Bank_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Payment_Bank_Model> call, Response<Cities_Payment_Bank_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                bankList.addAll(response.body().getBanks());
                                adapter.notifyDataSetChanged();
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(BankActivity.this, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(BankActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(BankActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Payment_Bank_Model> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(BankActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BankActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
