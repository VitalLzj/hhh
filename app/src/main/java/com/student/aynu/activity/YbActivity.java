package com.student.aynu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import com.student.aynu.adapter.NewsAdapter;
import com.student.aynu.adapter.YbAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Home_News;
import com.student.aynu.entity.Yb_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.nohttp.JavaBeanRequest;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.student.aynu.webView.NewsWebViewActivity;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/12 0012.
 * 邮箱：976623696@qq.com
 */
public class YbActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.yb_recycler)
    LRecyclerView mLRecyclerView;
    private YbAdapter mAdapter;
    private LRecyclerViewAdapter mLrecyclerAdapter;
    private List<Yb_entity.DataBean> mLists;

    private int now_page = 2;
    //无数据图片
    @BindView(R.id.yb_error_img)
    ImageView mErrorImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_yb);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    /**
     * 获取数据
     */
    private void initData() {
        mLists = new ArrayList<>();
        getData();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mLRecyclerView.setLayoutManager(manager);
        mAdapter = new YbAdapter(mLists, mContext);
        mLrecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        //设置适配器
        mLRecyclerView.setAdapter(mLrecyclerAdapter);

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
                if (mLists.size() < 10) {
                    RecyclerViewStateUtils.setFooterViewState(YbActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(YbActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);

            }
        });
        //点击事件
        mLrecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("您确定要呼叫" + mLists.get(position).getYb_phone() + "吗？")
                        .setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String phoneNumber = "0372" + mLists.get(position).getYb_phone();
                                //意图：想干什么事
                                Intent intent3 = new Intent();
                                intent3.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent3.setData(Uri.parse("tel:" + phoneNumber));
                                //开启系统拨号器
                                startActivity(intent3);
                            }
                        });
                builder.show();
            }
        });
    }

    private void getData() {
        StringRequest request = new StringRequest(IpUtil.getHelp_Info + "page=" + 1, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    //加载更多的请求
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.getHelp_Info + "page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    //刷新数据
    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.getHelp_Info + "page=" + 1, RequestMethod.GET);
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
                    mLRecyclerView.refreshComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page = 2;
                    break;
                //加载更多
                case 2:
                    mAdapter.loadMore(mLists);
                    mLRecyclerView.refreshComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page++;
                    RecyclerViewStateUtils.setFooterViewState(mLRecyclerView, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    };

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            Yb_entity yb = gson.fromJson(response.get(), Yb_entity.class);
            switch (what) {
                //第一次加载
                case 0:
                    if (yb.getCode() == 0) {
                        mLists = yb.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLRecyclerView.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    if (yb.getCode() == 0) {
                        mLists = yb.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        ToastUtil.showFaliureToast(mContext, yb.getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    if (yb.getCode() == 0) {
                        mLists.addAll(yb.getData());
                        if (yb.getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(YbActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, yb.getMessage());
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

    @OnClick({R.id.yb_toolbar_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yb_toolbar_left:
                finish();
                break;
        }
    }
}
