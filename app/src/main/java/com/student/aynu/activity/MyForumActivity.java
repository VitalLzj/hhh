package com.student.aynu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.student.aynu.R;
import com.student.aynu.adapter.MyForumAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.bean.Base_entity;
import com.student.aynu.bean.My_Forum;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/2/27 0027.
 * 邮箱：976623696@qq.com
 * 我的帖子
 */
public class MyForumActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.myForum_recycler)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.myForum_no_data)
    ImageView mErrorImg;

    private MyForumAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private List<My_Forum.DataBean> mLists;

    //加载更多时 当前的页码now_page
    private int now_page = 2;
    private PreviewHandler mHandler = new PreviewHandler(this);

    private int mClickPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_myforum);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mLists = new ArrayList<>();
        getData();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mLRecyclerView.setLayoutManager(manager);
        mAdapter = new MyForumAdapter(mLists, mContext);
        mLRecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        //设置适配器
        mLRecyclerView.setAdapter(mLRecyclerAdapter);

        //刷新
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mLRecyclerView, LoadingFooter.State.Normal);
                //网络请求数据
                requestData();
            }
        });
        //加载更多
        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mLRecyclerView);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(MyForumActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);

            }
        });
        //点击跳转到帖子详情
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ForumActivity.class);
                intent.putExtra("fid", mLists.get(position).getFid());
                startActivity(intent);
            }
        });
        //删除事件
        mAdapter.setOnDeleteListener(new MyForumAdapter.onDeleteListener() {
            @Override
            public void onDelete(View v, int position) {
                //position从1开始
                mClickPosition = position - 1;
                showDeleteDialog();
            }
        });
    }

    /**
     * 删除提示
     */
    private void showDeleteDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("您确定要删除么")
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doDelete();
                    }
                });
        builder.show();
    }

    /**
     * 删除帖子 传入帖子id
     */
    private void doDelete() {
        StringRequest request = new StringRequest(IpUtil.delete_User_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("fid", mLists.get(mClickPosition).getFid());
        request(3, request, callback, false, true);
    }

    /**
     * 获取我的帖子数据
     */
    private void getData() {
        StringRequest request = new StringRequest(IpUtil.get_User_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("page", "1");
        request(0, request, callback, false, true);
    }


    /**
     * @param now_page 当前页
     *                 加载更多的请求
     */
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.get_User_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("page", now_page + "");
        request(2, request, callback, false, false);
    }

    /**
     * 刷新数据
     */

    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.get_User_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("page", "1");
        request(1, request, callback, false, false);
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<MyForumActivity> ref;

        PreviewHandler(MyForumActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyForumActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                //刷新----第一次进入
                case 0:
                    activity.mAdapter.refresh(activity.mLists);
                    activity.mLRecyclerView.refreshComplete();
                    activity.mLRecyclerAdapter.notifyDataSetChanged();
                    activity.now_page = 2;
                    break;
                //加载更多
                case 2:
                    activity.mAdapter.loadMore(activity.mLists);
                    activity.mLRecyclerView.refreshComplete();
                    activity.mLRecyclerAdapter.notifyDataSetChanged();
                    activity.now_page++;
                    RecyclerViewStateUtils.setFooterViewState(activity.mLRecyclerView, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();

            switch (what) {
                //第一次加载
                case 0:
                    My_Forum forum = gson.fromJson(responseInfo, My_Forum.class);
                    if (forum.getCode() == 0) {
                        mLists = forum.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLRecyclerView.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    My_Forum forum1 = gson.fromJson(responseInfo, My_Forum.class);
                    if (forum1.getCode() == 0) {
                        mLists = forum1.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        ToastUtil.showFaliureToast(mContext, forum1.getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    My_Forum forum2 = gson.fromJson(responseInfo, My_Forum.class);
                    if (forum2.getCode() == 0) {
                        mLists.addAll(forum2.getData());
                        if (forum2.getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(MyForumActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, forum2.getMessage());
                    }
                    break;
                case 3:
                    Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
                    if (base.getCode() == 0) {
                        //删除成功
                        ToastUtil.showText(mContext, base.getMessage());
                        mLists.remove(mClickPosition);
                        mLRecyclerAdapter.notifyDataSetChanged();
                    } else if (base.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, base.getMessage());
                    } else {
                        //登录过期了，重新登录
                        ToastUtil.showFaliureToast(mContext, "请重新登录");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mLRecyclerView.setEmptyView(mErrorImg);
        }
    };


    @OnClick(R.id.myForum_toolbar_left)
    public void onClick() {
        finish();
    }

}
