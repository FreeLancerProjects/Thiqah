package com.arab_developers_apps.theqah.activities_fragments.activity_splash_2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_sign_in.SignInActivity;
import com.arab_developers_apps.theqah.databinding.ActivitySplashBinding;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.tags.Tags;

public class Splash2Activity extends AppCompatActivity {
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
                String session = preferences.getSession(Splash2Activity.this);
                if (session.equals(Tags.session_login))
                {
                    Intent intent = new Intent(Splash2Activity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Intent intent = new Intent(Splash2Activity.this, SignInActivity.class);
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


