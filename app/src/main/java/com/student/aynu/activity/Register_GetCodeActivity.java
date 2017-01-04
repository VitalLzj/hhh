package com.student.aynu.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册----获取验证码
 * Created by lzj on 2017/1/4 0004.
 * 邮箱：976623696@qq.com
 */
public class Register_GetCodeActivity extends BaseActivity {

    @BindView(R.id.register_phoneNumber)
    TextView mPhoneText;
    @BindView(R.id.register_yzm)
    EditText mCodeEdit;
    @BindView(R.id.register_get)
    TextView mGetCodeText;

    //待注册的手机号
    private String phoneNumber = null;
    private Context mContext;
    //倒计时
    private int recLen = 60;
    //终止线程
    private boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_regist);
        phoneNumber = getIntent().getStringExtra("phone_number");
        if (phoneNumber == null) {
            ToastUtil.showText(mContext, Constant.FALIURE_MESSAGE);
            return;
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        SMSSDK.initSDK(this, Constant.MOB_APP_KEY, Constant.MOB_APP_SECRET);
        mPhoneText.setText(phoneNumber);
    }


    @OnClick({R.id.register_toolbar_left, R.id.register_get, R.id.register_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_toolbar_left:
                finish();
                break;
            case R.id.register_get:
                flag = true;
                recLen = 60;
                if (recLen > 0) {
                    mGetCodeText.setEnabled(false);
                }
                getCode();
                break;
            case R.id.register_next:
                break;
        }
    }

    private Handler mHandler = new Handler();

    /**
     * 获取验证码
     */
    private void getCode() {
        //开启倒计时
        new Thread(new MyThread()).start();
        SMSSDK.getVerificationCode("86", phoneNumber);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, final Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Log.d("tag", "回调成功");
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tag", data + "获取验证码成功");
                                ToastUtil.showText(mContext, "验证码已发送成功，请注意查收");
                            }
                        });
                    }
                } else {
                    Log.d("tag", "回调失败");
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    final Handler handler = new Handler() {     // handle
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (recLen <= 60 && recLen > 0) {
                        recLen--;
                        mGetCodeText.setText(recLen + "秒");
                    } else {
                        flag = false;
                        mGetCodeText.setEnabled(true);
                        mGetCodeText.setText("获取");
                    }
            }
            super.handleMessage(msg);
        }
    };

    //倒计时
    public class MyThread implements Runnable {   // thread
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(1000);   // sleep 1000ms
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时关闭线程
        flag = false;
        SMSSDK.unregisterAllEventHandler();
    }
}
