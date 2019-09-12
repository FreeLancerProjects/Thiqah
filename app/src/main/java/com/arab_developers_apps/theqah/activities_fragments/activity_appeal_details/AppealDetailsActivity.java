package com.arab_developers_apps.theqah.activities_fragments.activity_appeal_details;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_image.ImageActivity;
import com.arab_developers_apps.theqah.adapters.ComplainImagesAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityAppealDetailsBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.ComplainDataModel;
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

public class AppealDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAppealDetailsBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private ComplainImagesAdapter adapter;
    private List<String> imageList;
    private UserModel userModel;
    private Preferences preferences;
    private int order_id;
    private String phone_number;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_appeal_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            order_id = intent.getIntExtra("order_id",0);
            phone_number = intent.getStringExtra("phone_number");
        }
    }


    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        imageList = new ArrayList<>();
        manager = new GridLayoutManager(this,2);
        adapter = new ComplainImagesAdapter(imageList,this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        getData();

    }

    private void getData() {
        try {

            Api.getService(lang)
                    .getComplain("Bearer "+userModel.getToken(),phone_number,order_id)
                    .enqueue(new Callback<ComplainDataModel>() {
                        @Override
                        public void onResponse(Call<ComplainDataModel> call, Response<ComplainDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getComplaint()!=null)
                            {
                                binding.ll.setVisibility(View.VISIBLE);
                                updateUI(response.body().getComplaint());
                            }else {

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                Toast.makeText(AppealDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            }else if (response.code()==401)
                            {
                                Toast.makeText(AppealDetailsActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                            }else
                            {

                                Toast.makeText(AppealDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("error",response.code()+"_");


                            }
                            }
                        }

                        @Override
                        public void onFailure(Call<ComplainDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(AppealDetailsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(AppealDetailsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateUI(ComplainDataModel.Complain complaint) {
        binding.setComplain(complaint);
        imageList.addAll(complaint.getAttachments());
        Log.e("vvv","vvv");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void back() {
        finish();
    }

    public void setImageItem(String endPoint) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("endPoint",endPoint);
        startActivity(intent);
    }
}
