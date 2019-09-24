package com.arab_developers_apps.theqah.activities_fragments.activity_order_buyer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.about_app.AboutAppActivity;
import com.arab_developers_apps.theqah.adapters.CityAdapter;
import com.arab_developers_apps.theqah.adapters.PaymentAdapter;
import com.arab_developers_apps.theqah.adapters.SpinnerAdapter;
import com.arab_developers_apps.theqah.adapters.SpinnerBankAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityBuyerBinding;
import com.arab_developers_apps.theqah.databinding.DialogAlertBinding;
import com.arab_developers_apps.theqah.databinding.DialogSelectImageBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.BuyerModel;
import com.arab_developers_apps.theqah.models.Cities_Payment_Bank_Model;
import com.arab_developers_apps.theqah.models.NotificationDataModel;
import com.arab_developers_apps.theqah.models.OrderDataModel;
import com.arab_developers_apps.theqah.models.OrderIdModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.BuyerListener {
    private ActivityBuyerBinding binding;
    private String lang;
    private List<String> period;
    private List<Integer> days;
    private SpinnerAdapter adapter;
    private BuyerModel buyerModel;
    private final int IMG1 = 1, IMG2 = 2;
    private Uri uri = null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private double chargeAmount = -1;
    private CityAdapter cityAdapter;
    private SpinnerBankAdapter bankAdapter;
    private List<Cities_Payment_Bank_Model.Bank> bankList;
    private List<Cities_Payment_Bank_Model.City> cityList;
    private List<Cities_Payment_Bank_Model.Payment> paymentList;
    private PaymentAdapter paymentAdapter;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private String city_id = "";
    private NotificationDataModel.NotificationModel notificationModel;
    private OrderDataModel.OrderModel orderModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buyer);
        getDataFromIntent();

        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("notification"))
        {
            notificationModel = (NotificationDataModel.NotificationModel) intent.getSerializableExtra("notification");
        }
    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        paymentList = new ArrayList<>();
        bankList = new ArrayList<>();
        bankList.add(new Cities_Payment_Bank_Model.Bank(getString(R.string.ch_bank)));
        cityList = new ArrayList<>();
        buyerModel = new BuyerModel();
        period = new ArrayList<>();
        days = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setBuyerListener(this);
        binding.setBuyerModel(buyerModel);

        if (notificationModel!=null)
        {
            getOrderNumber();
            getOrderDetails();


        }else
        {
            setUpAdapter();

            if (userModel != null) {
                binding.setUserModel(userModel);
                getOrderNumber();

            }

        }


        binding.edtCondition.setEnabled(false);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        paymentAdapter = new PaymentAdapter(paymentList, this);
        binding.recView.setAdapter(paymentAdapter);
        binding.recView.setNestedScrollingEnabled(true);
        binding.rb1.setOnClickListener(view -> {
            buyerModel.setCondition(true);
            binding.setBuyerModel(buyerModel);
            binding.edtCondition.setEnabled(true);

        });

        binding.rb2.setOnClickListener(view -> {
            buyerModel.setCondition(false);
            buyerModel.setCondition("");
            binding.setBuyerModel(buyerModel);
            binding.edtCondition.setText("");
            binding.edtCondition.setEnabled(false);
        });

        binding.checkbox1.setOnClickListener(view -> {
            if (binding.checkbox1.isChecked()) {
                buyerModel.setAcceptRule1(true);
            } else {
                buyerModel.setAcceptRule1(false);

            }
            binding.setBuyerModel(buyerModel);
        });
        binding.image.setOnClickListener(view -> CreateImageAlertDialog());


        binding.checkbox2.setOnClickListener(view -> {
            if (binding.checkbox2.isChecked()) {
                buyerModel.setAcceptRule2(true);
            } else {
                buyerModel.setAcceptRule2(false);

            }
            binding.setBuyerModel(buyerModel);
            Intent intent = new Intent(BuyerActivity.this, AboutAppActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        });


        binding.edtValue.addTextChangedListener(new TextWatcher() {
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
                        binding.tvAmount.setText("");
                        buyerModel.setPrice("");
                        binding.setBuyerModel(buyerModel);
                    }
                } catch (Exception e) {

                }


            }
        });


        bankAdapter = new SpinnerBankAdapter(bankList, this);
        binding.spinnerBank.setAdapter(bankAdapter);
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i ==0)
                {

                    if (notificationModel==null)
                    {
                        if (userModel!=null)
                        {
                            buyerModel.setCity_id(String.valueOf(userModel.getUser().getCity_id()));
                            binding.setBuyerModel(buyerModel);
                        }else
                        {
                            city_id = "";
                            buyerModel.setCity_id(city_id);
                            binding.setBuyerModel(buyerModel);
                        }

                    }else
                    {

                        if (orderModel!=null)
                        {
                            buyerModel.setCity_id(String.valueOf(orderModel.getSeller_city_id()));
                            binding.setBuyerModel(buyerModel);
                        }else
                        {
                            city_id = "";
                            buyerModel.setCity_id(city_id);
                            binding.setBuyerModel(buyerModel);
                        }

                    }

                }else
                {
                    try {
                        city_id = String.valueOf(userModel.getUser().getCity_id());
                        buyerModel.setCity_id(city_id);
                        binding.setBuyerModel(buyerModel);
                    }catch (Exception e){}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
                    buyerModel.setBank_id("");
                    binding.setBuyerModel(buyerModel);

                }else
                    {
                        buyerModel.setBank_id(String.valueOf(bankList.get(i).getId()));
                        binding.setBuyerModel(buyerModel);

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
                    if (notificationModel==null)
                    {
                        buyerModel.setPeriod("");
                        binding.setBuyerModel(buyerModel);
                    }

                }else
                    {

                        buyerModel.setPeriod(String.valueOf(days.get(i-1)));
                        binding.setBuyerModel(buyerModel);


                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getCities();




    }

    private void calcTotalItemsValue(double value) {
        double total;

        if (chargeAmount != -1) {
            if (value <= 1500) {
                total = 0;
                total += value + 45 + chargeAmount;

            } else {
                total = 0;
                total += (value * 0.03) + value + chargeAmount;

            }

            binding.tvAmount.setText(String.format("%s %s", total, getString(R.string.sar)));
            buyerModel.setPrice(String.valueOf(total));
            binding.setBuyerModel(buyerModel);
        }
    }

    private void getCities() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang)
                    .getCity_Payment_Bank()
                    .enqueue(new Callback<Cities_Payment_Bank_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Payment_Bank_Model> call, Response<Cities_Payment_Bank_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateCityAdapter(response.body());

                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(BuyerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(BuyerActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Payment_Bank_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(BuyerActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BuyerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void updateCityAdapter(Cities_Payment_Bank_Model body) {

        if (notificationModel!=null)
        {
            if (orderModel!=null)
            {
                cityList.clear();
                cityList.add(new Cities_Payment_Bank_Model.City(orderModel.getSeller_city_title(),orderModel.getSeller_city_title()));
                cityAdapter = new CityAdapter(cityList, this);
                binding.spinnerCity.setAdapter(cityAdapter);
            }

        }else
            {
                if (userModel != null) {
                    cityList.clear();
                    cityList.add(new Cities_Payment_Bank_Model.City(userModel.getUser().getCity_title_ar(), userModel.getUser().getCity_title_en()));
                    cityAdapter = new CityAdapter(cityList, this);
                    binding.spinnerCity.setAdapter(cityAdapter);

                    buyerModel.setCity_id(String.valueOf(userModel.getUser().getCity_id()));
                    binding.setBuyerModel(buyerModel);

                } else {
                    cityList.add(new Cities_Payment_Bank_Model.City("إختر", "Choose"));
                    cityList.addAll(body.getCities());
                    cityAdapter = new CityAdapter(cityList, this);
                    binding.spinnerCity.setAdapter(cityAdapter);
                }
            }


        bankList.addAll(body.getBanks());
        bankAdapter.notifyDataSetChanged();

        paymentList.addAll(body.getShippingTypes());
        paymentAdapter.notifyDataSetChanged();
    }

    private void getOrderDetails() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(lang)
                    .getOrderDetails("Bearer "+userModel.getToken(),notificationModel.getOrder_id())
                    .enqueue(new Callback<OrderDataModel.OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderDataModel.OrderModel> call, Response<OrderDataModel.OrderModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                orderModel = response.body();
                                updateUI(response.body());

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(BuyerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(BuyerActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(BuyerActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderDataModel.OrderModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(BuyerActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(BuyerActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }

    private void updateUI(OrderDataModel.OrderModel orderModel) {
        binding.tvName.setText(orderModel.getSeller_name());
        binding.tvPhone.setText(orderModel.getSeller_phone());
        binding.tvEmail.setText(orderModel.getSeller_email());


        buyerModel.setCity_id(String.valueOf(orderModel.getSeller_city_id()));
        binding.setBuyerModel(buyerModel);

        binding.edtTransferPurpose.setText(orderModel.getReason());
        binding.edtTransferPurpose.setEnabled(false);
        buyerModel.setTransfer_purpose(orderModel.getReason());

        binding.tvOrderNumber.setText(String.valueOf(orderModel.getId()));


        binding.edtValue.setText(String.valueOf(orderModel.getPrice()));
        binding.edtValue.setEnabled(false);
        buyerModel.setItem_value(String.valueOf(orderModel.getPrice()));

        //calcTotalItemsValue((double) orderModel.getPrice());

        if (orderModel.getConditions()!=null)
        {
            binding.rb1.setChecked(true);
            binding.rb2.setChecked(false);
            binding.rb1.setEnabled(false);
            binding.rb2.setEnabled(false);
            buyerModel.setCondition(true);
            buyerModel.setCondition(orderModel.getConditions());
            binding.edtCondition.setText(orderModel.getConditions());
            binding.edtCondition.setEnabled(false);
            binding.edtCondition.setVisibility(View.VISIBLE);


        }else
        {
            binding.rb1.setChecked(false);
            binding.rb2.setChecked(true);
            binding.rb1.setEnabled(false);
            binding.rb2.setEnabled(false);
            buyerModel.setCondition(false);
            buyerModel.setCondition("");
            binding.edtCondition.setVisibility(View.GONE);

        }


        period.clear();
        period.add(orderModel.getDays_left()+getString(R.string.day));
        adapter = new SpinnerAdapter(period,this);
        binding.spinnerPeriod.setAdapter(adapter);

        buyerModel.setPeriod(String.valueOf(orderModel.getDays_left()));

        binding.edtPhone2.setText(userModel.getUser().getMobile_number());
        binding.edtPhone2.setEnabled(false);
        buyerModel.setPhone2(userModel.getUser().getMobile_number());

        binding.setBuyerModel(buyerModel);


    }


    private void getOrderNumber() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(lang)
                    .getOrderNumber("Bearer "+userModel.getToken())
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                binding.tvOrderNumber.setText(String.valueOf(response.body()+100));


                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(BuyerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(BuyerActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(BuyerActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(BuyerActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(BuyerActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }

    private void setUpAdapter() {
        period.add(getString(R.string.choose));
        for (int i = 1; i < 31; i++) {
            days.add(i);
            period.add(i + " " + getString(R.string.day));
        }

        adapter = new SpinnerAdapter(period, this);
        binding.spinnerPeriod.setAdapter(adapter);
    }



    @Override
    public void send(BuyerModel buyerModel) {
        this.buyerModel = buyerModel;
        binding.setBuyerModel(buyerModel);

        if (userModel != null) {

            Intent intent = new Intent(this,AboutAppActivity.class);
            intent.putExtra("type",1);
            intent.putExtra("show",true);
            startActivityForResult(intent,100);

        } else {
            Common.CreateNoSignAlertDialog(this);
        }

    }
    private void sendOrder(BuyerModel buyerModel) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {

            Log.e("phone",buyerModel.getPhone2());
            String token = "Bearer "+userModel.getToken();
            RequestBody phone_part = Common.getRequestBodyText("966"+buyerModel.getPhone2());
            RequestBody reason_part = Common.getRequestBodyText(buyerModel.getTransfer_purpose());
            RequestBody price_part = Common.getRequestBodyText(buyerModel.getPrice());
            RequestBody day_part = Common.getRequestBodyText(buyerModel.getPeriod());
            RequestBody conditions_part = Common.getRequestBodyText(buyerModel.getCondition());
            RequestBody bank_id_part = Common.getRequestBodyText(buyerModel.getBank_id());
            RequestBody shipping_id_part = Common.getRequestBodyText(buyerModel.getShipping_method());

            MultipartBody.Part image = Common.getMultiPart(this,Uri.parse(buyerModel.getImage_uri()),"bank_transfer_pic");

            Api.getService(lang)
                    .buyer_order1(token,phone_part,price_part,reason_part,day_part,conditions_part,bank_id_part,shipping_id_part,image)
                    .enqueue(new Callback<OrderIdModel>() {
                        @Override
                        public void onResponse(Call<OrderIdModel> call, Response<OrderIdModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                CreateDialogAlert(response.body().getId());
                            }else
                            {
                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(BuyerActivity.this, "Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(BuyerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(BuyerActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(BuyerActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<OrderIdModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(BuyerActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(BuyerActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}

                        }
                    });
        }catch (Exception e)
        {
            dialog.dismiss();
            Log.e("Exception",e.getMessage()+"__");
        }

    }

    private void buyerUpdateOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {

            String token = "Bearer " + userModel.getToken();
            RequestBody bank_id_part = Common.getRequestBodyText(buyerModel.getBank_id());
            RequestBody shipping_part = Common.getRequestBodyText(buyerModel.getShipping_method());
            RequestBody order_id_part = Common.getRequestBodyText(String.valueOf(notificationModel.getOrder_id()));
            RequestBody not_id_part = Common.getRequestBodyText(String.valueOf(notificationModel.getId()));

            MultipartBody.Part image = Common.getMultiPart(this, Uri.parse(buyerModel.getImage_uri()), "bank_transfer_pic");

            Api.getService(lang)
                    .buyerUpdateOrder(token,bank_id_part,shipping_part,order_id_part,not_id_part,image)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                Toast.makeText(BuyerActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(BuyerActivity.this, "Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(BuyerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code()==401)
                                {
                                    Toast.makeText(BuyerActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(BuyerActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(BuyerActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BuyerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void CreateDialogAlert(int order_id) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(String.format("%s %s%s",getString(R.string.order_sent_suc),"#",order_id));
        binding.btnCancel.setOnClickListener((v) ->
        {
            dialog.dismiss();
            finish();
        }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            Check_ReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    private void Check_ReadPermission() {
        if (ContextCompat.checkSelfPermission(this, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_permission}, IMG1);
        } else {
            select_photo(1);
        }
    }


    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG2);
        } else {
            select_photo(2);

        }

    }

    private void select_photo(int type) {

        Intent intent = new Intent();

        if (type == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, IMG1);

        } else if (type == 2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IMG2);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            String path = Common.getImagePath(this, uri);
            buyerModel.setImage_uri(uri.toString());
            binding.setBuyerModel(buyerModel);

            binding.icon.setVisibility(View.GONE);
            if (path != null) {
                Picasso.with(this).load(new File(path)).fit().into(binding.image);
            } else {
                Picasso.with(this).load(uri).fit().into(binding.image);

            }
        } else if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.icon.setVisibility(View.GONE);
            uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                String path = Common.getImagePath(this, uri);
                buyerModel.setImage_uri(uri.toString());
                binding.setBuyerModel(buyerModel);
                if (path != null) {
                    Picasso.with(this).load(new File(path)).fit().into(binding.image);
                } else {
                    Picasso.with(this).load(uri).fit().into(binding.image);

                }
            }

        }else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null)
        {
            if (buyerModel.isDataValid(this)) {

                if (notificationModel==null)
                {
                    sendOrder(buyerModel);

                }else
                {
                    buyerUpdateOrder();
                }


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == IMG2) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(2);

                } else {
                    Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    public void setItemData(Cities_Payment_Bank_Model.Payment payment) {
        try {
            buyerModel.setShipping_method(String.valueOf(payment.getId()));
            binding.setBuyerModel(buyerModel);
            chargeAmount = Double.parseDouble(payment.getCost());

            try {
                double total;
                if (!binding.edtValue.getText().toString().trim().isEmpty()) {
                    double value = Double.parseDouble(String.format(Locale.ENGLISH, binding.edtValue.getText().toString().trim()));

                    if (chargeAmount != -1) {
                        if (value <1500) {
                            total = 0;
                            total += value + 45 + chargeAmount;

                        } else {
                            total = 0;
                            total += (value * 0.03) + value + chargeAmount;

                        }

                        binding.tvAmount.setText(String.format("%s %s", total, getString(R.string.sar)));
                        buyerModel.setPrice(String.valueOf(total));
                        binding.setBuyerModel(buyerModel);
                    }


                } else {
                    binding.tvAmount.setText("");
                    buyerModel.setPrice("");
                    binding.setBuyerModel(buyerModel);
                }
            } catch (Exception e) {

            }

        } catch (NumberFormatException e) {

        } catch (Exception e) {
        }
    }

    @Override
    public void back() {
        finish();
    }
}
