package com.arab_developers_apps.theqah.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_info.InfoActivity;
import com.arab_developers_apps.theqah.adapters.CategoryAdapter;
import com.arab_developers_apps.theqah.databinding.FragmentMainBinding;
import com.arab_developers_apps.theqah.models.AboutAppModel;
import com.arab_developers_apps.theqah.models.CategoryModel;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private FragmentMainBinding binding;
    private String videoPath;
    private CategoryAdapter adapter;
    private List<CategoryModel> categoryModelList;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private HomeActivity activity;
    private boolean isFirstTime = true;

    public static Fragment_Main newInstance() {

        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        categoryModelList = new ArrayList<>();


        if (lang.equals("ar")) {
            binding.imageRight.setRotation(180.0f);
            binding.imageLeft.setRotation(180.0f);
        }

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        binding.discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.75f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build());





        binding.btn1.setOnClickListener(view -> navigateToInfoActivity(R.drawable.thiqah));
        binding.btn2.setOnClickListener(view -> navigateToInfoActivity(R.drawable.waseet));
        binding.btn3.setOnClickListener(view -> navigateToInfoActivity(R.drawable.maly));
        binding.discreteScrollView.addOnItemChangedListener((viewHolder, pos) -> {
            if (pos == 0) {
                binding.imageRight.setVisibility(View.INVISIBLE);

                binding.imageLeft.setVisibility(View.VISIBLE);

            } else if (pos == categoryModelList.size() - 1) {
                binding.imageLeft.setVisibility(View.INVISIBLE);
                binding.imageRight.setVisibility(View.VISIBLE);


            } else {
                binding.imageLeft.setVisibility(View.VISIBLE);
                binding.imageRight.setVisibility(View.VISIBLE);
            }
        });
        binding.imageLeft.setOnClickListener((view -> binding.discreteScrollView.smoothScrollToPosition(binding.discreteScrollView.getCurrentItem() + 1)));
        binding.imageRight.setOnClickListener((view -> binding.discreteScrollView.smoothScrollToPosition(binding.discreteScrollView.getCurrentItem() - 1)));

        binding.btnPlay.setOnClickListener(view -> {
            binding.videoView.setVisibility(View.GONE);
            binding.webView.setVisibility(View.VISIBLE);
            binding.btnPlay.setVisibility(View.GONE);
            setUpWebView();
        });
        updateUI();
        getVideoUrl();
    }

    private void getVideoUrl() {
        try {

            Api.getService(lang)
                    .appData()
                    .enqueue(new Callback<AboutAppModel>() {
                        @Override
                        public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {

                                videoPath = response.body().getVideo_link();
                                Log.e("vid",videoPath);
                                activity.videoPath = videoPath;
                                binding.btnPlay.setVisibility(View.VISIBLE);
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void setUpWebView() {
        isFirstTime = false;
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.loadUrl(videoPath);
        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                              //   binding.webView.onPause();

                                             }

                                             @Override
                                             public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                                 super.onReceivedError(view, request, error);
                                                 binding.webView.setVisibility(View.INVISIBLE);
                                             }
                                             @Override
                                             public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                                 super.onReceivedHttpError(view, request, errorResponse);
                                                 binding.webView.setVisibility(View.INVISIBLE);
                                             }
                                         }

        );
    }

    private void updateUI() {
        categoryModelList.add(new CategoryModel(R.drawable.ic_resume, getString(R.string.new_order)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_enterprise, getString(R.string.about_app)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_chat, getString(R.string.comments)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_contract, getString(R.string.guide)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_shield, getString(R.string.guarantees)));
        adapter = new CategoryAdapter(categoryModelList, activity,this);
        binding.discreteScrollView.setAdapter(adapter);

        new Handler()
                .postDelayed(() -> {
                    binding.discreteScrollView.scrollToPosition(2);
                    binding.imageLeft.setVisibility(View.VISIBLE);
                    binding.imageRight.setVisibility(View.VISIBLE);
                }, 500);

    }

    private void navigateToInfoActivity(int image) {
        Intent intent = new Intent(activity, InfoActivity.class);
        intent.putExtra("image", image);
        startActivity(intent);
    }

    public void setItemPos(int pos) {
        if (pos == 0) {
            activity.displayFragmentChooseOrderType();
        } else if (pos == 1) {
            activity.navigateToAboutAppActivity(2);
        } else if (pos == 2) {
            activity.navigateToCommentsActivity();
        } else if (pos == 3) {
            activity.navigateToGuideActivity();
        } else if (pos == 4) {
            activity.navigateToAboutAppActivity(3);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstTime)
        {
            binding.webView.onResume();
        }
    }

    public void pauseVideo()
    {
        binding.webView.onPause();

    }

    public void playVideo()
    {
        binding.webView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        binding.webView.onPause();
        Log.e("sss","ggg");
    }
}
