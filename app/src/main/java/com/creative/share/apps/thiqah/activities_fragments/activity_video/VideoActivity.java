package com.creative.share.apps.thiqah.activities_fragments.activity_video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.ActivityVideoBinding;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.AboutAppModel;
import com.creative.share.apps.thiqah.remote.Api;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity {
    private ActivityVideoBinding binding;
    private String videoPath="";
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            videoPath = intent.getStringExtra("video_path");
        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang",Locale.getDefault().getLanguage());

        if (!videoPath.isEmpty())
        {
            setUpWebView();
        }else
            {
                getVideoPath();
            }

    }

    private void getVideoPath() {
        try {

            Api.getService(lang)
                    .appData()
                    .enqueue(new Callback<AboutAppModel>() {
                        @Override
                        public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                            if (response.isSuccessful() && response.body() != null) {

                                videoPath = response.body().getVideo_link();
                                setUpWebView();
                            } else {

                                if (response.code() == 422) {
                                    Toast.makeText(VideoActivity.this, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(VideoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(VideoActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(VideoActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(VideoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.loadUrl(videoPath);
        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
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

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }
}
