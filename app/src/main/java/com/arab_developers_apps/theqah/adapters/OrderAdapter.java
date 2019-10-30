package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.my_order.MyOrderActivity;
import com.arab_developers_apps.theqah.databinding.LoadmoreRowBinding;
import com.arab_developers_apps.theqah.databinding.OrderRowBinding;
import com.arab_developers_apps.theqah.models.OrderDataModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_DATA = 1;
    private final int LOAD = 2;

    private List<OrderDataModel.OrderModel> orderList;
    private Context context;
    private MyOrderActivity activity;

    public OrderAdapter(List<OrderDataModel.OrderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        activity = (MyOrderActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            OrderRowBinding orderRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_row, parent, false);
            return new MyHolder(orderRowBinding);
        } else {
            LoadmoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.loadmore_row, parent, false);
            return new LoadHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            OrderDataModel.OrderModel orderModel = orderList.get(position);
            myHolder.orderRowBinding.setOrderMode(orderModel);
            myHolder.BindData(orderModel);
            myHolder.orderRowBinding.btnDetails.setOnClickListener(view -> {
                OrderDataModel.OrderModel orderModel1 = orderList.get(holder.getAdapterPosition());

                activity.setItemData(orderModel1);
            });
         /*   myHolder.orderRowBinding.btnAppeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.makeApeal(orderList.get(myHolder.getLayoutPosition()));
                }
            });*/
        } else {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.setIndeterminate(true);
        }


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

        public void BindData(OrderDataModel.OrderModel orderModel) {

            if (orderModel.getStatus() == 0) {
                orderRowBinding.tvTransState.setVisibility(View.GONE);
                orderRowBinding.ll.setVisibility(View.VISIBLE);

                step1();

            } else if (orderModel.getStatus() == 1 || orderModel.getStatus() == 2) {
                orderRowBinding.ll.setVisibility(View.VISIBLE);

                orderRowBinding.tvTransState.setVisibility(View.GONE);

                step2();
            }


            else if (orderModel.getStatus() == 3) {
                orderRowBinding.ll.setVisibility(View.GONE);
                orderRowBinding.tvTransState.setVisibility(View.VISIBLE);
                orderRowBinding.tvOrderState.setText(context.getString(R.string.refused_transfer));
            } else if (orderModel.getStatus() == 4) {
                orderRowBinding.ll.setVisibility(View.VISIBLE);
                orderRowBinding.tvTransState.setVisibility(View.GONE);

                step3();

            } else if (orderModel.getStatus() == 5) {
                orderRowBinding.ll.setVisibility(View.VISIBLE);
                orderRowBinding.tvTransState.setVisibility(View.GONE);
                step4();
            } else if (orderModel.getStatus() == 6) {
                step5();

            }
        }


        private void step1() {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image2.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image3.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);

        }

        private void step2() {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image3.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);


        }

        private void step3() {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image4.setBackgroundResource(R.drawable.un_checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context, R.color.gray4));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);


        }

        private void step4() {

            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image4.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context, R.color.gray4));
            orderRowBinding.image5.setBackgroundResource(R.drawable.un_checked_circle);

        }

        private void step5() {
            orderRowBinding.tv1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image1.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image2.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));


            orderRowBinding.tv3.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image3.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view3.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv4.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image4.setBackgroundResource(R.drawable.checked_circle);
            orderRowBinding.view4.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            orderRowBinding.tv5.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            orderRowBinding.image5.setBackgroundResource(R.drawable.checked_circle);

        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadmoreRowBinding binding;

        public LoadHolder(@NonNull LoadmoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        OrderDataModel.OrderModel orderModel = orderList.get(position);
        if (orderModel != null) {
            return ITEM_DATA;
        } else {
            return LOAD;
        }

    }

}
