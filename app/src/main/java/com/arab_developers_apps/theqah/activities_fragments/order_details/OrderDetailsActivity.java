package com.arab_developers_apps.theqah.activities_fragments.order_details;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_appeal.AppealActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_appeal_details.AppealDetailsActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_buyer.BuyerActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_seller.OrderSellerActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_payment_details.PaymentDetailsActivity;
import com.arab_developers_apps.theqah.databinding.ActivityOrderDetailsBinding;
import com.arab_developers_apps.theqah.databinding.DialogRateBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.NotificationCountModel;
import com.arab_developers_apps.theqah.models.OrderDataModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;
import com.arab_developers_apps.theqah.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderDetailsBinding binding;
    private String lang;
    private OrderDataModel.OrderModel orderModel;
    private int rate = 0;
    private UserModel userModel;
    private Preferences preferences;
    private boolean refreshOrder = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        initView();
        getDataFromIntent();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.btnEnd.setOnClickListener(view -> CreateRateDialog());
        binding.btnAppeal.setOnClickListener(view -> {
            int type = 0;
            if (orderModel.getBuyer_phone().equals(userModel.getUser().getMobile_number())) {
                type = 1;
            } else if (orderModel.getSeller_phone().equals(userModel.getUser().getMobile_number())) {
                type = 2;

            }

            Intent intent = new Intent(this, AppealActivity.class);
            intent.putExtra("order_number", orderModel.getId());
            intent.putExtra("type", type);
            startActivityForResult(intent, 1);
        });

        binding.btnDetails.setOnClickListener(v -> {

            if (orderModel.getNotification().getAction()==2)
            {

                Intent intent = new Intent(this, OrderSellerActivity.class);
                intent.putExtra("notification",orderModel.getNotification());
                startActivityForResult(intent,123);

            }else if (orderModel.getNotification().getAction()==3)
            {
                Intent intent = new Intent(this, BuyerActivity.class);
                intent.putExtra("notification",orderModel.getNotification());
                startActivityForResult(intent,124);


            }
            else if (orderModel.getNotification().getAction()==4)
            {

                Intent intent = new Intent(this, PaymentDetailsActivity.class);
                intent.putExtra("order_id",orderModel.getNotification().getOrder_id());
                intent.putExtra("notification_id",orderModel.getNotification().getId());
                startActivity(intent);
            }

            else if (orderModel.getNotification().getAction()==6)
            {
                //confirmMoney(notificationModel1.getOrder_id(),notificationModel1.getId());
            }
            else if (orderModel.getNotification().getAction()==8)
            {

                Intent intent = new Intent(this, AppealDetailsActivity.class);
                intent.putExtra("order_id",orderModel.getNotification().getOrder_id());
                intent.putExtra("phone_number",String.valueOf(orderModel.getNotification().getFrom_phone()));
                startActivityForResult(intent,125);
            }

        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("order_id")){
                int order_id = intent.getIntExtra("order_id",0);
                getOrderById(order_id);
            }else {
                orderModel = (OrderDataModel.OrderModel) intent.getSerializableExtra("data");
                updateOrderUI();
            }

        }
    }

    private void updateOrderUI() {

        binding.setOrderModel(orderModel);

        if (orderModel.getStatus() < 2) {
            if (orderModel.getBank_transfer_pic() != null) {
                binding.llTransferImage.setVisibility(View.VISIBLE);
            } else {
                binding.llTransferImage.setVisibility(View.GONE);

            }

            if (orderModel.getItem_pic() != null) {
                binding.llItemImage.setVisibility(View.VISIBLE);
            } else {
                binding.llItemImage.setVisibility(View.GONE);

            }


        } else {
            if (orderModel.getBank_transfer_pic() != null) {
                binding.llTransferImage.setVisibility(View.VISIBLE);
            } else {
                binding.llTransferImage.setVisibility(View.GONE);

            }

            if (orderModel.getItem_pic() != null) {

                binding.llItemImage.setVisibility(View.VISIBLE);
            } else {
                binding.llItemImage.setVisibility(View.GONE);

            }
        }


        if (orderModel.getStatus() == 0) {
            binding.tvTransState.setVisibility(View.GONE);
            binding.ll.setVisibility(View.VISIBLE);

            step1();

        } else if (orderModel.getStatus() == 1 || orderModel.getStatus() == 2) {
            binding.ll.setVisibility(View.VISIBLE);
            binding.tvTransState.setVisibility(View.GONE);

            step2();
        } else if (orderModel.getStatus() == 3) {
            binding.ll.setVisibility(View.GONE);
            binding.tvTransState.setVisibility(View.VISIBLE);
            binding.btnEnd.setVisibility(View.GONE);


        } else if (orderModel.getStatus() == 4) {
            binding.ll.setVisibility(View.VISIBLE);
            binding.tvTransState.setVisibility(View.GONE);

            step3();

        } else if (orderModel.getStatus() == 5) {
            binding.ll.setVisibility(View.VISIBLE);
            binding.tvTransState.setVisibility(View.GONE);
            step4();
        } else if (orderModel.getStatus() == 6) {
            step5();

        }


        if (orderModel.getStatus() >= 4) {

            if (userModel.getUser().getMobile_number().equals(orderModel.getBuyer_phone())) {
                if (orderModel.getBuyer_complained() == 1)
                {
                    binding.btnAppeal.setVisibility(View.GONE);
                }else
                {
                    binding.btnAppeal.setVisibility(View.VISIBLE);

                }
            } else if (userModel.getUser().getMobile_number().equals(orderModel.getSeller_phone())) {

                if (orderModel.getSeller_complained() == 1)
                {
                    binding.btnAppeal.setVisibility(View.GONE);
                }else
                {
                    binding.btnAppeal.setVisibility(View.VISIBLE);

                }
            }
        }


        if (orderModel.getNotification()!=null){
            Log.e("data",orderModel.getNotification().getMessage_body()+"__");
            if (orderModel.getNotification().getAction() == 2 || orderModel.getNotification().getAction() == 3) {
                binding.btnDetails.setVisibility(View.VISIBLE);
                binding.btnReceiveMoney.setVisibility(View.GONE);

            } else if (orderModel.getNotification().getAction() == 4) {
                binding.btnReceiveMoney.setVisibility(View.GONE);
                binding.btnDetails.setVisibility(View.VISIBLE);

            }

            else if (orderModel.getNotification().getAction() == 6) {
                binding.btnReceiveMoney.setVisibility(View.GONE);
                binding.btnDetails.setVisibility(View.GONE);

            } else if (orderModel.getNotification().getAction() == 8) {
                binding.btnReceiveMoney.setVisibility(View.GONE);
                binding.btnDetails.setVisibility(View.VISIBLE);


            } else {
                binding.btnReceiveMoney.setVisibility(View.GONE);
                binding.btnDetails.setVisibility(View.GONE);

            }

        }

    }

    private void getOrderById(int order_id) {
        binding.consContainer.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        Log.e("lang",lang);
        Api.getService(Tags.base_url)
                .getOrderById("Bearer "+userModel.getToken(),lang,order_id)
                .enqueue(new Callback<OrderDataModel.OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel.OrderModel> call, Response<OrderDataModel.OrderModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {

                                orderModel = response.body();
                                Log.e("title",response.body().getNotification().getOrder_id()+"__"+response.body().getNotification().getId()+"__action"+response.body().getNotification().getAction());
                                Log.e("body",response.body().getNotification().getMessage_body()+"__");

                                binding.setOrderModel(orderModel);
                                updateOrderUI();
                                binding.consContainer.setVisibility(View.VISIBLE);
                                binding.progBar.setVisibility(View.GONE);
                            }

                        }else
                        {
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel.OrderModel> call, Throwable t) {
                        try
                        {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }


    private void step1() {
        binding.btnEnd.setVisibility(View.GONE);
        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image2.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));


        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image3.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));

        binding.tv4.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);

    }

    private void step2() {

        binding.btnEnd.setVisibility(View.GONE);

        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image3.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));

        binding.tv4.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);


    }

    private void step3() {

        if (orderModel.getBuyer_phone().equals(userModel.getUser().getMobile_number())) {
            if (orderModel.getBuyer_done() == 0) {
                binding.btnEnd.setVisibility(View.VISIBLE);

            } else {
                binding.btnEnd.setVisibility(View.GONE);

            }
        } else if (orderModel.getSeller_phone().equals(userModel.getUser().getMobile_number())) {

            if (orderModel.getSeller_done() == 0) {
                binding.btnEnd.setVisibility(View.VISIBLE);

            } else {
                binding.btnEnd.setVisibility(View.GONE);

            }
        }

        if (orderModel.getBuyer_phone().equals(userModel.getUser().getMobile_number())) {
            if (orderModel.getBuyer_done() == 0) {
                binding.btnEnd.setVisibility(View.VISIBLE);

            } else {
                binding.btnEnd.setVisibility(View.GONE);

            }
        } else if (orderModel.getSeller_phone().equals(userModel.getUser().getMobile_number())) {

            if (orderModel.getSeller_done() == 0) {
                binding.btnEnd.setVisibility(View.VISIBLE);

            } else {
                binding.btnEnd.setVisibility(View.GONE);

            }
        }

        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this, R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);


    }

    private void step4() {


        if (orderModel.getBuyer_phone().equals(userModel.getUser().getMobile_number())) {
            if (orderModel.getBuyer_done() == 1) {
                binding.btnEnd.setVisibility(View.GONE);

            }
        }

        if (orderModel.getSeller_phone().equals(userModel.getUser().getMobile_number())) {

            if (orderModel.getSeller_done() == 1) {
                binding.btnEnd.setVisibility(View.GONE);

            }
        }

        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image4.setBackgroundResource(R.drawable.checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv5.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);

    }

    private void step5() {



        binding.btnEnd.setVisibility(View.GONE);

        binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image4.setBackgroundResource(R.drawable.checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.tv5.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.image5.setBackgroundResource(R.drawable.checked_circle);

    }


    private void CreateRateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogRateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_rate, null, false);

        binding.rateBar.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> {
            binding.tvRate.setText(String.valueOf(rating));
            rate = (int) rating;

        });
        binding.btnRate.setOnClickListener((v) ->
                {
                    String comment = binding.edtComment.getText().toString().trim();
                    if (!TextUtils.isEmpty(comment)) {
                        EndOrder(rate, comment);
                        dialog.dismiss();

                    } else {
                        binding.edtComment.setError(getString(R.string.field_req));
                    }
                }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void EndOrder(int rate, String comment) {


        if (orderModel.getBuyer_phone().equals(userModel.getUser().getMobile_number())) {
            buyerFinishOrder(rate, comment);

        } else if (orderModel.getSeller_phone().equals(userModel.getUser().getMobile_number())) {
            sellerFinishOrder(rate, comment);


        }


    }


    private void sellerFinishOrder(int rate, String comment) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(lang)
                    .sellerFinishOrder("Bearer " + userModel.getToken(), orderModel.getId(), comment, rate)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                setResult(RESULT_OK);
                                finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(OrderDetailsActivity.this, "Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(OrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(OrderDetailsActivity.this, "User Unauthenticated", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void buyerFinishOrder(int rate, String comment) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(lang)
                    .buyerFinishOrder("Bearer " + userModel.getToken(), orderModel.getId(), comment, rate)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                setResult(RESULT_OK);
                                finish();
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(OrderDetailsActivity.this, "Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(OrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(OrderDetailsActivity.this, "User Unauthenticated", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            binding.btnAppeal.setVisibility(View.GONE);
            refreshOrder = true;
        }if ((requestCode==123||requestCode==124||requestCode==125)&&resultCode==RESULT_OK)
        {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void back() {
        if (refreshOrder) {
            Intent intent = getIntent();
            if (intent != null) {
                setResult(RESULT_OK);
            }
        } else {
            finish();

        }
    }

}
