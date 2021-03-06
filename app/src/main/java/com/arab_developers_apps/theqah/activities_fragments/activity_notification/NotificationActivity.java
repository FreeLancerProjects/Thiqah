package com.arab_developers_apps.theqah.activities_fragments.activity_notification;

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
import com.arab_developers_apps.theqah.activities_fragments.activity_appeal_details.AppealDetailsActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_buyer.BuyerActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_seller.OrderSellerActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_payment_details.PaymentDetailsActivity;
import com.arab_developers_apps.theqah.activities_fragments.order_details.OrderDetailsActivity;
import com.arab_developers_apps.theqah.adapters.MyNotificationAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityNotificationBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.FireBaseNotModel;
import com.arab_developers_apps.theqah.models.NotificationDataModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityNotificationBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private MyNotificationAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;
    private UserModel userModel;
    private Preferences preferences;
    private boolean isFromFireBase= false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        initView();
        getDataFromIntent();

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("notification"))
        {
            isFromFireBase = true;
        }
    }


    private void initView() {
        EventBus.getDefault().register(this);

        notificationModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new MyNotificationAdapter(notificationModelList,this);
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
                        notificationModelList.add(null);
                        adapter.notifyItemInserted(notificationModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);

                    }
                }

            }
        });

        getNotification();
    }

    private void getNotification()
    {
        try {

            Api.getService(lang)
                    .getNotifications("Bearer "+userModel.getToken(),1)
                    .enqueue(new Callback<NotificationDataModel>() {
                        @Override
                        public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                Log.e("fff","ttt");
                                notificationModelList.clear();
                                if (response.body().getData().size()>0)
                                {
                                    notificationModelList.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    binding.llNoNotifications.setVisibility(View.GONE);
                                }else
                                    {
                                        binding.llNoNotifications.setVisibility(View.VISIBLE);

                                    }

                            }else
                            {
                               if (response.code() == 500) {
                                    Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(NotificationActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(NotificationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(NotificationActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }
    private void loadMore(int page) {

        try {
            notificationModelList.remove(notificationModelList.size()-1);
            adapter.notifyItemRemoved(notificationModelList.size()-1);

            Api.getService(lang)
                    .getNotifications("Bearer "+userModel.getToken(),page)
                    .enqueue(new Callback<NotificationDataModel>() {
                        @Override
                        public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    notificationModelList.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    current_page = response.body().getCurrent_page();
                                    isLoading  =false;
                                }

                            }else
                            {
                                isLoading  =false;

                                if (response.code() == 500) {
                                    Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(NotificationActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                            try {
                                isLoading  =false;

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(NotificationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(NotificationActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }


    public void setItemData(NotificationDataModel.NotificationModel notificationModel1) {

        if (notificationModel1.getAction()==2)
        {

            Intent intent = new Intent(this, OrderSellerActivity.class);
            intent.putExtra("notification",notificationModel1);
            startActivityForResult(intent,123);

        }else if (notificationModel1.getAction()==3)
        {
            Intent intent = new Intent(this, BuyerActivity.class);
            intent.putExtra("notification",notificationModel1);
            startActivityForResult(intent,124);


        }
        else if (notificationModel1.getAction()==4)
        {

            Intent intent = new Intent(this, PaymentDetailsActivity.class);
            intent.putExtra("order_id",notificationModel1.getOrder_id());
            intent.putExtra("notification_id",notificationModel1.getId());
            startActivity(intent);
        }

        else if (notificationModel1.getAction()==6)
        {
            //confirmMoney(notificationModel1.getOrder_id(),notificationModel1.getId());
        }
        else if (notificationModel1.getAction()==8)
        {

            Intent intent = new Intent(this, AppealDetailsActivity.class);
            intent.putExtra("order_id",notificationModel1.getOrder_id());
            intent.putExtra("phone_number",String.valueOf(notificationModel1.getFrom_phone()));
            startActivityForResult(intent,125);
        }


    }

    public void setItemDataDetails(NotificationDataModel.NotificationModel notificationModel1) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order_id",notificationModel1.getOrder_id());
        startActivityForResult(intent,126);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==123||requestCode==124||requestCode==125||requestCode==126)&&resultCode==RESULT_OK)
        {
            getNotification();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newNotification(FireBaseNotModel fireBaseNotModel)
    {
        getNotification();
    }

    @Override
    public void back() {
        if (isFromFireBase)
        {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else
            {
                finish();
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

    }


}

