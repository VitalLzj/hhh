package com.student.aynu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.student.aynu.R;
import com.student.aynu.adapter.Info_LeftAdapter;
import com.student.aynu.adapter.Info_RightAdapter;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.bean.Info_Data;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.student.aynu.webView.InfoWebViewActivity;
import com.student.aynu.webView.NewsWebViewActivity;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 资讯
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class InfoFragment extends BaseFragment {

    //左侧的相关数据
    @BindView(R.id.info_left)
    ListView mLeftList;
    private Info_LeftAdapter mLeftAdapter;
    private List<Info_Data.DataBean> mLeftLists;

    //右侧的相关数据
    @BindView(R.id.info_right)
    RecyclerView mRightRecycler;
    private Info_RightAdapter mRightAdapter;

    //无数据图片
    @BindView(R.id.info_no_data)
    ImageView mNoDataImg;
    //进入主页
    @BindView(R.id.info_go)
    RelativeLayout mGoLayout;
    //左侧title的Position
    private int mPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.bind(this, v);
        initData();
        return v;
    }

    private void initData() {
        mLeftLists = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRightRecycler.setLayoutManager(manager);
        //获取院系信息
        StringRequest request = new StringRequest(IpUtil.getInfoData, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            //解析结果
            if (gson.fromJson(response.get(), Info_Data.class).getCode() == 0) {
                mLeftLists = gson.fromJson(response.get(), Info_Data.class).getData();
                mLeftAdapter = new Info_LeftAdapter(getActivity(), mLeftLists);
                mLeftList.setAdapter(mLeftAdapter);
                //判断是否有数据，如果没有，显示无数据
                if (mLeftLists.get(0).getNews().size() == 0) {
                    mRightRecycler.setVisibility(View.GONE);
                    mNoDataImg.setVisibility(View.VISIBLE);
                } else {
                    mRightRecycler.setVisibility(View.VISIBLE);
                    mNoDataImg.setVisibility(View.GONE);
                    mRightAdapter = new Info_RightAdapter(getActivity(), mLeftLists.get(0).getNews());
                    mRightRecycler.setAdapter(mRightAdapter);
                    mRightAdapter.setOnInfoItemClickListener(new Info_RightAdapter.onItemClick() {
                        @Override
                        public void onClick(View v, int position) {
                            Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                            intent.putExtra("NEWS_TYPE", mLeftLists.get(0).getYx_name());
                            intent.putExtra("NEWS_TITLE", mLeftLists.get(0).getNews().get(position).getNews_title());
                            intent.putExtra("NEWS_TIME", mLeftLists.get(0).getNews().get(position).getNews_time());
                            intent.putExtra("NEWS_URL", mLeftLists.get(0).getNews().get(position).getNews_url());
                            startActivity(intent);
                        }
                    });
                }
            } else {
                ToastUtil.showFaliureToast(getActivity(), gson.fromJson(response.get(), Info_Data.class).getMessage());
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    @OnItemClick(R.id.info_left)
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        //i从0开始
        mPosition = i;
        mLeftAdapter.setmCurPosition(i);
        mLeftAdapter.notifyDataSetChanged();
        mLeftList.smoothScrollToPositionFromTop(i, (adapterView.getHeight() - view.getHeight()) / 2);
        //点击时右侧listview内容变化
        //判断是否有数据
        if (mLeftLists.get(i).getNews().size() == 0) {
            mRightRecycler.setVisibility(View.GONE);
            mNoDataImg.setVisibility(View.VISIBLE);
        } else {
            mRightRecycler.setVisibility(View.VISIBLE);
            mNoDataImg.setVisibility(View.GONE);
            mRightAdapter = new Info_RightAdapter(getActivity(), mLeftLists.get(i).getNews());
            mRightRecycler.setAdapter(mRightAdapter);
            mRightAdapter.setOnInfoItemClickListener(new Info_RightAdapter.onItemClick() {
                @Override
                public void onClick(View v, int position) {
                    Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                    intent.putExtra("NEWS_TYPE", mLeftLists.get(i).getYx_name());
                    intent.putExtra("NEWS_TITLE", mLeftLists.get(i).getNews().get(position).getNews_title());
                    intent.putExtra("NEWS_TIME", mLeftLists.get(i).getNews().get(position).getNews_time());
                    intent.putExtra("NEWS_URL", mLeftLists.get(i).getNews().get(position).getNews_url());
                    startActivity(intent);
                }
            });
        }
    }

    //进入主页点击事件
    @OnClick(R.id.info_go)
    public void onGoClick(View v) {
        Intent intent = new Intent(getActivity(), InfoWebViewActivity.class);
        intent.putExtra("NEWS_URL", mLeftLists.get(mPosition).getYx_url());
        intent.putExtra("NEWS_TYPE", mLeftLists.get(mPosition).getYx_name());
        startActivity(intent);
    }


}
