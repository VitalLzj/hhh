package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.bean.Base_entity;
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
public class ForGivePwdActivity extends BaseActivity {

    private Context mContext;
    private String mUserId = null;
    @BindView(R.id.forgive_pwd_security1)
    EditText mSecurityEdit1;
    @BindView(R.id.forgive_pwd_security2)
    EditText mSecurityEdit2;
    @BindView(R.id.forgive_pwd_security3)
    EditText mSecurityEdit3;
    private PopupWindow mPwdPop = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_forgive_pwd);
        mUserId = getIntent().getStringExtra("user_id");
        Log.d("tag", mUserId);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.forgive_pwd_security_sure, R.id.forgive_pwd_toolbar_left, R.id.forgive_pwd_problem})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgive_pwd_toolbar_left:
                finish();
                break;
            case R.id.forgive_pwd_security_sure:
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
                        checkSecurity();
                    }
                }
                break;
            case R.id.forgive_pwd_problem:
                showWarnDialog();
                break;
        }
    }

    /**
     * 提示对话框
     */
    private void showWarnDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("有问题请联系QQ:976623696")
                .setCancelable(false)
                .setPositiveButton("确定", null);
        builder.show();
    }

    /**
     * 检测密保
     */
    private void checkSecurity() {
        StringRequest request = new StringRequest(IpUtil.checkUser_Security, RequestMethod.POST);
        request.set("uid", mUserId);
        request.set("uSecurity1", Sha1Util.encode(mSecurityEdit1.getText().toString()));
        request.set("uSecurity2", Sha1Util.encode(mSecurityEdit2.getText().toString()));
        request.set("uSecurity3", Sha1Util.encode(mSecurityEdit3.getText().toString()));
        request(0, request, callback, false, true);
    }

    /**
     * @param s 新密码
     *          设置新密码
     */
    private void updatePwd(String s) {
        StringRequest request = new StringRequest(IpUtil.setNew_Pwd, RequestMethod.POST);
        request.set("uid", mUserId);
        request.set("new_pwd", Sha1Util.encode(s));
        request(1, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Base_entity base2 = gson.fromJson(responseInfo, Base_entity.class);
            switch (what) {
                case 0:
                    if (base2.getCode() == 0) {
                        //重新设置密码
                        showPwdPop();
                    } else if (base2.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, "验证失败,请检查密保");
                    }
                    break;
                case 1:
                    if (base2.getCode() == 0) {
                        //设置密码成功
                        ToastUtil.showFaliureToast(mContext, "修改成功");
                        finish();
                    } else if (base2.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, "设置失败");
                    }
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 重新设置密码
     */
    private void showPwdPop() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_new_password, null);
        mPwdPop = new PopupWindow(v, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPwdPop.setContentView(v);

        final EditText mPwdEdit = (EditText) v.findViewById(R.id.user_new_password);
        final EditText mRPwdEdit = (EditText) v.findViewById(R.id.user_new_rePassword);

        v.findViewById(R.id.user_new_password_quite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPwdPop != null && mPwdPop.isShowing()) {
                    mPwdPop.dismiss();
                }
            }
        });
        v.findViewById(R.id.user_new_password_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //本次点击时间
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mPwdEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入新密码");
                    } else if (TextUtils.isEmpty(mRPwdEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入确认密码");
                    } else if (!mPwdEdit.getText().toString().equals(mRPwdEdit.getText().toString())) {
                        ToastUtil.showText(mContext, "请确认两次输入的密码一致");
                    } else {
                        updatePwd(mPwdEdit.getText().toString());
                    }
                }
            }
        });
        //主布局
        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_forgive_pwd, null);
        // 点击外部消失
        mPwdPop.setBackgroundDrawable(new BitmapDrawable());
        mPwdPop.setOutsideTouchable(true);
        //动画加显示
        //淡入淡出
        mPwdPop.setAnimationStyle(R.style.animation);
        mPwdPop.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mPwdPop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mPwdPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }


    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.find();
    }

    public boolean isphoneNumber(String phonenumber) {
        String regex = ("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phonenumber);
        return m.find();
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
