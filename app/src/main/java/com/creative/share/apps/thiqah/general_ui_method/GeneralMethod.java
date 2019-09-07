package com.creative.share.apps.thiqah.general_ui_method;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void displayImageHome(ImageView imageView, int image_resource)
    {
        imageView.setImageResource(image_resource);
    }


    @BindingAdapter("uri")
    public static void displayImageuri(RoundedImageView imageView,Uri uri)
    {
        Picasso.with(imageView.getContext()).load(uri).fit().into(imageView);
    }
    @BindingAdapter("state")
    public static void orderState(TextView textView,int state)
    {
        Context context = textView.getContext();
        if (state==0)
        {
            textView.setText(context.getString(R.string.new_order));
        }else if (state==1)
        {
            textView.setText(R.string.beneficiary_transfer);
        }
        else if (state==2)
        {
            textView.setText(R.string.shipping);

        }
        else if (state==5)
        {
            textView.setText(R.string.confirmation);

        }
        else if (state==6)
        {
            textView.setText(R.string.transfer_error);

        }
        else if (state==7)
        {
            textView.setText(R.string.receipt_payment);

        }

    }



    @BindingAdapter("imageRoundBank")
    public static void imageRound(RoundedImageView imageView,String image_endPoint)
    {

        if (image_endPoint!=null)
        {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_BANK_URL+image_endPoint)).fit().into(imageView);
        }





    }

    @BindingAdapter("imageRoundItem")
    public static void imageRound2(RoundedImageView imageView,String image_endPoint)
    {

        if (image_endPoint!=null)
        {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_ITEM_URL+image_endPoint)).fit().into(imageView);
        }





    }
    @BindingAdapter("date")
    public static void date (TextView textView,long date)
    {
        Log.e("date",date+"_");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));
        textView.setText(m_date);

    }

    @BindingAdapter("rate")
    public static void rate (SimpleRatingBar simpleRatingBar, float rate)
    {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget(rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }







}
