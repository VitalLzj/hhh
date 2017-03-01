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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.student.aynu.entity.Book_Banner;
import com.student.aynu.entity.Book_List;
import com.student.aynu.header.BookActivity_header;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.BookGridDemins;
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
 * Created by lzj on 2017/2/22 0022.
 * 邮箱：976623696@qq.com
 * 帖子详情
 */
public class BookActivity extends BaseActivity {

    @BindView(R.id.book_recycler)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.book_error_img)
    ImageView mErrorImg;
    private BookActivity_header mHeaderView;
    private List<Book_Banner.DataBean> mHeaderData;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private List<Book_List.DataBean> mLists;
    private BookAdapter mAdapter;
    private Context mContext;

    //加载更多时 当前的页码now_page
    private int now_page = 2;
    private PreviewHandler mHandler = new PreviewHandler(this);
    @BindView(R.id.book_search_relative)
    RelativeLayout mSearchLayout;
    @BindView(R.id.book_search_relative2)
    RelativeLayout mSearchLayout2;

    private static final String TAG = "BookActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {

        //设置manager
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mLRecyclerView.setLayoutManager(manager);
        //设置适配器
        mAdapter = new BookAdapter(mLists, mContext);
        mLRecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.addItemDecoration(new BookGridDemins(10));
        mLRecyclerView.setAdapter(mLRecyclerAdapter);

        //添加头部布局
        mHeaderView = new BookActivity_header(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(params);

        //刷新
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mLRecyclerView, LoadingFooter.State.Normal);
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
                    RecyclerViewStateUtils.setFooterViewState(BookActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(BookActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);
            }
        });
        //点击事件
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bid", mLists.get(position).getBid());
                startActivity(intent);
            }
        });


        //滑动事件
        mLRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
//                Log.d(TAG, "onScrolled: dx" + distanceX);
//                Log.d(TAG, "onScrolled: dy" + distanceY);
                int imageHeight = mHeaderView.getHeight();
                if (distanceY <= 0) {
                    mSearchLayout.getBackground().mutate().setAlpha(0);
                    mSearchLayout2.getBackground().mutate().setAlpha(0);
                } else {
                    if (distanceY < imageHeight) {
                        int progress = (int) ((float) distanceY / (float) imageHeight * 255);
                        mSearchLayout.getBackground().mutate().setAlpha(progress);
                        mSearchLayout2.getBackground().mutate().setAlpha(progress);
                    } else {
                        //“图片”全部滑出屏幕的时候，设为完全不透明
                        mSearchLayout.getBackground().mutate().setAlpha(255);
                        mSearchLayout2.getBackground().mutate().setAlpha(255);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        mLists = new ArrayList<>();
        mHeaderData = new ArrayList<>();
        getBookHeaderData();
        loadDate();
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<BookActivity> ref;

        PreviewHandler(BookActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BookActivity activity = ref.get();
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
                case 3:
                    activity.mHeaderView.setData(activity.mHeaderData);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取评论列表
     */
    private void loadDate() {
        StringRequest request = new StringRequest(IpUtil.get_Book_List + "&page=1", RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    /**
     * 刷新评论列表
     */
    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.get_Book_List + "&page=1", RequestMethod.GET);
        request(1, request, callback, false, false);
    }

    /**
     * @param now_page 当前页
     *                 加载更多评论
     */
    //加载更多的请求
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.get_Book_List + "&page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    private void getBookHeaderData() {
        StringRequest request = new StringRequest(IpUtil.get_Book_Banner, RequestMethod.GET);
        request(3, request, callback, false, false);
    }


    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Book_List books = null;
            if (what != 3) {
                books = gson.fromJson(responseInfo, Book_List.class);
            }
            switch (what) {
                case 0:
                    mLists = books.getData();
                    mHandler.sendEmptyMessage(0);
                    break;
                case 1:
                    mLists = books.getData();
                    //请求成功，更新ui
                    mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    break;
                case 2:
                    if (books.getCode() == 0) {
                        mLists.addAll(books.getData());
                        if (books.getData().size() > 0) {
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(BookActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, books.getMessage());
                    }
                    break;
                case 3:
                    mHeaderData = gson.fromJson(responseInfo, Book_Banner.class).getData();
                    mHandler.sendEmptyMessage(3);
                    mLRecyclerAdapter.addHeaderView(mHeaderView);
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mLRecyclerView.setEmptyView(mErrorImg);
        }
    };

    @OnClick({R.id.book_toolbar_left, R.id.book_search_relative2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_toolbar_left:
                finish();
                break;
            case R.id.book_search_relative2:
                startActivity(new Intent(mContext, BookSearchActivity.class));
                break;
        }
    }
}
