package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.entity.Forum_Pl;
import com.student.aynu.entity.Home_News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class Forum_DetailAdapter extends RecyclerView.Adapter<Forum_DetailAdapter.MyViewHolder> {

    private List<Forum_Pl.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;

    public Forum_DetailAdapter(List<Forum_Pl.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.activity_forum_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Glide.with(mContext).load(mLists.get(position).getUserHead()).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.mHeadImg);
        if (mLists.get(position).getUserName().equals("")) {
            holder.mUserText.setText(mLists.get(position).getUserAccount());
        } else {
            holder.mUserText.setText(mLists.get(position).getUserName());
        }
        holder.mLcText.setText(mLists.get(position).getLc());
        holder.mContentText.setText(mLists.get(position).getFplinfo());
        holder.mTimeText.setText(mLists.get(position).getFpltime());

        //如果fplyid==0,说明是回复楼主的，如果不为0，说明回复评论的
        if (mLists.get(position).getFplyid().equals("0")) {
            holder.mHuiFuLayout.setVisibility(View.GONE);
        } else {
            holder.mHuiFuLayout.setVisibility(View.VISIBLE);
            //如果有昵称 显示昵称。
            if ("".equals(mLists.get(position).getY_userName())) {
                holder.mHuiFuTimeText.setText(mLists.get(position).getY_userAccount() + " 发表于 " + mLists.get(position).getY_ftime());
            } else {
                holder.mHuiFuTimeText.setText(mLists.get(position).getY_userName() + " 发表于 " + mLists.get(position).getY_ftime());
            }
            holder.mHuiFuContentText.setText(mLists.get(position).getY_fcontent());
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.forum_recycler_img)
        RoundedImageView mHeadImg;
        @BindView(R.id.forum_recycler_user)
        TextView mUserText;
        @BindView(R.id.forum_recycler_lc)
        TextView mLcText;
        @BindView(R.id.forum_recycler_content)
        TextView mContentText;
        @BindView(R.id.forum_recycler_time)
        TextView mTimeText;

        @BindView(R.id.forum_recycler_forum_relative)
        RelativeLayout mHuiFuLayout;
        @BindView(R.id.forum_recycler_forum_info)
        TextView mHuiFuTimeText;
        @BindView(R.id.forum_recycler_forum_text)
        TextView mHuiFuContentText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<Forum_Pl.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<Forum_Pl.DataBean> mLists) {
        this.mLists = (mLists);
    }
}
