package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by lzj on 2016/12/27 0027.
 * 邮箱：976623696@qq.com
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_name)
    EditText mUnameEdit;
    @BindView(R.id.login_pwd)
    EditText mUpwdEdit;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    //上次点击时间
    private long lastClickTime = 0;

    @OnClick({R.id.login_regist, R.id.login_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regist:
                startActivity(new Intent(this, Register1Activity.class));
                break;
            case R.id.login_login:
                //本次点击时间
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mUnameEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入用户名");
                        return;
                    } else if (TextUtils.isEmpty(mUpwdEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入密码");
                        return;
                    } else {
                        //进行登录
                        doLogin();
                    }
                }
                break;
        }
    }

    private void doLogin() {
        StringRequest request = new StringRequest(IpUtil.loginUser, RequestMethod.POST);
        request.set("uName", mUnameEdit.getText().toString());
        request.set("uPwd", Sha1Util.encode(mUpwdEdit.getText().toString()));
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base_entity = gson.fromJson(responseInfo, Base_entity.class);
            if (base_entity.getCode() == 0) {
                //登录成功会返回token
                //将token保存到本地
                getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("token", base_entity.getData()).commit();
                //将用户的id保存到sp中-----用于检测token是否过期
                String user_Id = base_entity.getData().split("@")[0];
                getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("user_id", user_Id).commit();
                //跳转到首页
                Intent intent = new Intent();
                intent.putExtra("select", 5);
                setResult(0, intent);
                finish();
            } else {
                ToastUtil.showFaliureToast(mContext, base_entity.getMessage());
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * @param keyCode
     * @param event
     * @return 如果按了返回键。跳回首页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("select", 1);
            setResult(0, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
