package com.arab_developers_apps.theqah.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.databinding.CommentRowBinding;
import com.arab_developers_apps.theqah.databinding.LoadmoreRowBinding;
import com.arab_developers_apps.theqah.models.CommentDataModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<CommentDataModel.Testimonials.CommentModel> commentModelList;
    private Context context;
    private LayoutInflater inflater;

    public CommentAdapter(List<CommentDataModel.Testimonials.CommentModel> commentModelList, Context context) {
        this.commentModelList = commentModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_DATA)
        {
            CommentRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.comment_row,parent,false);
            return new CommentHolder(binding);

        }else
            {
                LoadmoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.loadmore_row,parent,false);
                return new LoadHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentDataModel.Testimonials.CommentModel commentModel = commentModelList.get(position);
        if (holder instanceof CommentHolder)
        {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.binding.setCommentModel(commentModel);



        }else
            {
                LoadHolder loadHolder = (LoadHolder) holder;
                loadHolder.binding.progBar.setIndeterminate(true);
            }

    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public CommentRowBinding binding;
        public CommentHolder(@NonNull CommentRowBinding binding) {
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
        CommentDataModel.Testimonials.CommentModel commentModel = commentModelList.get(position);
        if (commentModel!=null)
        {
            return ITEM_DATA;
        }else
            {
                return LOAD;
            }

    }


}
