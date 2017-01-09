package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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
                            Glide.with(mContext).load(user.getData().get(0).getUserhead()).placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
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
                    } else {
                        ToastUtil.showFaliureToast(mContext, "上传失败");
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
                break;
            case R.id.info_relay3:
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
        }
    }

}