package com.creative.share.apps.thiqah.activities_fragments.order_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.thiqah.interfaces.Listeners;
import com.creative.share.apps.thiqah.language.LanguageHelper;
import com.creative.share.apps.thiqah.models.OrderModel;

import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderDetailsBinding binding;
    private String lang;
    private OrderModel orderModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            orderModel = (OrderModel) intent.getSerializableExtra("data");
            binding.setOrderModel(orderModel);

            if (orderModel.getStatus()<2)
            {
                if (orderModel.getBank_transfer_pic()!=null)
                {
                    binding.llTransferImage.setVisibility(View.VISIBLE);
                }else
                    {
                        binding.llTransferImage.setVisibility(View.GONE);

                    }

                if (orderModel.getItem_pic()!=null)
                {
                    binding.llItemImage.setVisibility(View.VISIBLE);
                }else
                {
                    binding.llItemImage.setVisibility(View.GONE);

                }


            }else
                {
                    if (orderModel.getBank_transfer_pic()!=null)
                    {
                        binding.llTransferImage.setVisibility(View.VISIBLE);
                    }else
                    {
                        binding.llTransferImage.setVisibility(View.GONE);

                    }

                    if (orderModel.getItem_pic()!=null)
                    {
                        binding.llItemImage.setVisibility(View.VISIBLE);
                    }else
                    {
                        binding.llItemImage.setVisibility(View.GONE);

                    }
                }
            if (orderModel.getStatus()==0)
            {
                step1();
            }else if (orderModel.getStatus()==1)
            {
                step2();
            }
            else if (orderModel.getStatus()==2)
            {
                step3();
            }
            else if (orderModel.getStatus()==5)
            {
                step4();
            }
            else if (orderModel.getStatus()==6)
            {
                binding.ll.setVisibility(View.GONE);
            }
            else if (orderModel.getStatus()==7)
            {
                step5();
            }
        }
    }


    private void step1()
    {
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image2.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));


        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image3.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));

        binding.tv4.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);

    }

    private void step2()
    {
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image3.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));

        binding.tv4.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);


    }
    private void step3()
    {
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image4.setBackgroundResource(R.drawable.un_checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this,R.color.gray4));

        binding.tv5.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);


    }
    private void step4()
    {

        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image4.setBackgroundResource(R.drawable.checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv5.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        binding.image5.setBackgroundResource(R.drawable.un_checked_circle);

    }
    private void step5()
    {
        binding.tv1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image1.setBackgroundResource(R.drawable.checked_circle);
        binding.view1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image2.setBackgroundResource(R.drawable.checked_circle);
        binding.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));


        binding.tv3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image3.setBackgroundResource(R.drawable.checked_circle);
        binding.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image4.setBackgroundResource(R.drawable.checked_circle);
        binding.view4.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        binding.tv5.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        binding.image5.setBackgroundResource(R.drawable.checked_circle);

    }

    @Override
    public void back() {
        finish();
    }

}
