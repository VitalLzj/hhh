package com.student.aynu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.student.aynu.R;
import com.student.aynu.activity.PicActivity;
import com.student.aynu.adapter.PicAdapter;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.util.PicGridDemins;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzj on 2017/2/7 0007.
 * 邮箱：976623696@qq.com
 */
public class PicFragment extends BaseFragment {

    private Context mContext;

    @BindView(R.id.pic_recycler)
    LRecyclerView mLrecycler;
    private LRecyclerViewAdapter mLrecyclerAdapter;
    private PicAdapter mAdapter;
    private List<String> mImgs;

    private static final String TAG = "PicFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pic, null);
        ButterKnife.bind(this, v);
        this.mContext = getActivity();
        initData();
        return v;
    }

    private void initData() {

        mImgs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mImgs.add("http://img1.3lian.com/2015/a1/113/d/10.jpg");
        }

        //垂直方向
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mLrecycler.setLayoutManager(manager);
        //设置布局之间间距
        mLrecycler.addItemDecoration(new PicGridDemins(10));
        mAdapter = new PicAdapter(mImgs, mContext);
        mLrecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        mLrecycler.setAdapter(mLrecyclerAdapter);

        mAdapter.setOnPhotoClcikListener(new PicAdapter.onPhotoClcikListener() {
            @Override
            public void onClick(View v, int position) {

                Log.d(TAG, "onClick");
                startActivity(new Intent(mContext, PicActivity.class));

            }
        });

    }

}
