package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.adapter.BookAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Book_List;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.BookSearchGridDemins;
import com.student.aynu.util.IpUtil;
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
 * Created by lzj on 2017/3/1 0001.
 * 邮箱：976623696@qq.com
 * 图书搜索
 */
public class BookSearchActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.search_edit)
    EditText mInputEdit;
    @BindView(R.id.search_toolbar_right)
    TextView mSearchText;
    @BindView(R.id.search_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_no_data)
    ImageView mErrorImg;

    private List<Book_List.DataBean> mLists;
    private BookAdapter mAdapter;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private static final String TAG = "BookSearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        //设置manager
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new BookAdapter(mLists, mContext);
        mRecyclerView.addItemDecoration(new BookSearchGridDemins(10));
        mRecyclerView.setAdapter(mAdapter);

        mInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!"".equals(mInputEdit.getText().toString().trim())) {
                    mSearchText.setText("搜索");
                } else {
                    mSearchText.setText("取消");
                }
            }
        });
    }

    private void initData() {
        mLists = new ArrayList<>();
        getData();
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<BookSearchActivity> ref;

        PreviewHandler(BookSearchActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BookSearchActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case 0:
                    Log.d(TAG, "handleMessage: " + activity.mLists.size());
                    activity.mAdapter.onDataChanged(activity.mLists);
                    //设置适配器
                    activity.mAdapter.setOnChildClickListener(new BookAdapter.onChildClickListener() {
                        @Override
                        public void onJump(View v, int position) {
                            Log.d(TAG, "onJump: " + position);
                            Intent intent = new Intent(activity.mContext, BookDetailActivity.class);
                            intent.putExtra("bid", activity.mLists.get(position).getBid());
                            activity.mContext.startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    activity.mRecyclerView.setVisibility(View.GONE);
                    activity.mErrorImg.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(IpUtil.get_Tj_Books, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Book_List books = gson.fromJson(responseInfo, Book_List.class);
            if (books.getCode() == 0) {
                mLists = books.getData();
                mHandler.sendEmptyMessage(0);
            } else {
                mHandler.sendEmptyMessage(1);
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mErrorImg.setImageResource(R.mipmap.wuwang);
            mRecyclerView.setVisibility(View.GONE);
            mErrorImg.setVisibility(View.VISIBLE);
        }
    };

    @OnClick(R.id.search_toolbar_right)
    public void onClick() {
        if ("取消".equals(mSearchText.getText().toString())) {
            finish();
        } else {
            //搜索
            Intent intent = new Intent(mContext, BookSearchResultActivity.class);
            intent.putExtra("words", mInputEdit.getText().toString());
            startActivity(intent);
        }
    }

}
