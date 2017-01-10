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
import com.student.aynu.entity.Base_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/10 0010.
 * 邮箱：976623696@qq.com
 */
public class UpdateUserPwdActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.user_pwd_new)
    EditText mPwd_Edit;
    @BindView(R.id.user_Rpwd_new)
    EditText mRPwd_Edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.user_pwd_toolbar_left, R.id.user_pwd_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_pwd_toolbar_left:
                finish();
                break;
            case R.id.user_pwd_sure:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mPwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入密码");
                        return;
                    } else if (TextUtils.isEmpty(mRPwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入确认密码");
                        return;
                    } else if (!mPwd_Edit.getText().toString().equals(mRPwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "两次密码不一致，请检查");
                        return;
                    } else {
                        doUpdate();
                    }
                }
                break;
        }
    }

    /**
     * 修改密码 sha1加密
     */
    private void doUpdate() {
        StringRequest request = new StringRequest(IpUtil.updateUser_Pwd, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("newPwd", Sha1Util.encode(mPwd_Edit.getText().toString()));
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base2 = gson.fromJson(responseInfo, Base_entity.class);
            if (base2.getCode() == 0) {
                //修改成功
                finish();
                ToastUtil.showText(mContext, "修改成功");
            } else if (base2.getCode() == 1) {
                ToastUtil.showFaliureToast(mContext, "修改失败");
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
