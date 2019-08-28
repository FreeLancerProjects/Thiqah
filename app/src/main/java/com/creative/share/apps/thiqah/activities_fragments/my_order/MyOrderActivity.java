package com.creative.share.apps.thiqah.activities_fragments.my_order;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.order_details.OrderDetailsActivity;
import com.creative.share.apps.thiqah.adapters.OrderAdapter;
import com.creative.share.apps.thiqah.databinding.ActivityMyOrderBinding;
import com.creative.share.apps.thiqah.interfaces.Listeners;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.OrderModel;
import com.creative.share.apps.thiqah.models.UserModel;
import com.creative.share.apps.thiqah.preferences.Preferences;
import com.creative.share.apps.thiqah.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMyOrderBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<OrderModel> orderModelList;
    private Preferences preferences;
    private UserModel userModel;
    private OrderAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_order);
        initView();
    }



    private void initView() {
        orderModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new OrderAdapter(orderModelList,this);
        binding.recView.setAdapter(adapter);

        getOrder();

    }

    private void getOrder() {
        try {

            Api.getService(lang)
                    .getOrder("Bearer "+userModel.getToken())
                    .enqueue(new Callback<List<OrderModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                               orderModelList.clear();
                               orderModelList.addAll(response.body());
                               adapter.notifyDataSetChanged();
                               if (orderModelList.size()>0)
                               {
                                   binding.llNoOrder.setVisibility(View.GONE);
                               }else
                                   {
                                       binding.llNoOrder.setVisibility(View.VISIBLE);

                                   }

                            }else
                            { if (response.code() == 500) {
                                    Toast.makeText(MyOrderActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(MyOrderActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(MyOrderActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(MyOrderActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(MyOrderActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void back() {
        finish();
    }

    public void setItemData(OrderModel orderModel) {

        Intent intent= new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("data",orderModel);
        startActivity(intent);
    }
}
