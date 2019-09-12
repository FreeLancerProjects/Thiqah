package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.activities_fragments.activity_appeal.AppealActivity;
import com.arab_developers_apps.theqah.databinding.ImageRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<Uri> uriList;
    private Context context;
    private AppealActivity activity;

    public ImagesAdapter(List<Uri> uriList, Context context) {
        this.uriList = uriList;
        this.context = context;
        activity = (AppealActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding imageRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(imageRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri uri = uriList.get(position);
        holder.imageRowBinding.setUri(uri);
        holder.imageRowBinding.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setItemPosForDeleting(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;

        public MyHolder(ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
