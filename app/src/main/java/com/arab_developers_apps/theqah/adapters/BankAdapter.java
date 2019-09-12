package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.BankRowBinding;
import com.arab_developers_apps.theqah.models.Cities_Payment_Bank_Model;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyHolder> {

    private List<Cities_Payment_Bank_Model.Bank> bankDataModelList;
    private Context context;

    public BankAdapter(List<Cities_Payment_Bank_Model.Bank> bankDataModelList, Context context) {
        this.bankDataModelList = bankDataModelList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bank_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Cities_Payment_Bank_Model.Bank bankModel = bankDataModelList.get(position);
        holder.bankRowBinding.setBankModel(bankModel);


    }

    @Override
    public int getItemCount() {
        return bankDataModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private BankRowBinding bankRowBinding;

        public MyHolder(BankRowBinding bankRowBinding) {
            super(bankRowBinding.getRoot());
            this.bankRowBinding = bankRowBinding;



        }


    }
}
