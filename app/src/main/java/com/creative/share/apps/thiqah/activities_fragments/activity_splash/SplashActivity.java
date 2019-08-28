package com.creative.share.apps.thiqah.activities_fragments.activity_splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.thiqah.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.thiqah.databinding.ActivitySplashBinding;
import com.creative.share.apps.thiqah.preferences.Preferences;
import com.creative.share.apps.thiqah.tags.Tags;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private MediaPlayer mp;
    private Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initView();
    }

    private void initView()
    {
        preferences = Preferences.newInstance();

        String path = "android.resource://"+getPackageName()+"/"+R.raw.sp;
        binding.videoView.setVideoPath(path);

        binding.videoView.setOnPreparedListener(mediaPlayer -> {
            mp = mediaPlayer;
            mp.setOnCompletionListener(mediaPlayer1 -> {
                mediaPlayer1.reset();
                String session = preferences.getSession(SplashActivity.this);
                if (session.equals(Tags.session_login))
                {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else
                    {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }

            });
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp!=null&&mp.isPlaying())
        {
            mp.reset();
        }
        binding.videoView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.videoView.isPlaying())
        {
            binding.videoView.resume();
        }else
            {
                binding.videoView.start();

            }


    }



}
