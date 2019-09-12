package com.arab_developers_apps.theqah.activities_fragments.my_order;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.order_details.OrderDetailsActivity;
import com.arab_developers_apps.theqah.adapters.OrderAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityMyOrderBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.OrderDataModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;

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
    private List<OrderDataModel.OrderModel> orderDataModelList;
    private Preferences preferences;
    private UserModel userModel;
    private OrderAdapter adapter;
    private int current_page=1;
    private boolean isLoading = false;

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
        orderDataModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new OrderAdapter(orderDataModelList,this);
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total_items = adapter.getItemCount();
                int last_item_pos = manager.findLastCompletelyVisibleItemPosition();

                if (dy>0)
                {
                    if ((total_items-last_item_pos)==5&&!isLoading)
                    {
                        orderDataModelList.add(null);
                        adapter.notifyItemInserted(orderDataModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);

                    }
                }

            }
        });

        getOrder();

    }



    private void getOrder()
    {
        try {

            Api.getService(lang)
                    .getOrder("Bearer "+userModel.getToken(),1)
                    .enqueue(new Callback<OrderDataModel>() {
                        @Override
                        public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                               orderDataModelList.clear();
                               orderDataModelList.addAll(response.body().getData());
                               adapter.notifyDataSetChanged();
                               if (orderDataModelList.size()>0)
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
                        public void onFailure(Call<OrderDataModel> call, Throwable t) {
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

    private void loadMore(int page)
    {

        try {
            orderDataModelList.remove(orderDataModelList.size()-1);
            adapter.notifyItemRemoved(orderDataModelList.size()-1);

            Api.getService(lang)
                    .getOrder("Bearer "+userModel.getToken(),page)
                    .enqueue(new Callback<OrderDataModel>() {
                        @Override
                        public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    orderDataModelList.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    current_page = response.body().getCurrent_page();
                                    isLoading  =false;
                                }

                            }else
                            {
                                isLoading  =false;

                                if (response.code() == 500) {
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
                        public void onFailure(Call<OrderDataModel> call, Throwable t) {
                            try {
                                isLoading  =false;

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

        }
    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(OrderDataModel.OrderModel orderModel) {

        Intent intent= new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("data", orderModel);
        startActivityForResult(intent,1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1010&&resultCode==RESULT_OK)
        {
            getOrder();
        }
    }
}
