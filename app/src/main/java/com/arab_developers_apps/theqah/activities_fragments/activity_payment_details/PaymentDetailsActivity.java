package com.arab_developers_apps.theqah.activities_fragments.activity_payment_details;

import android.app.ProgressDialog;
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

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.ActivityPaymentDetailsBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.PaymentDataModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPaymentDetailsBinding binding;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private int order_id;
    private int notification_id;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            order_id = intent.getIntExtra("order_id",0);
            notification_id = intent.getIntExtra("notification_id",0);

        }
    }


    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.btnConfirm.setOnClickListener(view -> confirmMoney(order_id,notification_id));
        getData();

    }

    private void getData() {
        try {

            Api.getService(lang)
                    .getPaymentDetails("Bearer "+userModel.getToken(),order_id)
                    .enqueue(new Callback<PaymentDataModel>() {
                        @Override
                        public void onResponse(Call<PaymentDataModel> call, Response<PaymentDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getPayment()!=null)
                            {
                                binding.ll.setVisibility(View.VISIBLE);
                                updateUI(response.body().getPayment());
                            }else {

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(PaymentDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(PaymentDetailsActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {

                                    Toast.makeText(PaymentDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    Log.e("error",response.code()+"_");


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PaymentDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(PaymentDetailsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(PaymentDetailsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            Log.e("e",e.getMessage()+"__");
            binding.progBar.setVisibility(View.GONE);
        }
    }

    private void updateUI(PaymentDataModel.Payment payment) {
        binding.setPayment(payment);
        if (payment.getOrder_status()==5)
        {
            binding.btnConfirm.setVisibility(View.VISIBLE);
        }else
            {
                binding.btnConfirm.setVisibility(View.GONE);

            }

    }

    private void confirmMoney(int order_id, int notification_id)
    {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {

            String token = "Bearer " + userModel.getToken();

            Api.getService(lang)
                    .confirmMoney(token,order_id,notification_id)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                //getNotification();

                                setResult(RESULT_OK);
                                finish();
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(PaymentDetailsActivity.this, "Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(PaymentDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code()==401)
                                {
                                    Toast.makeText(PaymentDetailsActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(PaymentDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(PaymentDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaymentDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }

                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception", e.getMessage() + "__");
        }
    }

    @Override
    public void back() {
        finish();
    }


}
