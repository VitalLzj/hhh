package com.student.aynu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.activity.AboutUsActivity;
import com.student.aynu.activity.BookActivity;
import com.student.aynu.activity.FeedBackActivity;
import com.student.aynu.activity.LoginActivity;
import com.student.aynu.activity.MyForumActivity;
import com.student.aynu.activity.MyReplyActivity;
import com.student.aynu.activity.PicActivity;
import com.student.aynu.activity.BookScActivity;
import com.student.aynu.activity.QingGuoActivity;
import com.student.aynu.activity.UserInfoActivity;
import com.student.aynu.activity.YbActivity;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.entity.User_Info;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_head)
    RoundedImageView mUser_Head;
    @BindView(R.id.mine_head_name)
    TextView mUser_Name;
    @BindView(R.id.mine_swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private Context mContext;

    //头像的url
    private String mUser_Img = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, v);
        getUserInfo(true);
        //设置刷新
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserInfo(false);
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        return v;
    }

    @OnClick({R.id.mine_layout1, R.id.mine_layout2, R.id.mine_layout3,
            R.id.mine_layout4, R.id.mine_layout5, R.id.mine_layout6,
            R.id.mine_layout7, R.id.mine_layout8, R.id.mine_layout9, R.id.mine_head})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_layout1:
                startActivity(new Intent(mContext, UserInfoActivity.class));
                break;
            case R.id.mine_layout2:
                startActivity(new Intent(mContext, QingGuoActivity.class));
                break;
            case R.id.mine_layout3:
                startActivity(new Intent(mContext, BookActivity.class));
                break;
            case R.id.mine_layout4:
                startActivity(new Intent(mContext, MyForumActivity.class));
                break;
            case R.id.mine_layout5:
                startActivity(new Intent(mContext, MyReplyActivity.class));
                break;
            case R.id.mine_layout6:
                startActivity(new Intent(mContext, BookScActivity.class));
                break;
            case R.id.mine_layout7:
                startActivity(new Intent(mContext, FeedBackActivity.class));
                break;
            case R.id.mine_layout8:
                startActivity(new Intent(mContext, YbActivity.class));
                break;
            case R.id.mine_layout9:
                startActivity(new Intent(mContext, AboutUsActivity.class));
                break;
            case R.id.mine_head:
                if (null == mUser_Img) {
                    return;
                }
                Intent intent = new Intent(mContext, PicActivity.class);
                intent.putExtra("img_url", mUser_Img);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(boolean isShow) {
        StringRequest request = new StringRequest(IpUtil.getUser_info, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request(0, request, callback, false, isShow);
    }

    HttpListener<String> callback = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();

            User_Info user = gson.fromJson(responseInfo, User_Info.class);
            if (user.getCode() == 0) {
                //登陆成功
                //设置头像
                if (!user.getData().get(0).getUserhead().equals("")) {
                    mUser_Img = user.getData().get(0).getUserhead();
                    Glide.with(mContext).load(user.getData().get(0).getUserhead()).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(mUser_Head);
                }
                //设置用户名,如果用户名不为空，设置用户名，如果用户名为空，设置ID
                if ("".equals(user.getData().get(0).getUsername())) {
                    mUser_Name.setText("你好:" + user.getData().get(0).getUseraccount());
                } else {
                    mUser_Name.setText("你好:" + user.getData().get(0).getUsername());
                }
                //将用户头像保存到sp中，方便评论时使用
                mContext.getSharedPreferences("TOKEN", 0).edit().putString("user_face", mUser_Img).commit();

            } else {
                //登录过期了，重新登录
                ToastUtil.showFaliureToast(mContext, "请重新登录");
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }


        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

}
