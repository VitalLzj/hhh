package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.Bitmaputils;
import com.student.aynu.util.GlideLoader;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_name)
    EditText mUserName_Edit;
    @BindView(R.id.register_password)
    EditText mUserPwd_Edit;
    @BindView(R.id.register_rePassword)
    EditText mUserRpwd_Edit;

    private Context mContext;
    //调用系统相册的code
    private static final int REQUEST_CODE = 0;
    //存放选择图片的地址
    private ArrayList<String> path = new ArrayList<>();
    //所选图片的url
    private String image_url = "";
    //头像
    @BindView(R.id.register_head)
    RoundedImageView mUser_head;
    //检测用户名是否可用
    @BindView(R.id.register_check)
    TextView mCheck_Text;
    //标记位，该账号是否可以注册
    private boolean isRegister = false;
    //密保pop
    private PopupWindow mPop;
    //密保的三个问题
    EditText mSecurityEdit1;
    EditText mSecurityEdit2;
    EditText mSecurityEdit3;
    //注册成功后用户的id
    private String mUser_Id = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_regist_phone);
        ButterKnife.bind(this);
        mUserName_Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCheck_Text.setText("检测");
                mCheck_Text.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //上次点击时间
    private long lastClickTime = 0;

    @OnClick({R.id.register_phone_toolbar_left, R.id.register_head, R.id.register_check, R.id.register_phone_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_phone_toolbar_left:
                finish();
                break;
            case R.id.register_head:
                choosePic();
                break;
            case R.id.register_check:
                if (TextUtils.isEmpty(mUserName_Edit.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入用户ID");
                    return;
                }
                Check_User();
                break;
            case R.id.register_phone_next:
                //本次点击时间
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (TextUtils.isEmpty(mUserPwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入密码");
                        return;
                    } else if (TextUtils.isEmpty(mUserRpwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "请输入确认密码");
                        return;
                    } else if (!mUserPwd_Edit.getText().toString().equals(mUserRpwd_Edit.getText().toString())) {
                        ToastUtil.showText(mContext, "两次密码不一致，请检查");
                        return;
                    } else if (!isRegister) {
                        ToastUtil.showText(mContext, "请确保账号是否可用");
                        return;
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("该ID供以后登录使用,一经注册不可更改,请慎重！！！")
                                .setCancelable(false)
                                .setNegativeButton("前去更改", null)
                                .setPositiveButton("立即注册", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        doRegister();
                                    }
                                });
                        builder.show();
                    }
                }
                break;
        }
    }

    /**
     * 设置密保
     */
    private void setSecurity() {
        StringRequest request = new StringRequest(IpUtil.setSecurity, RequestMethod.POST);
        request.set("uId", mUser_Id);
        request.set("uSecurity1", Sha1Util.encode(mSecurityEdit1.getText().toString()));
        request.set("uSecurity2", Sha1Util.encode(mSecurityEdit2.getText().toString()));
        request.set("uSecurity3", Sha1Util.encode(mSecurityEdit3.getText().toString()));
        request(2, request, callback, false, true);
    }

    /**
     * 提示对话框
     */
    private void showWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("设置密保问题方便您找回密码，请慎重")
                .setCancelable(false)
                .setNegativeButton("我要设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                }).setPositiveButton("我要离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (mPop != null && mPop.isShowing()) {
                    mPop.dismiss();
                }
                //注册成功，跳转到登录界面，但未设置密保
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //进行注册
    private void doRegister() {
        //是否上传了头像,0代表没有
        String is_head = "0";
        StringRequest request = new StringRequest(IpUtil.registerUser, RequestMethod.POST);
        request.set("uName", mUserName_Edit.getText().toString());
        request.set("pwd", Sha1Util.encode(mUserPwd_Edit.getText().toString()));
        if (!image_url.equals("")) {
            File file = new File(image_url);
            //新文件的文件名
            String filename = System.currentTimeMillis() + "";
            Bitmap compressBitmap = new Bitmaputils().getCompressBitmap(file.getAbsolutePath(), 200, 200);
            request.set("user_head", new BitmapBinary(compressBitmap, filename));
            is_head = "1";
        }
        request.set("is_head", is_head);
        request(1, request, callback, false, true);
    }

    /**
     * 检测当前账号是否可以注册
     */
    private void Check_User() {
        StringRequest request = new StringRequest(IpUtil.checkUserIsExist, RequestMethod.POST);
        request.set("uName", mUserName_Edit.getText().toString());
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            switch (what) {
                case 0:
                    Base_entity base_entity = gson.fromJson(responseInfo, Base_entity.class);
                    if (base_entity.getCode() == 0) {
                        isRegister = true;
                        mCheck_Text.setText("可注册");
                        mCheck_Text.setEnabled(false);
                    } else {
                        isRegister = false;
                        mCheck_Text.setText("已注册");
                        mCheck_Text.setEnabled(false);
                        ToastUtil.showFaliureToast(mContext, base_entity.getMessage());
                    }
                    break;
                case 1:
                    Base_entity is_Success = gson.fromJson(responseInfo, Base_entity.class);
                    if (is_Success.getCode() == 0) {
                        mUser_Id = is_Success.getData();
                        //注册成功，设置密保
                        showPop();
                    } else {
                        isRegister = false;
                        mUserName_Edit.setText("");
                        mUserPwd_Edit.setText("");
                        mUserRpwd_Edit.setText("");
                        ToastUtil.showFaliureToast(mContext, is_Success.getMessage());
                    }
                    break;
                case 2:
                    Base_entity is_Succeed = gson.fromJson(responseInfo, Base_entity.class);
                    if (is_Succeed.getCode() == 0) {
                        //密保设置成功，跳转登录界面
                        finish();
                    } else {
                        mSecurityEdit1.setText("");
                        mSecurityEdit2.setText("");
                        mSecurityEdit3.setText("");
                        ToastUtil.showFaliureToast(mContext, is_Succeed.getMessage());
                    }
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 弹出密保提示
     */
    private void showPop() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_security, null);
        mPop = new PopupWindow(v, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPop.setContentView(v);

        mSecurityEdit1 = (EditText) v.findViewById(R.id.register_security1);
        mSecurityEdit2 = (EditText) v.findViewById(R.id.register_security2);
        mSecurityEdit3 = (EditText) v.findViewById(R.id.register_security3);

        v.findViewById(R.id.register_security_quite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningDialog();
            }
        });
        v.findViewById(R.id.register_security_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mSecurityEdit1.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入您的QQ号码");
                } else if (TextUtils.isEmpty(mSecurityEdit2.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入您的邮箱地址");
                } else if (TextUtils.isEmpty(mSecurityEdit3.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入您的电话号码");
                } else if (!isEmail(mSecurityEdit2.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入正确的邮箱格式");
                } else if (!isphoneNumber(mSecurityEdit3.getText().toString())) {
                    ToastUtil.showText(mContext, "请输入正确的电话格式");
                } else {
                    setSecurity();
                }
            }
        });
        //主布局
        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_regist_phone, null);
        //淡入淡出
        mPop.setAnimationStyle(R.style.animation_bottom);
        mPop.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mPop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * @param email 邮箱地址
     * @return 邮箱地址的正则
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.find();
    }

    /**
     * @param phonenumber 电话号码
     * @return 电话号码的正则
     */
    public boolean isphoneNumber(String phonenumber) {
        String regex = ("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phonenumber);
        return m.find();
    }

    /**
     * 图片选择
     */
    private void choosePic() {
        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(Color.rgb(153, 0, 0))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(Color.rgb(153, 0, 0))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open(RegisterActivity.this, imageConfig); // 开启图片选择器
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            image_url = pathList.get(pathList.size() - 1);
            Glide.with(mContext).load(image_url).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(mUser_head);
        }
    }
}
