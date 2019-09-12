package com.arab_developers_apps.theqah.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_home.HomeActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_buyer.BuyerActivity;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_seller.OrderSellerActivity;
import com.arab_developers_apps.theqah.databinding.FragmentChooseOrderTypeBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Choose_Order_Type extends Fragment implements Listeners.BackListener,Listeners.OrderActionListener{

    private FragmentChooseOrderTypeBinding binding;
    private String lang;
    private HomeActivity activity;

    public static Fragment_Choose_Order_Type newInstance() {
        return new Fragment_Choose_Order_Type();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_choose_order_type, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);
        binding.setBackListener(this);
    }

    @Override
    public void back() {
        activity.finish();
    }

    @Override
    public void orderBuyer() {
        Intent intent = new Intent(activity, BuyerActivity.class);
        startActivity(intent);
    }

    @Override
    public void orderSeller() {
        Intent intent = new Intent(activity, OrderSellerActivity.class);
        startActivity(intent);

    }
}
