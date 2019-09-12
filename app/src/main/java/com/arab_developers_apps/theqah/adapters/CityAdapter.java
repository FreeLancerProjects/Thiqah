package com.arab_developers_apps.theqah.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.SpinnerCityRowBinding;
import com.arab_developers_apps.theqah.models.Cities_Payment_Bank_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CityAdapter extends BaseAdapter {
    private List<Cities_Payment_Bank_Model.City> cityList;
    private Context context;
    private String lang;
    public CityAdapter(List<Cities_Payment_Bank_Model.City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int i) {
        return cityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerCityRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_city_row,viewGroup,false);
        binding.setLang(lang);
        binding.setCityModel(cityList.get(i));
        return binding.getRoot();
    }
}
