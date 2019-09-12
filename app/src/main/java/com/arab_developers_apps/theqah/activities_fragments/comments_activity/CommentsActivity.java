package com.arab_developers_apps.theqah.activities_fragments.comments_activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arab_developers_apps.theqah.R;
import com.arab_developers_apps.theqah.adapters.CommentAdapter;
import com.arab_developers_apps.theqah.databinding.ActivityCommentsBinding;
import com.arab_developers_apps.theqah.interfaces.Listeners;
import com.arab_developers_apps.theqah.language.LanguageHelper;
import com.arab_developers_apps.theqah.models.CommentDataModel;
import com.arab_developers_apps.theqah.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCommentsBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<CommentDataModel.Testimonials.CommentModel> commentModelList;
    private CommentAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comments);
        initView();
    }



    private void initView()
    {
        commentModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new CommentAdapter(commentModelList,this);
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total_items = adapter.getItemCount();
                int last_item_pos = manager.findLastCompletelyVisibleItemPosition();

                if (dy>0)
                {
                    if ((total_items-last_item_pos)==5&&!isLoading)
                    {
                        commentModelList.add(null);
                        adapter.notifyItemInserted(commentModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);

                    }
                }

            }
        });

        getComments();
    }
    private void getComments()
    {
        try {

            Api.getService(lang)
                    .getAllComments(1)
                    .enqueue(new Callback<CommentDataModel>() {
                        @Override
                        public void onResponse(Call<CommentDataModel> call, Response<CommentDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getTestimonials()!=null&&response.body().getTestimonials().getData()!=null)
                            {
                                commentModelList.clear();
                                if (response.body().getTestimonials().getData().size()>0)
                                {
                                    commentModelList.addAll(response.body().getTestimonials().getData());
                                    adapter.notifyDataSetChanged();
                                    binding.llNoComment.setVisibility(View.GONE);
                                }else
                                {
                                    binding.llNoComment.setVisibility(View.VISIBLE);

                                }

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(CommentsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(CommentsActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(CommentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CommentsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CommentsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }
    private void loadMore(int page)
    {

        try {
            commentModelList.remove(commentModelList.size()-1);
            adapter.notifyItemRemoved(commentModelList.size()-1);

            Api.getService(lang)
                    .getAllComments(page)
                    .enqueue(new Callback<CommentDataModel>() {
                        @Override
                        public void onResponse(Call<CommentDataModel> call, Response<CommentDataModel> response) {

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getTestimonials()!=null&&response.body().getTestimonials().getData()!=null)
                            {
                                if (response.body().getTestimonials().getData().size()>0)
                                {
                                    commentModelList.addAll(response.body().getTestimonials().getData());
                                    adapter.notifyDataSetChanged();
                                    current_page = response.body().getTestimonials().getCurrent_page();
                                    isLoading  =false;
                                }

                            }else
                            {
                                isLoading  =false;

                                if (response.code() == 500) {
                                    Toast.makeText(CommentsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(CommentsActivity.this,"User Unauthenticated", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(CommentsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentDataModel> call, Throwable t) {
                            try {
                                isLoading  =false;

                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CommentsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CommentsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){

        }
    }
    @Override
    public void back() {
        finish();
    }


}

