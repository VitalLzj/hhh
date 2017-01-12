package com.student.aynu.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.student.aynu.R;
import com.student.aynu.adapter.YbAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Yb_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
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
    RecyclerView mRecycler;
    private YbAdapter mAdapter;
    private List<Yb_entity> mLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_yb);
        ButterKnife.bind(this);
        getData();
    }

    /**
     * 爬取网络数据
     */
    private void getData() {
        mLists = new ArrayList<>();
        StringRequest request = new StringRequest(IpUtil.aynu_url, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            Log.d("tag", response.get());
        }

        @Override
        public void onFailed(int what, Response<String> response) {

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
