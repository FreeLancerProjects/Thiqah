package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_appeal_details.AppealDetailsActivity;
import com.arab_developers_apps.theqah.databinding.ComplainImageRowBinding;

import java.util.List;

public class ComplainImagesAdapter extends RecyclerView.Adapter<ComplainImagesAdapter.MyHolder> {

    private List<String> imageList;
    private Context context;
    private AppealDetailsActivity activity;

    public ComplainImagesAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        this.activity = (AppealDetailsActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComplainImageRowBinding imageRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.complain_image_row,parent,false);
        return new MyHolder(imageRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        String endPoint = imageList.get(position);
        holder.imageRowBinding.setEndPoint(endPoint);
        holder.itemView.setOnClickListener(view -> {
            String endPoint1 = imageList.get(holder.getAdapterPosition());
            activity.setImageItem(endPoint1);

        });


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ComplainImageRowBinding imageRowBinding;

        public MyHolder(ComplainImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
