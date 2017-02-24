package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.adapter.Forum_PicAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.constant.Constant;
import com.student.aynu.entity.Base_entity;
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
import com.yolanda.nohttp.rest.Request;
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
 * Created by lzj on 2017/2/15 0015.
 * 邮箱：976623696@qq.com
 */
public class PublishActivity extends BaseActivity {

    private Context mContext;
    //调用系统相册的code
    private static final int REQUEST_CODE = 0;
    //存放选择图片的地址
    private ArrayList<String> path = new ArrayList<>();
    //存放所选图片的RecyclerView;

    @BindView(R.id.publish_forum_recycler)
    RecyclerView mRecycler;
    Forum_PicAdapter mAdapter;
    //    @BindView(R.id.publish_forum_title)
//    EditText mTitleEdit;
    @BindView(R.id.publish_forum_content)
    EditText mContentEdit;
    @BindView(R.id.publish_forum_relative)
    RelativeLayout mPublishRelativeLayout;
    @BindView(R.id.publish_forum_publish)
    TextView mPublishText;

    private static final String TAG = "PublishActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_publishforum);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeBack();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeBack() {
        if (mContentEdit.getText().toString().equals("")) {
            mPublishRelativeLayout.setBackgroundResource(R.drawable.publish_forum_back);
            mPublishText.setTextColor(Color.rgb(102, 102, 102));
        } else {
            mPublishRelativeLayout.setBackgroundResource(R.drawable.publish_forum_back_pressed);
            mPublishText.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    @OnClick({R.id.publish_forum_toolbar_left, R.id.publish_forum_choose, R.id.publish_forum_relative})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_forum_toolbar_left:
                finish();
                break;
            case R.id.publish_forum_choose:
                //选择图片
                choosePic();
                break;
            case R.id.publish_forum_relative:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > Constant.MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (mContentEdit.getText().toString().equals("")) {
                        ToastUtil.showFaliureToast(mContext, "请输入内容");
                        return;
                    } else {
                        doPublish();
                    }
                }
                break;
        }
    }

    /**
     * 进行发布
     */
    private void doPublish() {
        StringRequest request = new StringRequest(IpUtil.upload_Forum, RequestMethod.POST);
        request.set("token", mApplication.uname_token);
        request.set("forumContent", mContentEdit.getText().toString());
        if (path.size() != 0) {
            for (int i = 0; i < path.size(); i++) {
                File file = new File(path.get(i));
                Log.d(TAG, "doPublish: " + file.getName());
                Bitmap compressBitmap = new Bitmaputils().getCompressBitmap(file.getAbsolutePath(), 200, 200);
                request.add("images[]", new BitmapBinary(compressBitmap, file.getName()));
            }
        }

        request(0, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            Log.d(TAG, "onSucceed: " + responseInfo);
            Base_entity base = gson.fromJson(responseInfo, Base_entity.class);
            if (base.getCode() == 0) {
                //发表成功
                ToastUtil.showText(mContext, base.getMessage());
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (base.getCode() == 1) {
                mContentEdit.setText("");
                ToastUtil.showFaliureToast(mContext, base.getMessage());
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
                .crop()
                // 多选时的最大数量   （默认 3 张）
                .mutiSelectMaxSize(3)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open(PublishActivity.this, imageConfig); // 开启图片选择器
    }

    /**
     * @param requestCode 请求码
     * @param resultCode  响应吗
     * @param data        返回数据 相册回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //获取所选图片的地址
            final List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            Log.d("tag", "size=" + pathList.size());
            //如果有图片选中显示
            if (pathList.size() > 0) {
                mRecycler.setVisibility(View.VISIBLE);
            } else {
                mRecycler.setVisibility(View.GONE);
            }
            //垂直方向
            if (null == mAdapter) {
                GridLayoutManager manager = new GridLayoutManager(mContext, 3);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                mRecycler.setLayoutManager(manager);
                mAdapter = new Forum_PicAdapter(pathList, mContext);
                mRecycler.setAdapter(mAdapter);
            } else {
                mAdapter = new Forum_PicAdapter(pathList, mContext);
                mRecycler.setAdapter(mAdapter);
            }
            mAdapter.setOnPhotoClcikListener(new Forum_PicAdapter.onPhotoClcikListener() {
                @Override
                public void onClick(View v, int position) {
                    Intent intent = new Intent(mContext, PicActivity.class);
                    intent.putExtra("img_url", pathList.get(position));
                    startActivity(intent);
                }
            });
        }
    }
}
