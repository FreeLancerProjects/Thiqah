package com.creative.share.apps.thiqah.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.thiqah.databinding.HomeCircleRowBinding;
import com.creative.share.apps.thiqah.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
    private List<CategoryModel> categoryModelList;
    private Context context;
    private Fragment_Main fragment_main;
    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context,Fragment_Main fragment_main) {
        this.categoryModelList = categoryModelList;
        this.context = context;
        this.fragment_main = fragment_main;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeCircleRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_circle_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.setCategoryModel(categoryModelList.get(position));
        holder.binding.getRoot()
                .setOnClickListener(view -> fragment_main.setItemPos(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private HomeCircleRowBinding binding;
        public MyHolder(@NonNull HomeCircleRowBinding binding) {
            super(binding.getRoot());
            this.binding =binding;
        }
    }
}
