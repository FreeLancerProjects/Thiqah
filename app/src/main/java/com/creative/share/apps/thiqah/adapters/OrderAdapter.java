package com.creative.share.apps.thiqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.my_order.MyOrderActivity;
import com.creative.share.apps.thiqah.databinding.OrderRowBinding;
import com.creative.share.apps.thiqah.models.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    private List<OrderModel> orderList;
    private Context context;
    private MyOrderActivity activity;

    public OrderAdapter(List<OrderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        activity = (MyOrderActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderRowBinding orderRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_row,parent,false);
        return new MyHolder(orderRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        OrderModel orderModel = orderList.get(position);
        holder.orderRowBinding.setOrderModel(orderModel);
        holder.BindData(orderModel);
        holder.orderRowBinding.btnDetails.setOnClickListener(view -> {
            OrderModel orderModel1 = orderList.get(holder.getAdapterPosition());

            activity.setItemData(orderModel1);
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private OrderRowBinding orderRowBinding;

        public MyHolder(OrderRowBinding orderRowBinding) {
            super(orderRowBinding.getRoot());
            this.orderRowBinding = orderRowBinding;



        }

        public void BindData(OrderModel orderModel)
        {
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
                orderRowBinding.ll.setVisibility(View.GONE);
            }
            else if (orderModel.getStatus()==7)
            {
                step5();
            }
        }



        private void step1()
        {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image2.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image3.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);

        }

        private void step2()
        {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image3.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);


        }
        private void step3()
        {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);


        }
        private void step4()
        {

            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image4.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);

        }
        private void step5()
        {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image4.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            orderRowBinding.image5.setBackgroundResource(R.drawable.checked_circle);

        }
    }
}
