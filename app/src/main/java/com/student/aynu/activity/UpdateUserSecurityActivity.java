package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.entity.User_Security;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/10 0010.
 * 邮箱：976623696@qq.com
 */
public class UpdateUserSecurityActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.update_security1)
    EditText mSecurityEdit1;
    @BindView(R.id.update_security2)
    EditText mSecurityEdit2;
    @BindView(R.id.update_security3)
    EditText mSecurityEdit3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_update_security);
        ButterKnife.bind(this);
        getUserSecurity();
    }

    /**
     * 获取用户当前密保
     */
    private void getUserSecurity() {
        StringRequest request = new StringRequest(IpUtil.getUser_Security, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request(0, request, callback, false, true);
    }

    @OnClick({R.id.update_security_toolbar_left, R.id.update_security_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_security_toolbar_left:
                finish();
                break;
            case R.id.update_security_sure:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mSecurityEdit1.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入您的QQ号码");
                        return;
                    } else if (TextUtils.isEmpty(mSecurityEdit2.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入您的邮箱地址");
                        return;
                    } else if (TextUtils.isEmpty(mSecurityEdit3.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入您的电话号码");
                        return;
                    } else if (!isEmail(mSecurityEdit2.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入正确的邮箱格式");
                        return;
                    } else if (!isphoneNumber(mSecurityEdit3.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入正确的电话格式");
                        return;
                    } else {
                        setSecurity();
                    }
                }
                break;
        }
    }

    /**
     * 设置新的密保
     */
    private void setSecurity() {
        StringRequest request = new StringRequest(IpUtil.setNew_Security, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("userSecurity1", Sha1Util.encode(mSecurityEdit1.getText().toString()));
        request.set("userSecurity2", Sha1Util.encode(mSecurityEdit2.getText().toString()));
        request.set("userSecurity3", Sha1Util.encode(mSecurityEdit3.getText().toString()));
        request(1, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            switch (what) {
                case 0:
                    User_Security security = gson.fromJson(responseInfo, User_Security.class);
                    if (security.getCode() == 0) {
                        if (!security.getData().getUsersecurity1().equals("")) {
                            mSecurityEdit1.setText("******");
                            mSecurityEdit2.setText("******");
                            mSecurityEdit3.setText("******");
                        }

                    } else {
                        //登录过期了，重新登录
                        ToastUtil.showFaliureToast(mContext, "请重新登录");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case 1:
                    Base_entity base2 = gson.fromJson(responseInfo, Base_entity.class);
                    if (base2.getCode() == 0) {
                        ToastUtil.showFaliureToast(mContext, "修改成功");
                        //修改成功，推出
                        Intent intent = new Intent();
                        intent.putExtra("result", "success");
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (base2.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, "修改失败");
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

        }
    };

    /**
     * @param email
     * @return 邮箱地址的正则
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.find();
    }

    /**
     * @param phonenumber
     * @return 电话号码的正则
     */
    public boolean isphoneNumber(String phonenumber) {
        String regex = ("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phonenumber);
        return m.find();
    }
}
