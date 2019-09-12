package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.GuideRowBinding;
import com.arab_developers_apps.theqah.models.GuideModel;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.MyHolder> {

    private List<GuideModel.Guide> guideList;
    private List<Integer> image_resources ;
    private Context context;

    public GuideAdapter(List<GuideModel.Guide> guideList, Context context) {
        this.guideList = guideList;
        this.context = context;
        image_resources = new ArrayList<>();
        image_resources.add(R.drawable.click);
        image_resources.add(R.drawable.time);
        image_resources.add(R.drawable.right);

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
        holder.guideRowBinding.image.setImageResource(image_resources.get(position));


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
