package com.creative.share.apps.thiqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.databinding.GuideRowBinding;
import com.creative.share.apps.thiqah.models.GuideModel;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.MyHolder> {

    private List<GuideModel.Guide> guideList;
    private Context context;

    public GuideAdapter(List<GuideModel.Guide> guideList, Context context) {
        this.guideList = guideList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GuideRowBinding guideRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.guide_row,parent,false);
        return new MyHolder(guideRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        GuideModel.Guide guide = guideList.get(position);
        holder.guideRowBinding.setGuideModel(guide);


    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private GuideRowBinding guideRowBinding;

        public MyHolder(GuideRowBinding guideRowBinding) {
            super(guideRowBinding.getRoot());
            this.guideRowBinding = guideRowBinding;



        }


    }
}
