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
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/9 0009.
 * 邮箱：976623696@qq.com
 */
public class UpdateUserName extends BaseActivity {

    @BindView(R.id.user_name_edit)
    EditText mUser_NameEdit;
    private String mUser_Name = null;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_update_username);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mUser_Name = getIntent().getStringExtra("userName");
        if (mUser_Name == null) {
            ToastUtil.showFaliureToast(mContext, Constant.FAILURE_MESSAGE);
            return;
        }
        mUser_NameEdit.setText(mUser_Name);
    }

    @OnClick({R.id.user_name_toolbar_left, R.id.user_name_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_toolbar_left:
                finish();
                break;
            case R.id.user_name_sure:
                if (TextUtils.isEmpty(mUser_NameEdit.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入用户名");
                    return;
                } else if (mUser_Name.equals(mUser_NameEdit.getText().toString())) {
                    ToastUtil.showText(mContext, "请保证用户名与之前不同");
                    return;
                } else {
                    doUpdate();
                }
                break;
        }
    }

    /**
     * 更改用户名
     */
    private void doUpdate() {
        StringRequest request = new StringRequest(IpUtil.updateUser_Name, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("new_name", mUser_NameEdit.getText().toString());
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
            if (base.getCode() == 0) {
                //修改成功
                Intent intent = new Intent();
                intent.putExtra("new_Name", mUser_NameEdit.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else if (base.getCode() == 1) {
                mUser_NameEdit.setText("");
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
