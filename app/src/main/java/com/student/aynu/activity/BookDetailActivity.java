package com.student.aynu.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Book_Detail;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ShareUtils;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/2/28 0028.
 * 邮箱：976623696@qq.com
 * 图书详情
 */
public class BookDetailActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.book_detail_scroll)
    ScrollView mScrollView;
    @BindView(R.id.book_detail_img)
    ImageView mBookImg;
    @BindView(R.id.book_detail_name)
    TextView mBookTitle;
    @BindView(R.id.book_detail_layout_fx)
    RelativeLayout mBookFx;
    @BindView(R.id.book_detail_text)
    TextView mBookIntroduce;

    @BindView(R.id.book_zrz)
    TextView mBookAuthor;
    @BindView(R.id.book_cbs)
    TextView mBookCbs;
    @BindView(R.id.book_cby)
    TextView mBookCby;
    @BindView(R.id.book_ssh)
    TextView mBookSsh;
    @BindView(R.id.book_gcs)
    TextView mBookGcs;

    @BindView(R.id.book_detail_no_data)
    ImageView mErrorImg;

    //收藏
    @BindView(R.id.book_detail_sc_layout)
    RelativeLayout mScLayout;
    @BindView(R.id.book_detail_sc_img)
    ImageView mScImg;

    private Book_Detail.DataBean mBookDetails;
    private String bid;
    private PreviewHandler mHandler = new PreviewHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        bid = getIntent().getStringExtra("bid");
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {

    }

    private void initData() {
        getBookDetail();
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<BookDetailActivity> ref;

        PreviewHandler(BookDetailActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BookDetailActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case 0:

                    List<Book_Detail.DataBean.BAuthorBean> mAuthors = activity.mBookDetails.getBAuthor();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mAuthors.size(); i++) {
                        sb.append(mAuthors.get(i).getBaathor()).append(" ");
                    }

                    Glide.with(activity.mContext).load(activity.mBookDetails.getBthumb()).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(activity.mBookImg);
                    activity.mBookTitle.setText(activity.mBookDetails.getBname());
                    activity.mBookAuthor.setText(sb.toString());
                    activity.mBookIntroduce.setText(activity.mBookDetails.getBintroduce());
                    activity.mBookCbs.setText(activity.mBookDetails.getBcbs());
                    activity.mBookCby.setText(activity.mBookDetails.getByear());
                    activity.mBookSsh.setText(activity.mBookDetails.getBplace());
                    activity.mBookGcs.setText(activity.mBookDetails.getBtotal() + "   可借数  " + activity.mBookDetails.getNnow());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 获取图书详情
     */
    private void getBookDetail() {
        StringRequest request = new StringRequest(IpUtil.get_Book_Detail + "bid=" + bid, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Book_Detail mBookData = gson.fromJson(responseInfo, Book_Detail.class);
            if (mBookData.getCode() == 0) {
                //获取成功
                mBookDetails = mBookData.getData().get(0);
                mHandler.sendEmptyMessage(0);
            } else {
                ToastUtil.showFaliureToast(mContext, "这不可能发生");
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            mScrollView.setVisibility(View.GONE);
            mErrorImg.setVisibility(View.VISIBLE);
        }
    };

    @OnClick({R.id.book_detail_toolbar_left, R.id.book_detail_layout_fx})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_detail_toolbar_left:
                finish();
                break;
            case R.id.book_detail_layout_fx:
                ShareUtils.share(mContext, mBookDetails.getBname());
                break;
        }
    }


}
