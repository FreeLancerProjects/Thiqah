package com.creative.share.apps.thiqah.general_ui_method;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

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



    @BindingAdapter({"imageRound","orderType"})
    public static void imageRound(RoundedImageView imageView,String image_endPoint,int order_type)
    {

        if (order_type==1)
        {
            if (image_endPoint!=null)
            {
                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_BANK_URL+image_endPoint)).fit().into(imageView);
            }
        }else
            {
                if (image_endPoint!=null)
                {
                    Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_ITEM_URL+image_endPoint)).fit().into(imageView);
                }
            }



    }





}
