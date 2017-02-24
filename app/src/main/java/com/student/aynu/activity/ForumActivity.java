package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.adapter.Forum_DetailAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.entity.Forum_Detail_bean;
import com.student.aynu.entity.Forum_Pl;
import com.student.aynu.header.ForumActivity_header;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/2/22 0022.
 * 邮箱：976623696@qq.com
 * 帖子详情
 */
public class ForumActivity extends BaseActivity {

    @BindView(R.id.forum_recycler)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.forum_bottom_img)
    RoundedImageView mBottomImg;
    @BindView(R.id.forum_bottom_edit)
    EditText mBottomEdit;
    @BindView(R.id.forum_bottom_fb)
    TextView mBottomText;
    @BindView(R.id.forum_no_data)
    ImageView mErrorImg;
    private ForumActivity_header mHeaderView;
    private Forum_Detail_bean.DataBean mHeaderData;
    private LRecyclerViewAdapter mLRecyclerAdapter;
    private List<Forum_Pl.DataBean> mLists;
    private Forum_DetailAdapter mAdapter;
    private String fid = "";
    private Context mContext;

    private String mUserName, mUserHead;
    //加载更多时 当前的页码now_page
    private int now_page = 2;
    private PreviewHandler mHandler = new PreviewHandler(this);
    //是否回复的楼主
    private boolean isLouzhu = true;
    //如果回复的是评论，获取评论的id,评论人，评论内容,评论时间
    private String mPl_id = "0", mPl_name, mPl_info, mPl_time;
    //评论列表是否到达底部,这两个是为了拼接评论内容
    private boolean isEnd = false;

    //点赞数量与评论数量
    private String mZan_num, mPl_num;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        initData();
        initView();
    }

    /**
     * 当用户没有登录时，登录成功后。
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mUserName = getSharedPreferences("TOKEN", MODE_PRIVATE).getString("user_name", "");
    }

    private void initView() {

        //如果用户已经登录过了，获取用户名与用户头像。
        mUserName = getSharedPreferences("TOKEN", MODE_PRIVATE).getString("user_name", "");
        mUserHead = getSharedPreferences("TOKEN", MODE_PRIVATE).getString("user_face", "");
        //设置头像
        if (!"".equals(mUserHead)) {
            Glide.with(mContext).load(mUserHead).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(mBottomImg);
        }

        //设置manager
        mLRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new Forum_DetailAdapter(mLists, mContext);
        mLRecyclerAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.setAdapter(mLRecyclerAdapter);

        //添加头部布局
        mHeaderView = new ForumActivity_header(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(params);
        //header整体点击事件
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLouzhu = true;
                mPl_id = "0";
                //谈起软键盘
                mBottomEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) mBottomEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                mBottomEdit.setHint("回复楼主");
            }
        });
        //header点赞
        mHeaderView.mZanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doZan();
            }
        });

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
                    RecyclerViewStateUtils.setFooterViewState(ForumActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                    return;
                }
                RecyclerViewStateUtils.setFooterViewState(ForumActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.Loading, null);
                requestLoadData(now_page);
            }
        });
        //recyclerview点击事件
        mLRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                isLouzhu = false;
                mPl_id = mLists.get(i).getFplid();
                //谈起软键盘
                mBottomEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) mBottomEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                mPl_name = mLists.get(i).getUserAccount();
                mPl_info = mLists.get(i).getFplinfo();
                mPl_time = mLists.get(i).getFpltime();
                mBottomEdit.setHint("回复" + mLists.get(i).getLc() + mLists.get(i).getUserAccount());
            }
        });

    }

    private void initData() {
        mLists = new ArrayList<>();
        mHeaderData = new Forum_Detail_bean.DataBean();
        getForumHeaderData();
        loadDate();
        loadIsZan();
    }

    //加载 防止oom的handler
    private static class PreviewHandler extends Handler {
        private WeakReference<ForumActivity> ref;

        PreviewHandler(ForumActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ForumActivity activity = ref.get();
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
                    activity.mHeaderView.setData(activity.mHeaderData, activity.mContext);
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
        StringRequest request = new StringRequest(IpUtil.getForum_Pl_List + "fid=" + fid + "&page=1", RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    /**
     * 刷新评论列表
     */
    private void requestData() {
        StringRequest request = new StringRequest(IpUtil.getForum_Pl_List + "fid=" + fid + "&page=1", RequestMethod.GET);
        request(1, request, callback, false, false);
    }

    /**
     * @param now_page 当前页
     *                 加载更多评论
     */
    //加载更多的请求
    private void requestLoadData(int now_page) {
        StringRequest request = new StringRequest(IpUtil.getForum_Pl_List + "fid=" + fid + "&page=" + now_page, RequestMethod.GET);
        request(2, request, callback, false, false);
    }

    /**
     * 获取帖子详情
     */
    private void getForumHeaderData() {
        StringRequest request = new StringRequest(IpUtil.getForum_Detail + "fid=" + fid, RequestMethod.GET);
        request(3, request, callback, false, false);
    }

    /**
     * 检测用户是否点赞了
     */
    private void loadIsZan() {
        StringRequest request = new StringRequest(IpUtil.check_User_IsZan, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("fid", fid);
        request(5, request, callback, false, false);
    }

    /**
     * 点赞
     */
    private void doZan() {
        StringRequest request = new StringRequest(IpUtil.zan_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("fid", fid);
        request(6, request, callback, false, false);
    }


    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();

            switch (what) {
                case 0:
                    Forum_Pl mPl = gson.fromJson(responseInfo, Forum_Pl.class);
                    mLists = mPl.getData();
                    mHandler.sendEmptyMessage(0);
                    break;
                case 1:
                    Forum_Pl mPl2 = gson.fromJson(responseInfo, Forum_Pl.class);
                    mLists = mPl2.getData();
                    //请求成功，更新ui
                    mHandler.sendEmptyMessageDelayed(0, Constant.DELAY_TIME);
                    break;
                case 2:
                    Forum_Pl mPl3 = gson.fromJson(responseInfo, Forum_Pl.class);
                    if (mPl3.getCode() == 0) {
                        mLists.addAll(mPl3.getData());
                        if (mPl3.getData().size() > 0) {
                            isEnd = false;
                            mHandler.sendEmptyMessageDelayed(2, Constant.DELAY_TIME);
                        }
                    } else {
                        isEnd = true;
                        RecyclerViewStateUtils.setFooterViewState(ForumActivity.this, mLRecyclerView, Constant.PAGE_SIZE, LoadingFooter.State.TheEnd, null);
                        ToastUtil.showFaliureToast(mContext, mPl3.getMessage());
                    }
                    break;
                case 3:
                    mHeaderData = gson.fromJson(responseInfo, Forum_Detail_bean.class).getData().get(0);
                    mZan_num = mHeaderData.getFzan_num();
                    mPl_num = mHeaderData.getFping_num();
                    mHandler.sendEmptyMessage(3);
                    mLRecyclerAdapter.addHeaderView(mHeaderView);
                    break;
                case 4:
                    Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
                    if (base.getCode() == 0) {
                        ToastUtil.showText(mContext, "评论成功");
                        //评论成功了，评论数+1
                        int new_p_num = Integer.parseInt(mPl_num);
                        new_p_num = new_p_num + 1;
                        mHeaderView.mHuiFuText.setText(new_p_num + "");

                        if (isEnd || mLists.size() < 10) {
                            //到达底部 --拼接list,没有加载更多是 ----拼接list
                            Forum_Pl.DataBean mBean = new Forum_Pl.DataBean();

                            int lc = mLists.size() + 2;
                            //判断回复的是谁
                            if (isLouzhu) {
                                mBean.setFplinfo(mBottomEdit.getText().toString());
                                mBean.setUserAccount(mUserName);
                                mBean.setUserHead(mUserHead);
                                mBean.setUserName(mUserName);
                                mBean.setLc(lc + "楼");
                                mBean.setFpltime("刚刚");
                                mBean.setFplyid("0");

                                mBean.setY_userAccount("");
                                mBean.setY_userName("");
                                mBean.setY_fcontent("");
                                mBean.setY_ftime("");

                            } else {
                                mBean.setFplinfo(mBottomEdit.getText().toString());
                                mBean.setUserAccount(mUserName);
                                mBean.setUserHead(mUserHead);
                                mBean.setUserName(mUserName);
                                mBean.setLc(lc + "楼");
                                mBean.setFpltime("刚刚");
                                mBean.setFplyid(mPl_id);

                                mBean.setY_userAccount(mPl_name);
                                mBean.setY_userName(mPl_name);
                                mBean.setY_fcontent(mPl_info);
                                mBean.setY_ftime(mPl_time);


                            }
                            mLists.add(mBean);
                            mAdapter.notifyDataSetChanged();
                        }
                        mBottomEdit.setText("");

                    } else if (base.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, "评论失败");
                    } else {
                        //登录过期了，重新登录
                        ToastUtil.showFaliureToast(mContext, "请重新登录");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case 5:
                    Base_entity base2 = gson.fromJson(responseInfo, Base_entity.class);
                    if (base2.getCode() == 0) {
                        //已点赞
                        mHeaderView.mZanImg.setImageResource(R.mipmap.shequ_dz_hover);
                    } else {
                        mHeaderView.mZanImg.setImageResource(R.mipmap.shequ_dz);
                    }
                    break;
                case 6:
                    Base_entity base3 = gson.fromJson(responseInfo, Base_entity.class);
                    if (base3.getCode() == 0) {
                        //点赞成功

                        //点赞成功了，点赞数+1
                        int new_z_num = Integer.parseInt(mZan_num);
                        new_z_num = new_z_num + 1;
                        mHeaderView.mZanText.setText(new_z_num + "");

                        ToastUtil.show(mContext, base3.getMessage());
                        mHeaderView.mZanImg.setImageResource(R.mipmap.shequ_dz_hover);
                    } else if (base3.getCode() == 1) {
                        //点赞失败
                        ToastUtil.showFaliureToast(mContext, base3.getMessage());
                    } else if (base3.getCode() == 3) {
                        //重复点赞
                        ToastUtil.showFaliureToast(mContext, base3.getMessage());
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

    @OnClick({R.id.forum_toolbar_left, R.id.forum_bottom_fb})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forum_toolbar_left:
                onResult();
                break;
            //发表
            case R.id.forum_bottom_fb:
                //本次点击时间
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mBottomEdit.getText().toString())) {
                        ToastUtil.showFaliureToast(mContext, "请输入评论内容");
                    } else {
                        doFabiao(mPl_id);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回更新数据
     */
    private void onResult() {
        Intent intent = new Intent();
        intent.putExtra("zan", mHeaderView.mZanText.getText().toString());
        intent.putExtra("pl", mHeaderView.mHuiFuText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 发表评论  fplyid 为回复的谁。如果回复楼主 为0，回复评论，则传入fid
     */
    private void doFabiao(String pl_id) {
        StringRequest request = new StringRequest(IpUtil.upload_Forum_Pl + "fid=" + fid, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("fid", fid);
        request.set("fPlyid", pl_id);
        request.set("content", mBottomEdit.getText().toString());
        request(4, request, callback, false, true);
    }

}
