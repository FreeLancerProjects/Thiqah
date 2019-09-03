package com.creative.share.apps.thiqah.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.activity_notification.NotificationActivity;
import com.creative.share.apps.thiqah.databinding.LoadmoreRowBinding;
import com.creative.share.apps.thiqah.databinding.NotificationRowBinding;
import com.creative.share.apps.thiqah.models.NotificationDataModel;

import java.util.List;

public class MyNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private Context context;
    private LayoutInflater inflater;
    private NotificationActivity activity;

    public MyNotificationAdapter(List<NotificationDataModel.NotificationModel> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (NotificationActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_DATA)
        {
            NotificationRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.notification_row,parent,false);
            return new NotificationHolder(binding);

        }else
            {
                LoadmoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.loadmore_row,parent,false);
                return new LoadHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationDataModel.NotificationModel notificationModel = notificationModelList.get(position);
        if (holder instanceof NotificationHolder)
        {
            NotificationHolder notificationHolder = (NotificationHolder) holder;
            notificationHolder.binding.setNotificationModel(notificationModel);



            if (notificationModel.getAction()==2||notificationModel.getAction()==3)
            {
                notificationHolder.binding.tvDetails.setVisibility(View.VISIBLE);
                notificationHolder.binding.tvReceiveMoney.setVisibility(View.GONE);

            }else if (notificationModel.getAction()==6)
            {
                notificationHolder.binding.tvReceiveMoney.setVisibility(View.VISIBLE);
                notificationHolder.binding.tvDetails.setVisibility(View.GONE);

            }else {
                notificationHolder.binding.tvReceiveMoney.setVisibility(View.GONE);

                notificationHolder.binding.tvDetails.setVisibility(View.GONE);

                }
            notificationHolder.binding.getRoot().setOnClickListener(view -> {

                NotificationDataModel.NotificationModel notificationModel1 = notificationModelList.get(notificationHolder.getAdapterPosition());
                activity.setItemData(notificationModel1);
            });

        }else
            {
                LoadHolder loadHolder = (LoadHolder) holder;
                loadHolder.binding.progBar.setIndeterminate(true);
            }

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        public NotificationRowBinding binding;
        public NotificationHolder(@NonNull NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadmoreRowBinding binding;
        public LoadHolder(@NonNull LoadmoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        NotificationDataModel.NotificationModel notificationModel = notificationModelList.get(position);
        if (notificationModel!=null)
        {
            return ITEM_DATA;
        }else
            {
                return LOAD;
            }

    }


}
