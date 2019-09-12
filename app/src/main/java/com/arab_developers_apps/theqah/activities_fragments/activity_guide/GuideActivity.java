package com.arab_developers_apps.theqah.activities_fragments.activity_guide;

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

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.adapters.GuideAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityGuideBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.GuideModel;
import com.arab_developers_apps.theqah.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuideActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityGuideBinding binding;
    private String lang;
    private List<GuideModel.Guide> guideList;
    private LinearLayoutManager manager;
    private GuideAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide);
        initView();
    }



    private void initView() {
        guideList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new GuideAdapter(guideList,this);
        binding.recView.setAdapter(adapter);
        getData();
    }


    private void getData() {

        try {
            Api.getService(lang)
                    .guide()
                    .enqueue(new Callback<GuideModel>() {
                        @Override
                        public void onResponse(Call<GuideModel> call, Response<GuideModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null&&response.body().getUserGuides()!=null) {
                                guideList.addAll(response.body().getUserGuides());
                                adapter.notifyDataSetChanged();
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(GuideActivity.this,"Validation Error", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(GuideActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(GuideActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GuideModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(GuideActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(GuideActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
