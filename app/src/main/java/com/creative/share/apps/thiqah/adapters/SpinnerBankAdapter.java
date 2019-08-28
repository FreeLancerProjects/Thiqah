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
import com.creative.share.apps.thiqah.models.Cities_Payment_Bank_Model;

import java.util.List;

public class SpinnerBankAdapter extends BaseAdapter {
    private List<Cities_Payment_Bank_Model.Bank> bankList;
    private Context context;

    public SpinnerBankAdapter(List<Cities_Payment_Bank_Model.Bank> bankList, Context context) {
        this.bankList = bankList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bankList.size();
    }

    @Override
    public Object getItem(int i) {
        return bankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_row,viewGroup,false);

        String name = bankList.get(i).getBank_name();
        binding.setTitle(name);
        return binding.getRoot();
    }
}
