package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_order_buyer.BuyerActivity;
import com.arab_developers_apps.theqah.databinding.PaymentRowBinding;
import com.arab_developers_apps.theqah.models.Cities_Payment_Bank_Model;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {
    private List<Cities_Payment_Bank_Model.Payment> paymentList;
    private Context context;
    private BuyerActivity activity;
    private int selected_pos =-1;

    public PaymentAdapter(List<Cities_Payment_Bank_Model.Payment> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
        activity = (BuyerActivity) context;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentRowBinding paymentRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.payment_row,parent,false);
        return new PaymentHolder(paymentRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        Cities_Payment_Bank_Model.Payment payment = paymentList.get(position);
        holder.paymentRowBinding.setPayment(payment);

        if (selected_pos!=-1)
        {
            if (selected_pos==position)
            {
                holder.paymentRowBinding.rb.setChecked(true);

            }else
                {
                    holder.paymentRowBinding.rb.setChecked(false);

                }

        }else
            {
                holder.paymentRowBinding.rb.setChecked(false);

            }

        holder.itemView.setOnClickListener(view -> {
            selected_pos = holder.getAdapterPosition();
            Cities_Payment_Bank_Model.Payment payment1 = paymentList.get(holder.getAdapterPosition());
            activity.setItemData(payment1);
            notifyDataSetChanged();
        });

        holder.paymentRowBinding.rb.setOnClickListener(view -> {
            selected_pos = holder.getAdapterPosition();
            Cities_Payment_Bank_Model.Payment payment1 = paymentList.get(holder.getAdapterPosition());
            activity.setItemData(payment1);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        private PaymentRowBinding paymentRowBinding;
        public PaymentHolder(@NonNull PaymentRowBinding paymentRowBinding) {
            super(paymentRowBinding.getRoot());
            this.paymentRowBinding = paymentRowBinding;
        }
    }
}
