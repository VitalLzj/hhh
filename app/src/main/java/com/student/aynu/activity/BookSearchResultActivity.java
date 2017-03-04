package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.student.aynu.adapter.BookAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.bean.Book_List;
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
 * Created by lzj on 2017/2/7 0007.
 * 图书搜索结果
 * 邮箱：976623696@qq.com
 */
public class BookSearchResultActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.sc_result_recycler)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.sc_result_no_data)
    ImageView mErrorImg;

    private BookAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private List<Book_List.DataBean> mLists;

    //加载更多时 当前的页码now_page
    private int now_page = 2;
    private PreviewHandler mHandler = new PreviewHandler(this);

    private String words = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        words = getIntent().getStringExtra("words");
        setContentView(R.layout.activity_book_sc_result);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mLists = new ArrayList<>();
        getData();
    }

    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mLRecyclerView.setLayoutManager(manager);

        mAdapter = new BookAdapter(mLists, mContext);
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
                RecyclerViewStateUtils.setFooterViewState(BookSearchResultActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);

            }
        });
        //点击跳转到图书详情
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bid", mLists.get(position).getBid());
                startActivity(intent);
            }
        });

    }


    /**
     * 获取搜索结果
     */
    private void getData() {
        StringRequest request = new StringRequest(IpUtil.search_Books, RequestMethod.POST);
        request.set("words", words);
        request.set("page", "1");
        request(0, request, callback, false, true);
    }

    /**
     * @param now_page 当前页
     *                 加载更多的请求
     */
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.search_Books, RequestMethod.POST);
        request.set("words", words);
        request.set("page", now_page + "");
        request(2, request, callback, false, false);
    }

    /**
     * 刷新数据
     */

    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.search_Books, RequestMethod.POST);
        request.set("words", words);
        request.set("page", "1");
        request(1, request, callback, false, false);
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<BookSearchResultActivity> ref;

        PreviewHandler(BookSearchResultActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BookSearchResultActivity activity = ref.get();
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
            Book_List sc = gson.fromJson(responseInfo, Book_List.class);
            switch (what) {
                //第一次加载
                case 0:
                    if (sc.getCode() == 0) {
                        mLists = sc.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLRecyclerView.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    if (sc.getCode() == 0) {
                        mLists = sc.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        ToastUtil.showFaliureToast(mContext, sc.getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    if (sc.getCode() == 0) {
                        mLists.addAll(sc.getData());
                        if (sc.getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(BookSearchResultActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, sc.getMessage());
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


    @OnClick(R.id.sc_result_toolbar_left)
    public void onClick() {
        finish();
    }


}
