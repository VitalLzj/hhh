package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.student.aynu.R;
import com.student.aynu.entity.Home_News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<Home_News.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;

    public NewsAdapter(List<Home_News.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.fragment_home_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(mLists.get(position).getNews_title());
        holder.mInfo.setText(mLists.get(position).getNews_info());
        holder.mTime.setText(mLists.get(position).getNews_time());
        Glide.with(mContext).load(mLists.get(position).getNews_thumbnail()).placeholder(R.mipmap.ic_launcher).into(holder.mImg);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_recycler_title)
        TextView mTitle;
        @BindView(R.id.home_recycler_content)
        TextView mInfo;
        @BindView(R.id.home_recycler_time)
        TextView mTime;
        @BindView(R.id.home_recycler_img)
        ImageView mImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //刷新
    public void refresh(List<Home_News.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<Home_News.DataBean> mLists) {
        this.mLists = (mLists);
    }
}
