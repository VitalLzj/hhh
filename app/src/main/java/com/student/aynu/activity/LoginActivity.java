package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.entity.Login_entity;
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
    //判断从哪个activity过来的
    private int flag = 0;
    private PopupWindow mUserPop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_login);
        flag = getIntent().getIntExtra("flag", 0);
        Log.d("tag", flag + "flag");
        ButterKnife.bind(this);
    }

    //上次点击时间
    private long lastClickTime = 0;

    @OnClick({R.id.login_regist, R.id.login_login, R.id.login_miss})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regist:
                startActivity(new Intent(this, RegisterActivity.class));
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
            case R.id.login_miss:
                showUserPop();
                break;
        }
    }

    /**
     * 弹出输入用户名的框
     */
    private void showUserPop() {

        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_username, null);
        mUserPop = new PopupWindow(v, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mUserPop.setContentView(v);

        final EditText mUNameEdit = (EditText) v.findViewById(R.id.user_username);

        v.findViewById(R.id.user_username_quite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserPop != null && mUserPop.isShowing()) {
                    mUserPop.dismiss();
                }
            }
        });
        v.findViewById(R.id.user_username_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //本次点击时间
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mUNameEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入需要找回的账号");
                    } else {
                        checkUName(mUNameEdit.getText().toString());
                    }
                }
            }
        });
        //主布局
        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null);
        // 点击外部消失
        mUserPop.setBackgroundDrawable(new BitmapDrawable());
        mUserPop.setOutsideTouchable(true);
        //动画加显示
        //淡入淡出
        mUserPop.setAnimationStyle(R.style.animation);
        mUserPop.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mUserPop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mUserPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }

    /**
     * 检测当前账号是否存在
     *
     * @param s 当前账号
     */
    private void checkUName(String s) {
        StringRequest request = new StringRequest(IpUtil.checkUserIsExist, RequestMethod.POST);
        request.set("uName", s);
        request(1, request, callback, false, true);
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
            switch (what) {
                case 0:
                    Login_entity login_entity = gson.fromJson(responseInfo, Login_entity.class);
                    if (login_entity.getCode() == 0) {
                        //登录成功会返回token
                        //将token保存到本地
                        getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("token", login_entity.getData().getUtoken()).commit();
                        //将用户的id保存到sp中-----用于检测token是否过期
                        String user_Id = login_entity.getData().getUid();
                        getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("user_id", user_Id).commit();
                        //保存一个当前时间，用于在一天后清理token。
                        //系统当前毫秒数
                        long now_time = System.currentTimeMillis();
                        getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putLong("token_time", now_time).commit();
                        //将用户名保存sp中，方便评论使用
                        mContext.getSharedPreferences("TOKEN", 0).edit().putString("user_name", mUnameEdit.getText().toString()).commit();
                        //跳转到首页
                        Intent intent = new Intent();
                        intent.putExtra("select", 5);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.showFaliureToast(mContext, login_entity.getMessage());
                    }
                    break;
                case 1:
                    Base_entity base_entity = gson.fromJson(responseInfo, Base_entity.class);
                    if (base_entity.getCode() == 1) {
                        //1代表该账号已注册，可以找回密码
                        if (mUserPop != null && mUserPop.isShowing()) {
                            mUserPop.dismiss();
                        }
                        Intent intent = new Intent(mContext, ForGivePwdActivity.class);
                        intent.putExtra("user_id", base_entity.getData());
                        startActivity(intent);
                    } else {
                        ToastUtil.showFaliureToast(mContext, "该账号尚未注册,请仔细检查");
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            switch (flag) {
                case 5:
                    Intent intent1 = new Intent();
                    intent1.putExtra("select", 1);
                    setResult(RESULT_OK, intent1);
                    break;
                case 4:
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("select", 1);
                    startActivity(intent);
                    mApplication.finishOtherActivity();
                    break;
                case 0:
                    break;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
