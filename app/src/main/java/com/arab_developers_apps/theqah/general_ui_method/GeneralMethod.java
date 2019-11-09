package com.arab_developers_apps.theqah.general_ui_method;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
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

    @BindingAdapter("complainImage")
    public static void complainImage(ImageView imageView,String image_endPoint)
    {

        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_COMPLAINTS_URL+image_endPoint)).fit().into(imageView);





    }

    @BindingAdapter("paymentImage")
    public static void paymentImage(ImageView imageView,String image_endPoint)
    {

        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_PAYMENT_URL+image_endPoint)).fit().into(imageView);





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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));
        textView.setText(m_date);

    }
    @BindingAdapter("time")
    public static void time (TextView textView,long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));
        textView.setText(m_date);

    }
    @BindingAdapter({"created","updated"})
    public static void change (TextView textView,String created,String updated)
    { SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:aa ",Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy hh:mm ",Locale.ENGLISH);
        Date date ;
        try {

            if (updated==null) {
                date=(Date)dateFormat.parse(updated);
            }
            else {
                date=(Date)dateFormat.parse(created);

            }
            String m_date = dateFormat2.format(date.getTime());
       //     Log.e("lll",m_date+date.toString());
            textView.setText(m_date);
        }
        catch (Exception e){
Log.e("llkkcks",e.getMessage());
        }


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
