package com.student.aynu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.student.aynu.base.BaseViewpagerFragment;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Home_News;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.nohttp.JavaBeanRequest;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Toast;
import com.student.aynu.webView.NewsWebViewActivity;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * title1
 * Created by lzj on 2016/12/23 0023.
 * 邮箱：976623696@qq.com
 */
public class HomeTitle7Fragment extends BaseViewpagerFragment {

    private LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLrecyclerAdapter;
    private NewsAdapter mNewsAdapter;
    //数据
    private List<Home_News.DataBean> mLists;
    //加载更多时 当前的页码now_page
    private int now_page = 2;

    ImageView mErrorImg;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_1;
    }

    public static HomeTitle7Fragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("TYPE", type);
        HomeTitle7Fragment fragment = new HomeTitle7Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        mLists = new ArrayList<>();
        mLRecyclerView = findView(R.id.home_recycler1);
        mErrorImg = findView(R.id.error_img);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mLRecyclerView.setLayoutManager(manager);
        mNewsAdapter = new NewsAdapter(mLists, getActivity());
        mLrecyclerAdapter = new LRecyclerViewAdapter(mNewsAdapter);
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
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);
            }
        });
        //点击事件
        mLrecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, NewsWebViewActivity.class);
                intent.putExtra("NEWS_TYPE", mNews_Type);
                intent.putExtra("NEWS_TITLE", mLists.get(position).getNews_title());
                intent.putExtra("NEWS_TIME", mLists.get(position).getNews_time());
                intent.putExtra("NEWS_URL", mLists.get(position).getNews_url());
                startActivity(intent);
            }
        });
    }

    //加载更多的请求
    private void requestLoadData(int now_page) {
        JavaBeanRequest<Home_News> request = new JavaBeanRequest<>(IpUtil.getHomeNews + "nav=7&page=" + now_page, Home_News.class);
        request(2, request, callback, false, false);
    }

    //刷新数据
    private void requestData() {
        JavaBeanRequest<Home_News> request = new JavaBeanRequest<>(IpUtil.getHomeNews + "nav=7&page=" + 1, Home_News.class);
        request(1, request, callback, false, false);
    }

    @Override
    protected void initData() {
        //获取数据
        JavaBeanRequest<Home_News> request = new JavaBeanRequest<>(IpUtil.getHomeNews + "nav=7&page=" + 1, Home_News.class);
        request(0, request, callback, false, true);
    }

    //定义handler，显示图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //刷新----第一次进入
                case 0:
                    mNewsAdapter.refresh(mLists);
                    mLRecyclerView.refreshComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page = 2;
                    break;
                //加载更多
                case 2:
                    mNewsAdapter.loadMore(mLists);
                    mLRecyclerView.refreshComplete();
//                    mAdapter.loadComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page++;
                    RecyclerViewStateUtils.setFooterViewState(mLRecyclerView, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    };

    HttpListener<Home_News> callback = new HttpListener<Home_News>() {
        @Override
        public void onSucceed(int what, Response<Home_News> response) {
            switch (what) {
                //第一次加载
                case 0:
                    if (response.get().getCode() == 0) {
                        mLists = response.get().getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLRecyclerView.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    if (response.get().getCode() == 0) {
                        mLists = response.get().getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        Toast.showFaliureToast(getActivity(), response.get().getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    if (response.get().getCode() == 0) {
                        mLists.addAll(response.get().getData());
                        if (response.get().getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(getActivity(), mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        Toast.showFaliureToast(getActivity(), response.get().getMessage());
                    }
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<Home_News> response) {
            Log.d("tag", "网络不好");
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mLRecyclerView.setEmptyView(mErrorImg);
        }
    };
}
