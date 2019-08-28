package com.creative.share.apps.thiqah.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.SpinnerRowBinding;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private List<String> period;
    private Context context;

    public SpinnerAdapter(List<String> period, Context context) {
        this.period = period;
        this.context = context;
    }

    @Override
    public int getCount() {
        return period.size();
    }

    @Override
    public Object getItem(int i) {
        return period.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_row,viewGroup,false);

        String title = period.get(i);
        binding.setTitle(title);
        return binding.getRoot();
    }
}
