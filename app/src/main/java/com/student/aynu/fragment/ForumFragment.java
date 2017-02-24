package com.student.aynu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.student.aynu.R;
import com.student.aynu.activity.ForumActivity;
import com.student.aynu.activity.PublishActivity;
import com.student.aynu.adapter.ForumAdapter;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Forum_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 论坛
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class ForumFragment extends BaseFragment {

    private Context mContext;
    @BindView(R.id.forum_LRecycler)
    LRecyclerView mLRecycler;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private ForumAdapter mAdapter;
    private List<Forum_entity.DataBean> mLists;

    private int now_page = 2;
    //无数据图片
    @BindView(R.id.forum_error_img)
    ImageView mErrorImg;

    private static final String TAG = "ForumFragment";

    //发布帖子
    private static final int REQUEST_CODE = 1;
    //进入帖子详情
    private static final int REQUEST_CODE_DETAIL = 2;
    //进入的帖子所在的位置
    private int mPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_forum, null);
        ButterKnife.bind(this, v);
        initData();
        initView();
        return v;
    }

    private void initView() {
        //垂直方向
        mLRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ForumAdapter(mLists, mContext);
        mLRecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecycler.setAdapter(mLRecyclerAdapter);

        //刷新
        mLRecycler.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mLRecycler, LoadingFooter.State.Normal);
                //网络请求数据
                requestData();
            }
        });
        //加载更多
        mLRecycler.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mLRecycler);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (mLists.size() < 10) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecycler, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecycler, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);
            }
        });
        //点击事件
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                Intent intent = new Intent(mContext, ForumActivity.class);
                intent.putExtra("fid", mLists.get(position).getFid());
                startActivityForResult(intent, REQUEST_CODE_DETAIL);
            }
        });

    }

    private void initData() {
        mLists = new ArrayList<>();
        getForumList();
    }

    /**
     * 获取论坛列表
     */
    private void getForumList() {
        StringRequest request = new StringRequest(IpUtil.getForum_List + "page=" + 1, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    //加载更多的请求
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.getForum_List + "page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    //刷新数据
    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.getForum_List + "page=" + 1, RequestMethod.GET);
        request(1, request, callback, false, false);
    }

    //定义handler，显示图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //刷新----第一次进入
                case 0:
                    mAdapter.refresh(mLists);
                    mLRecycler.refreshComplete();
                    mLRecyclerAdapter.notifyDataSetChanged();
                    now_page = 2;
                    break;
                //加载更多
                case 2:
                    mAdapter.loadMore(mLists);
                    mLRecycler.refreshComplete();
                    mLRecyclerAdapter.notifyDataSetChanged();
                    now_page++;
                    RecyclerViewStateUtils.setFooterViewState(mLRecycler, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    };

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            Forum_entity forum = gson.fromJson(response.get(), Forum_entity.class);
            switch (what) {
                //第一次加载
                case 0:
                    if (forum.getCode() == 0) {
                        mLists = forum.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLRecycler.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    if (forum.getCode() == 0) {
                        mLists = forum.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        ToastUtil.showFaliureToast(mContext, forum.getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    if (forum.getCode() == 0) {
                        Log.d(TAG, "0");
                        mLists.addAll(forum.getData());
                        if (forum.getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecycler, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, forum.getMessage());
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mLRecycler.setEmptyView(mErrorImg);
        }
    };

    @OnClick(R.id.forum_toolbar_right)
    public void onClick() {
        startActivityForResult(new Intent(mContext, PublishActivity.class), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == -1) {
                initData();
            }
        } else if (requestCode == REQUEST_CODE_DETAIL) {
            if (resultCode == -1 && data != null) {
                //接受传来的点赞数量与评论数量
                String mZan = data.getStringExtra("zan");
                String mPl = data.getStringExtra("pl");
                //更新点赞与评论数量
                mLists.get(mPosition).setFzan_num(mZan);
                mLists.get(mPosition).setFping_num(mPl);
                mLRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}
