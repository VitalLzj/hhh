package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新用户注册第一步 检测当前账号是否已注册
 * Created by lzj on 2017/1/4 0004.
 * 邮箱：976623696@qq.com
 */
public class Register1Activity extends BaseActivity {

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
                    ToastUtil.showText(mContext, "请输入用户名");
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
                        doRegister();
                    }
                }
                break;
        }
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
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Log.d("tag", "裁剪前" + bitmap.getAllocationByteCount() + "");
            Bitmap compressBitmap = new Bitmaputils().getCompressBitmap(file.getAbsolutePath(), 200, 200);
            Log.d("tag", "裁剪后" + compressBitmap.getAllocationByteCount() + "");

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
                    Base_entity is_Succees = gson.fromJson(responseInfo, Base_entity.class);
                    if (is_Succees.getCode() == 0) {
                        //注册成功，跳转到登录界面
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    } else {
                        isRegister = false;
                        mUserName_Edit.setText("");
                        mUserPwd_Edit.setText("");
                        mUserRpwd_Edit.setText("");
                        ToastUtil.showFaliureToast(mContext, is_Succees.getMessage());
                    }
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };


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
        ImageSelector.open(Register1Activity.this, imageConfig); // 开启图片选择器
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
