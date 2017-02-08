package com.student.aynu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.student.aynu.R;
import com.student.aynu.activity.PicActivity;
import com.student.aynu.adapter.PicAdapter;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Style_Img;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.PicGridDemins;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

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
    private List<Style_Img.DataBean> mImgs;

    private int now_page = 2;
    //无数据图片
    @BindView(R.id.pic_error_img)
    ImageView mErrorImg;

    private static final String TAG = "PicFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pic, null);
        ButterKnife.bind(this, v);
        this.mContext = getActivity();
        initData();
        initView();
        return v;
    }

    private void initView() {
        //垂直方向
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mLrecycler.setLayoutManager(manager);
        //设置布局之间间距
        mLrecycler.addItemDecoration(new PicGridDemins(10));
        mAdapter = new PicAdapter(mImgs, mContext);
        mLrecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        mLrecycler.setAdapter(mLrecyclerAdapter);

        //刷新
        mLrecycler.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mLrecycler, LoadingFooter.State.Normal);
                //网络请求数据
                requestData();
            }
        });
        //加载更多
        mLrecycler.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mLrecycler);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                if (mImgs.size() < 10) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mLrecycler, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mLrecycler, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);
            }
        });

        mAdapter.setOnPhotoClcikListener(new PicAdapter.onPhotoClcikListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(mContext, PicActivity.class);
                intent.putExtra("img_url", mImgs.get(position - 1).getStyle_img_url());
                startActivity(intent);
            }
        });


    }

    private void initData() {
        mImgs = new ArrayList<>();
        getStyleImg();
    }

    /**
     * 获取图片
     */
    private void getStyleImg() {
        StringRequest request = new StringRequest(IpUtil.getStyle_Img + "page=" + 1, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    //加载更多的请求
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.getStyle_Img + "page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    //刷新数据
    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.getStyle_Img + "page=" + 1, RequestMethod.GET);
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
                    mAdapter.refresh(mImgs);
                    mLrecycler.refreshComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page = 2;
                    break;
                //加载更多
                case 2:
                    mAdapter.loadMore(mImgs);
                    mLrecycler.refreshComplete();
                    mLrecyclerAdapter.notifyDataSetChanged();
                    now_page++;
                    RecyclerViewStateUtils.setFooterViewState(mLrecycler, LoadingFooter.State.Normal);
                    break;
                default:
                    break;
            }
        }
    };

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            Style_Img img = gson.fromJson(response.get(), Style_Img.class);
            switch (what) {
                //第一次加载
                case 0:
                    if (img.getCode() == 0) {
                        mImgs = img.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mLrecycler.setEmptyView(mErrorImg);
                    }
                    break;
                //刷新
                case 1:
                    if (img.getCode() == 0) {
                        mImgs = img.getData();
                        //请求成功，更新ui
                        mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    } else {
                        ToastUtil.showFaliureToast(mContext, img.getMessage());
                    }
                    break;
                //加载更多
                case 2:
                    if (img.getCode() == 0) {
                        mImgs.addAll(img.getData());
                        if (img.getData().size() > 0) {
                            //加载成功
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(getActivity(), mLrecycler, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, img.getMessage());
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mLrecycler.setEmptyView(mErrorImg);
        }
    };
}
