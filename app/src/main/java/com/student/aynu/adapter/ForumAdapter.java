package com.student.aynu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.activity.PicActivity;
import com.student.aynu.entity.Forum_entity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder> {

    private List<Forum_entity.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;

    private Forum_PicAdapter mAdapter;
    private List<String> mImgs;

    public ForumAdapter(List<Forum_entity.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.fragment_forum_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mLists.get(position).getUserHead()).placeholder(R.mipmap.ic_launcher).into(holder.mHeadImg);
        if ("".equals(mLists.get(position).getUserName())) {
            //如果没有昵称，设置账号
            holder.mUserText.setText(mLists.get(position).getUserAccount());
        } else {
            holder.mUserText.setText(mLists.get(position).getUserName());
        }
        holder.mTimeText.setText(mLists.get(position).getFtime());
        holder.mContentText.setText(mLists.get(position).getFcontent());
        holder.mPlText.setText(mLists.get(position).getFping_num());
        holder.mDzText.setText(mLists.get(position).getFzan_num());

        if (mLists.get(position).getForumImgs().size() != 0) {
            holder.mRecycler.setVisibility(View.VISIBLE);
            mImgs = new ArrayList<>();
            for (int i = 0; i < mLists.get(position).getForumImgs().size(); i++) {
                mImgs.add(mLists.get(position).getForumImgs().get(i).getFimgUrl());
            }

            GridLayoutManager manager = new GridLayoutManager(mContext, 3);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            holder.mRecycler.setLayoutManager(manager);
            mAdapter = new Forum_PicAdapter(mImgs, mContext);
            holder.mRecycler.setAdapter(mAdapter);

            mAdapter.setOnPhotoClcikListener(new Forum_PicAdapter.onPhotoClcikListener() {
                @Override
                public void onClick(View v, int ImagePosition) {
//                    Log.d("tag", "position=" +mLists.get(position).getForumImgs().get(ImagePosition).getFimgUrl());
                    Intent intent = new Intent(mContext, PicActivity.class);
                    intent.putExtra("img_url", mLists.get(position).getForumImgs().get(ImagePosition).getFimgUrl());
                    mContext.startActivity(intent);
                }
            });

        } else {
            holder.mRecycler.setVisibility(View.GONE);
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
        @BindView(R.id.forum_recycler_time)
        TextView mTimeText;
        @BindView(R.id.forum_recycler_content)
        TextView mContentText;
        @BindView(R.id.forum_Recycler)
        RecyclerView mRecycler;
        @BindView(R.id.forum_recycler_pl_num)
        TextView mPlText;
        @BindView(R.id.forum_recycler_dz_num)
        TextView mDzText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<Forum_entity.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<Forum_entity.DataBean> mLists) {
        this.mLists = (mLists);
    }
}
