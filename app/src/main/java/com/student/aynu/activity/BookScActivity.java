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
import com.student.aynu.adapter.BookScAdapter;
import com.student.aynu.adapter.MyForumAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.entity.Book_Sc;
import com.student.aynu.entity.My_Forum;
import com.student.aynu.fragment.InfoFragment;
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
 * 图书收藏
 * 邮箱：976623696@qq.com
 */
public class BookScActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.sc_recycler)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.sc_no_data)
    ImageView mErrorImg;

    private BookScAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private List<Book_Sc.DataBean> mLists;

    //加载更多时 当前的页码now_page
    private int now_page = 2;
    private PreviewHandler mHandler = new PreviewHandler(this);

    private static final String TAG = "BookScActivity";
    private int mClickPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_book_sc);
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
        mAdapter = new BookScAdapter(mLists, mContext);
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
                RecyclerViewStateUtils.setFooterViewState(BookScActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);

            }
        });
        //点击跳转到帖子详情
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bid", mLists.get(position).getBid());
                startActivity(intent);
            }
        });
        //删除事件
        mAdapter.setOnDeleteListener(new BookScAdapter.onDeleteListener() {
            @Override
            public void onQuite(View v, int position) {
                Log.d(TAG, "onQuite: " + position);
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
        builder.setTitle("您确定要取消收藏么")
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
        StringRequest request = new StringRequest(IpUtil.do_Quite_Sc + "token=" + mApplication.uname_token +
                "&bid=" + mLists.get(mClickPosition).getBid(), RequestMethod.GET);
        request(3, request, callback, false, true);
    }

    /**
     * 获取我的帖子数据
     */
    private void getData() {
        StringRequest request = new StringRequest(IpUtil.get_Sc_List + "token=" + mApplication.uname_token +
                "&page=1", RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    /**
     * @param now_page 当前页
     *                 加载更多的请求
     */
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.get_Sc_List + "token=" + mApplication.uname_token +
                "&page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    /**
     * 刷新数据
     */

    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.get_Sc_List + "token=" + mApplication.uname_token +
                "&page=1", RequestMethod.GET);
        request(1, request, callback, false, false);
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<BookScActivity> ref;

        PreviewHandler(BookScActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BookScActivity activity = ref.get();
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
            Book_Sc sc = null;
            if (what != 3) {
                sc = gson.fromJson(responseInfo, Book_Sc.class);
            }
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
                        RecyclerViewStateUtils.setFooterViewState(BookScActivity.this, mLRecyclerView, Constant.PAGE_SIZE_10, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, sc.getMessage());
                    }
                    break;
                case 3:
                    Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
                    if (base.getCode() == 0) {
                        //取消成功
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


    @OnClick(R.id.sc_toolbar_left)
    public void onClick() {
        finish();
    }


}
