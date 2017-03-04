package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.bean.Base_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/11 0011.
 * 邮箱：976623696@qq.com
 */
public class FeedBackActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.feedback_text)
    EditText mFeedEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.feedback_toolbar_left, R.id.feedback_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_toolbar_left:
                finish();
                break;
            case R.id.feedback_upload:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mFeedEdit.getText().toString())) {
                        ToastUtil.showFaliureToast(mContext, "请输入反馈内容");
                    } else {
                        upLoadFeed();
                    }
                }
                break;
        }
    }

    /**
     * 提交反馈意见
     */
    private void upLoadFeed() {
        StringRequest request = new StringRequest(IpUtil.upload_Feed, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("feedContent", mFeedEdit.getText().toString());
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
            if (base.getCode() == 0) {
                //反馈成功
                ToastUtil.showText(mContext, "反馈成功");
                finish();
            } else if (base.getCode() == 1) {
                mFeedEdit.setText("");
                ToastUtil.showFaliureToast(mContext, "反馈失败");
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
