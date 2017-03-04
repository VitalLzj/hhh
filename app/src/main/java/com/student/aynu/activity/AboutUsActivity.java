package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.bean.App_info;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.student.aynu.util.VersionUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/11 0011.
 * 邮箱：976623696@qq.com
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.about_us_phone)
    TextView mPhoneText;
    @BindView(R.id.about_us_qq)
    TextView mQqText;
    @BindView(R.id.about_us_email)
    TextView mEmailText;
    @BindView(R.id.about_us_version)
    TextView mVersionText;

    private Context mContext;
    private PopupWindow mIntroducePop;
    private String mIntroduceText = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        getAboutUsInfo();
    }

    /**
     * 获取关于我们信息
     */
    private void getAboutUsInfo() {
        StringRequest request = new StringRequest(IpUtil.getAPP_Info, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            App_info info = gson.fromJson(responseInfo, App_info.class);
            if (info.getCode() == 0) {
                //获取成功
                mIntroduceText = info.getData().getAintroduce();
                mQqText.setText(info.getData().getAqq());
                mEmailText.setText(info.getData().getAemail());
                mPhoneText.setText(info.getData().getAphone());
                mVersionText.setText("安师说v" + VersionUtil.getVersionName(mContext));
            } else {
                ToastUtil.showFaliureToast(mContext, "加载失败");
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    @OnClick({R.id.about_us_toolbar_left, R.id.about_us_1, R.id.about_us_3,
            R.id.about_us_4, R.id.about_us_5, R.id.about_us_6})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us_toolbar_left:
                finish();
                break;
            case R.id.about_us_1:
                showIntroducePop();
                break;
            case R.id.about_us_3:
                showPhoneDialog();
                break;
            case R.id.about_us_4:
                break;
            case R.id.about_us_5:
                break;
            case R.id.about_us_6:
                break;
        }
    }

    /**
     * 展示App介绍
     */
    private void showIntroducePop() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_introduce, null);
        mIntroducePop = new PopupWindow(v, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mIntroducePop.setContentView(v);

        TextView mIntroduce = (TextView) v.findViewById(R.id.introduce_text);
        mIntroduce.setText(mIntroduceText);
        mIntroduce.setMovementMethod(ScrollingMovementMethod.getInstance());

        //主布局
        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_aboutus, null);
        // 点击外部消失
        mIntroducePop.setBackgroundDrawable(new BitmapDrawable());
        mIntroducePop.setOutsideTouchable(true);
        //动画加显示
        //淡入淡出
        mIntroducePop.setAnimationStyle(R.style.animation);
        mIntroducePop.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mIntroducePop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mIntroducePop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });

    }

    /**
     * 是否拨打电话
     */
    private void showPhoneDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mPhoneText.getText().toString())
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneNumber = mPhoneText.getText().toString();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}