package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.entity.User_Info;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.Bitmaputils;
import com.student.aynu.util.GlideLoader;
import com.student.aynu.util.IpUtil;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/9 0009.
 * 邮箱：976623696@qq.com
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.info_head)
    RoundedImageView mUser_Head;
    @BindView(R.id.info_id)
    TextView mUser_Id;
    @BindView(R.id.info_name)
    TextView mUser_Name;
    @BindView(R.id.info_sex)
    TextView mUser_Sex;
    @BindView(R.id.info_mb)
    TextView mUser_Mb;
    private Context mContext;
    //调用系统相册的code
    private static final int REQUEST_CODE = 0;
    //存放选择图片的地址
    private ArrayList<String> path = new ArrayList<>();
    //所选图片的url
    private String image_url = "";
    //修改用户名的code
    private static final int UPDATE_NAME = 1;
    //修改用户性别的pop
    private PopupWindow mPopupWindow;
    //当前选中的性别
    private String mSex = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        StringRequest request = new StringRequest(IpUtil.getUser_info, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();

            switch (what) {
                case 0:
                    User_Info user = gson.fromJson(responseInfo, User_Info.class);
                    if (user.getCode() == 0) {
                        //登陆成功
                        //头像
                        if (!user.getData().get(0).getUserhead().equals("")) {
                            Glide.with(mContext).load(user.getData().get(0).getUserhead()).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(mUser_Head);
                        }
                        //ID
                        mUser_Id.setText(user.getData().get(0).getUserid());
                        //设置用户名
                        mUser_Name.setText(user.getData().get(0).getUsername());
                        //性别
                        if ("".equals(user.getData().get(0).getUsersex())) {
                            mUser_Sex.setText("未设置");
                        } else {
                            mUser_Sex.setText(user.getData().get(0).getUsersex());
                        }
                        //密保
                        if ("".equals(user.getData().get(0).getUsersecurity1())) {
                            mUser_Mb.setVisibility(View.VISIBLE);
                        } else {
                            mUser_Mb.setVisibility(View.GONE);
                        }
                    } else {
                        //登录过期了，重新登录
                        ToastUtil.showFaliureToast(mContext, "请重新登录");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case 1:
                    Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
                    if (base.getCode() == 0) {
                        //上传成功
                        Glide.with(mContext).load(image_url).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(mUser_Head);
                    } else if (base.getCode() == 1) {
                        ToastUtil.showFaliureToast(mContext, "上传失败");
                    } else {
                        //登录过期了，重新登录
                        ToastUtil.showFaliureToast(mContext, "请重新登录");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case 2:
                    Base_entity base2 = gson.fromJson(responseInfo, Base_entity.class);
                    if (base2.getCode() == 0) {
                        //修改成功
                        mUser_Sex.setText(mSex);
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
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

    @OnClick({R.id.info_relay1, R.id.info_relay2, R.id.info_relay3, R.id.info_relay4, R.id.info_relay5, R.id.info_toolbar_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_toolbar_left:
                finish();
                break;
            case R.id.info_relay1:
                //设置头像
                setUserHead();
                break;
            case R.id.info_relay2:
                //修改用户名
                Intent intent = new Intent(mContext, UpdateUserName.class);
                intent.putExtra("userName", mUser_Name.getText().toString());
                startActivityForResult(intent, UPDATE_NAME);
                break;
            case R.id.info_relay3:
                showPopupWindow();
                break;
            case R.id.info_relay4:
                break;
            case R.id.info_relay5:
                break;
        }
    }

    /**
     * 上传用户头像
     *
     * @param image_url 头像路径
     */
    public void upLoadHead(String image_url) {
        StringRequest request = new StringRequest(IpUtil.updateUser_Head, RequestMethod.POST);
        File file = new File(image_url);
        //更改用户头像是，新头像的名字为用户id命名
        String filename = getSharedPreferences("TOKEN", MODE_PRIVATE).getString("user_id", "");
        Bitmap compressBitmap = new Bitmaputils().getCompressBitmap(file.getAbsolutePath(), 200, 200);
        request.set("user_head", new BitmapBinary(compressBitmap, filename));
        request.set("token", mApplication.uname_token);
        request(1, request, callback, false, true);
    }

    /**
     * 设置用户头像
     */
    private void setUserHead() {
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
        ImageSelector.open(UserInfoActivity.this, imageConfig); // 开启图片选择器
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
            upLoadHead(image_url);
        } else if (requestCode == UPDATE_NAME && resultCode == RESULT_OK && data != null) {
            String new_Name = data.getStringExtra("new_Name");
            mUser_Name.setText(new_Name);
        }
    }


    /**
     * 设置性别
     */
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_sex, null);
        mPopupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        mPopupWindow.setContentView(contentView);

        // 男 女 取消
        RelativeLayout mEditLayout = (RelativeLayout) contentView
                .findViewById(R.id.pop_sex_man);
        RelativeLayout mDeleteLayout = (RelativeLayout) contentView
                .findViewById(R.id.pop_sex_woman);
        RelativeLayout mCancleLayout = (RelativeLayout) contentView
                .findViewById(R.id.pop_sex_quite);

        // 男
        mEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSex = "男";
                doUpdate(mSex);
            }
        });
        // 女
        mDeleteLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mSex = "女";
                doUpdate(mSex);
            }
        });
        // 取消
        mCancleLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
            }
        });

        View rootView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_user_info, null);

        // 点击外部消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setAnimationStyle(R.style.animation);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.7f);// 0.0-1.0
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }

    /**
     * 更新性别
     *
     * @param mSex
     */
    private void doUpdate(String mSex) {
        StringRequest request = new StringRequest(IpUtil.updateUser_Sex, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("sex", mSex);
        request(2, request, callback, false, true);
    }

    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}