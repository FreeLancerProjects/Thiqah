package com.arab_developers_apps.theqah.activities_fragments.activity_home;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.about_app.AboutAppActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_contact.ContactUsActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_guide.GuideActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.fragments.Fragment_Choose_Order_Type;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.fragments.Fragment_Main;
import com.arab_developers_apps.theqah.activities_fragments.activity_notification.NotificationActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_sign_in.SignInActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_video.VideoActivity;
import com.arab_developers_apps.theqah.activities_fragments.bank_account.BankActivity;
import com.arab_developers_apps.theqah.activities_fragments.comments_activity.CommentsActivity;
import com.arab_developers_apps.theqah.activities_fragments.my_order.MyOrderActivity;
import com.arab_developers_apps.theqah.databinding.DialogLanguageBinding;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.UserModel;
import com.arab_developers_apps.theqah.preferences.Preferences;
import com.arab_developers_apps.theqah.remote.Api;
import com.arab_developers_apps.theqah.share.Common;
import com.arab_developers_apps.theqah.tags.Tags;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView tvNotificationCount;
    private FrameLayout flNotification;
    private UserModel userModel;
    private Preferences preferences;
    private LinearLayout llMain, llOrder, llShare;
    private ImageView iconMain, iconOrder;
    private TextView tvMain, tvOrder;
    private Fragment_Main fragment_main;
    private Fragment_Choose_Order_Type fragment_choose_order_type;
    private FragmentManager fragmentManager;
    public String videoPath = "";
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            Log.e("MyApp", "Deep link clicked " + uri);
        }
        fragmentManager = getSupportFragmentManager();
        initView();

        if (savedInstanceState==null)
        {
            displayFragmentMain();
        }


        updateUserToken();

    }



    private void initView() {

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        llMain = findViewById(R.id.llMain);
        llOrder = findViewById(R.id.llOrder);
        llShare = findViewById(R.id.llShare);
        iconMain = findViewById(R.id.iconMain);
        iconOrder = findViewById(R.id.iconOrder);
        tvMain = findViewById(R.id.tvMain);
        tvOrder = findViewById(R.id.tvOrder);


        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        flNotification = findViewById(R.id.flNotification);

        flNotification.setOnClickListener(view -> {

            if (userModel != null) {
                navigateToNotificationActivity();
            } else {
                Common.CreateNoSignAlertDialog(HomeActivity.this);
            }
        });

        llMain.setOnClickListener(view -> displayFragmentMain());

        llOrder.setOnClickListener(view ->displayFragmentChooseOrderType());

        llShare.setOnClickListener(view -> CreateIntentShare());

        String lastVisit = preferences.getLastVisit(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String now = dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

        if (!lastVisit.equals(now))
        {
            updateVisit(now,(Calendar.getInstance().getTimeInMillis()/1000));

        }

        if (userModel==null)
        {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);

        }else {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        }
    }

    private void updateUserToken() {
        if (userModel!=null)
        {

            Log.e("token",userModel.getToken());
            //EventBus.getDefault().register(this);

            FirebaseInstanceId.getInstance()
                    .getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                        {
                            String token = task.getResult().getToken();

                            Api.getService(Tags.base_url)
                                    .update_token("Bearer "+userModel.getToken(),token)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful())
                                            {
                                                Log.e("token","updated successfully");
                                            }else
                                                {
                                                    Log.e("token","updated failed");

                                                    try {
                                                        Log.e("code_error",response.code()+"_"+response.errorBody().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("token",t.getMessage());

                                            }catch (Exception e){}

                                        }
                                    });
                        }
                    });
        }
    }

    private void updateVisit(String now, long time) {
        Api.getService(Tags.base_url)
                .updateVisit(now,1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            preferences.setLastVisit(HomeActivity.this,now);
                        }else
                        {
                            try {
                                Log.e("errorVisitCode",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage()+"_");
                        }catch (Exception e){}
                    }
                });
    }

    private void updateNotificationCount(int count) {
        if (count > 0) {
            tvNotificationCount.setText(String.valueOf(count));
            tvNotificationCount.setVisibility(View.VISIBLE);
        } else {
            tvNotificationCount.setText(String.valueOf(count));
            tvNotificationCount.setVisibility(View.GONE);
        }
    }


    private void selectMain() {
        llMain.setBackgroundResource(R.color.white);
        tvMain.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        iconMain.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        llOrder.setBackgroundResource(R.color.colorPrimary);
        tvOrder.setTextColor(ContextCompat.getColor(this, R.color.white));
        iconOrder.setColorFilter(ContextCompat.getColor(this, R.color.white));
    }

    private void selectOrder() {
        llOrder.setBackgroundResource(R.color.white);
        tvOrder.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        iconOrder.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        llMain.setBackgroundResource(R.color.colorPrimary);
        tvMain.setTextColor(ContextCompat.getColor(this, R.color.white));
        iconMain.setColorFilter(ContextCompat.getColor(this, R.color.white));

    }


    public void displayFragmentMain()
    {
        selectMain();

        if (fragment_main==null)
        {
            fragment_main = Fragment_Main.newInstance();
        }

        if (fragment_main.isAdded())
        {
            fragment_main.playVideo();
            fragmentManager.beginTransaction().show(fragment_main).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_main,"fragment_main").addToBackStack("fragment_main").commit();
            }

        if (fragment_choose_order_type!=null&&fragment_choose_order_type.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_choose_order_type).commit();

        }


    }
    public void displayFragmentChooseOrderType()
    {
        selectOrder();

        if (fragment_choose_order_type==null)
        {
            fragment_choose_order_type = Fragment_Choose_Order_Type.newInstance();
        }

        if (fragment_choose_order_type.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_choose_order_type).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_choose_order_type,"fragment_choose_order_type").addToBackStack("fragment_choose_order_type").commit();
        }

        if (fragment_main!=null&&fragment_main.isAdded())
        {
            fragment_main.pauseVideo();
            fragmentManager.beginTransaction().hide(fragment_main).commit();

        }


    }

    private void CreateIntentShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String video_url = videoPath;
        String android_url = "shorturl.at/hstVY";
        String content = "(تطبيق ثقة)"+"\n"+"وسيط مالي"+"\n"+"يضمن حق الطرفين في البيع والشراء"+"\n"+"وتقديم الخدمات"+"\n"+"رابط الفيديو التعريفي :"+"\n"+video_url+"\n\n"+"رابط الاندرويد :"+"\n"+android_url;
        intent.putExtra(Intent.EXTRA_TEXT,content);
        startActivity(intent);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayFragmentMain();

        } else if (id == R.id.nav_new_order) {
            displayFragmentChooseOrderType();

            /*if (userModel!=null)
            {
                displayFragmentChooseOrderType();

            }else
            {
                Common.CreateNoSignAlertDialog(HomeActivity.this);
            }*/
        }else if (id == R.id.nav_my_order) {
            if (userModel!=null)
            {
                navigateToMyOrderActivity();

            }else
                {
                    Common.CreateNoSignAlertDialog(HomeActivity.this);
                }
        } else if (id == R.id.nav_bank_account) {
            navigateToBankActivity();
        } else if (id == R.id.nav_guide) {
            navigateToGuideActivity();
        }else if (id == R.id.nav_not) {
            if (userModel != null) {
                navigateToNotificationActivity();
            } else {
                Common.CreateNoSignAlertDialog(HomeActivity.this);
            }        } else if (id == R.id.nav_comments) {
            navigateToCommentsActivity();
        } else if (id == R.id.nav_about) {
            navigateToAboutAppActivity(2);


        } else if (id == R.id.nav_video) {
            navigateToVideoActivity(videoPath);
        } else if (id == R.id.nav_guarantees) {
            navigateToAboutAppActivity(3);


        } else if (id == R.id.nav_terms) {
            navigateToAboutAppActivity(1);


        } else if (id == R.id.nav_contact) {
            navigateToContactActivity();
        }else if (id == R.id.nav_rate) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (id == R.id.nav_lang) {
            CreateLangDialog();
        } else if (id == R.id.nav_logout) {
            logout();
        }
        else if (id == R.id.nav_login) {
            navigateToSignInActivity();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


    public void navigateToGuideActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
    }



    public void navigateToAboutAppActivity(int type) {
        Intent intent = new Intent(this, AboutAppActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void navigateToVideoActivity(String videoPath) {

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("video_path", videoPath);
        startActivity(intent);
    }

    public void navigateToCommentsActivity()
    {
        Intent intent = new Intent(this, CommentsActivity.class);
        startActivity(intent);
    }
    private void navigateToContactActivity()
    {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

    private void navigateToNotificationActivity() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    public void navigateToBankActivity() {
        Intent intent = new Intent(this, BankActivity.class);
        startActivity(intent);
    }

    private void navigateToMyOrderActivity() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    private void CreateLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("ar"), 1000);
        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("en"), 1000);
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void refreshActivity(String lang) {
        preferences.selectedLanguage(this, lang);
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    private void logout() {
        if (userModel != null) {


            ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(lang)
                    .logout("Bearer "+userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if (manager!=null)
                                {
                                    manager.cancelAll();

                                }

                                preferences.clear(HomeActivity.this);
                                navigateToSignInActivity();

                            }else
                            {
                                if (response.code()==500)
                                {
                                    Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }else if (response.code()==401)
                                {
                                    Toast.makeText(HomeActivity.this, "User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                }
                                try {


                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());

                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(HomeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });

        } else {
            new Handler()
                    .postDelayed(this::navigateToSignInActivity, 500);

        }

    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment_main!=null&&fragment_main.isAdded()&&fragment_main.isVisible())
            {
                if (userModel == null) {
                    navigateToSignInActivity();
                } else {
                    finish();
                }
            }else
                {
                    displayFragmentMain();
                }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
           // EventBus.getDefault().unregister(this);
        }
    }
    /*
    private void getNotificationCount()
    {
        Api.getService(Tags.base_url)
                .getNotifications(userModel.getUser().getId(),"count_unread")
                .enqueue(new Callback<NotificationCountModel>() {
                    @Override
                    public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                updateNotificationCount(response.body().getCount_unread());
                            }

                        }else
                        {
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                        try
                        {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }*/
}
