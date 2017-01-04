package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新用户注册第一步 检测当前账号是否已注册
 * Created by lzj on 2017/1/4 0004.
 * 邮箱：976623696@qq.com
 */
public class Register1Activity extends BaseActivity {

    @BindView(R.id.register_phone_edit)
    EditText mPhoneNumberEdit;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_regist_phone);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.register_phone_toolbar_left, R.id.register_phone_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_phone_toolbar_left:
                finish();
                break;
            case R.id.register_phone_next:
                if (TextUtils.isEmpty(mPhoneNumberEdit.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入手机号");
                    return;
                } else if (!isphoneNumber(mPhoneNumberEdit.getText().toString())) {
                    ToastUtil.showText(mContext, "手机格式不正确，请检查");
                    return;
                } else {
                    checkIsRegister();
                }
                break;
        }
    }

    /**
     * 检测当前账号是否可以注册
     */
    private void checkIsRegister() {
        StringRequest request = new StringRequest(IpUtil.checkUserIsExist + "phone=" + mPhoneNumberEdit.getText().toString(), RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base_entity = gson.fromJson(responseInfo, Base_entity.class);
            if (base_entity.getCode() == 0) {
                //该用户可以注册，跳转到获取验证码
                Intent intent = new Intent(mContext, Register_GetCodeActivity.class);
                intent.putExtra("phone_number", mPhoneNumberEdit.getText().toString());
                startActivity(intent);
            } else {
                ToastUtil.showFaliureToast(mContext, base_entity.getMessage());
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 电话号正则表达式
     *
     * @param phoneNumber
     * @return
     */
    public boolean isphoneNumber(String phoneNumber) {
        String regex = "^1(3|4|5|7|8)\\d{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        return m.find();
    }
}
