package com.arab_developers_apps.theqah.activities_fragments.bank_account;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.adapters.BankAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityBankBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.AboutAppModel;
import com.arab_developers_apps.theqah.models.Cities_Payment_Bank_Model;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;

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
    private AboutAppModel aboutAppModel;
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
        binding.setValue(0);
        binding.setMaxNum(0);
        binding.setPercent(3);

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
        getCommission();
        getBankData();

        binding.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().trim().length() > 0) {
                        double value = Double.parseDouble(String.format(Locale.ENGLISH, editable.toString().trim()));

                        calcTotalItemsValue(value);


                    } else {
                        binding.tvResult.setText("");

                    }
                } catch (Exception e) {

                }


            }
        });

    }

    private void calcTotalItemsValue(double value) {
        double total;

        if (aboutAppModel!=null)
        {
            if (value <= aboutAppModel.getThiqah_average_amount()) {
                total = 0;
                total += value + aboutAppModel.getThiqah_average_value() ;

            } else {
                total = 0;
                total += (value * aboutAppModel.getThiqah_rate()/100) + value ;

            }

            binding.tvResult.setText(String.format("%s %s", (total-value), getString(R.string.sar)));
        }

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

    private void getCommission() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {


            Api.getService(lang)
                    .appData()
                    .enqueue(new Callback<AboutAppModel>() {
                        @Override
                        public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                aboutAppModel = response.body();
                                binding.setPercent(aboutAppModel.getThiqah_rate());
                                binding.setMaxNum(aboutAppModel.getThiqah_average_amount());
                                binding.setValue(aboutAppModel.getThiqah_average_value());
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
                        public void onFailure(Call<AboutAppModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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
            dialog.dismiss();
        }
    }

    @Override
    public void back() {
        finish();
    }

}
