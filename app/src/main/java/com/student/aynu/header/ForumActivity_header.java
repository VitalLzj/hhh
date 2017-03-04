package com.student.aynu.header;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.student.aynu.R;
import com.student.aynu.activity.PicActivity;
import com.student.aynu.adapter.Forum_PicAdapter;
import com.student.aynu.bean.Forum_Detail_bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情头部
 * Created by Administrator on 2016/9/6 0006.
 */
public class ForumActivity_header extends LinearLayout {

    private ImageView mHeadImg;
    private TextView mUserText;
    private TextView mInfoText;
    private TextView mTimeText;
    private RecyclerView mRecycler;
    private List<String> mImgs;
    private Forum_PicAdapter mAdapter;
    public TextView mZanText;
    public TextView mHuiFuText;
    public RelativeLayout mZanLayout;
    public ImageView mZanImg;

    private Forum_Detail_bean.DataBean mHeadItem;


    public ForumActivity_header(Context context) {
        super(context);
        init(context);
    }

    public ForumActivity_header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ForumActivity_header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = inflate(context, R.layout.activity_forum_header, this);
        mHeadImg = (ImageView) v.findViewById(R.id.forum_header_img);
        mUserText = (TextView) v.findViewById(R.id.forum_header_user);
        mInfoText = (TextView) v.findViewById(R.id.forum_header_content);
        mTimeText = (TextView) v.findViewById(R.id.forum_header_time);
        mZanText = (TextView) v.findViewById(R.id.forum_header_dz_num);
        mHuiFuText = (TextView) v.findViewById(R.id.forum_header_pl_num);
        mZanLayout = (RelativeLayout) v.findViewById(R.id.forum_header_layout);
        mZanImg = (ImageView) v.findViewById(R.id.forum_header_dz);
        mRecycler = (RecyclerView) v.findViewById(R.id.forum_Recycler);
    }

    public void setData(final Forum_Detail_bean.DataBean mHeadItem, final Context context) {
        this.mHeadItem = mHeadItem;
        Glide.with(context).load(mHeadItem.getUserHead()).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(mHeadImg);
        if (mHeadItem.getUserName().equals("")) {
            mUserText.setText(mHeadItem.getUserAccount());
        } else {
            mUserText.setText(mHeadItem.getUserName());
        }
        mInfoText.setText(mHeadItem.getFcontent());
        mTimeText.setText(mHeadItem.getFtime());
        mHuiFuText.setText(mHeadItem.getFping_num());
        mZanText.setText(mHeadItem.getFzan_num());

        if (mHeadItem.getForumImgs().size() != 0) {
            mRecycler.setVisibility(View.VISIBLE);
            mImgs = new ArrayList<>();
            for (int i = 0; i < mHeadItem.getForumImgs().size(); i++) {
                mImgs.add(mHeadItem.getForumImgs().get(i).getFimgUrl());
            }

            GridLayoutManager manager = new GridLayoutManager(context, 3);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            mRecycler.setLayoutManager(manager);
            mAdapter = new Forum_PicAdapter(mImgs, context);
            mRecycler.setAdapter(mAdapter);

            mAdapter.setOnPhotoClcikListener(new Forum_PicAdapter.onPhotoClcikListener() {
                @Override
                public void onClick(View v, int ImagePosition) {
                    Intent intent = new Intent(context, PicActivity.class);
                    intent.putExtra("img_url", mHeadItem.getForumImgs().get(ImagePosition).getFimgUrl());
                    context.startActivity(intent);
                }
            });

        } else {
            mRecycler.setVisibility(View.GONE);
        }


    }

    //设置点赞数量
    public void setZanNum(int number) {
        mZanText.setText(number + "");
    }

    //设置评论数量
    public void setPingNum(int number) {
        mHuiFuText.setText(number + "");
    }

}
