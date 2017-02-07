package com.student.aynu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzj on 2017/2/7 0007.
 * 邮箱：976623696@qq.com
 */
public class PicActivity extends BaseActivity {

    @BindView(R.id.pic_img)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);
        ButterKnife.bind(this);

        Glide.with(this).load("http://img1.3lian.com/2015/a1/113/d/10.jpg").placeholder(R.mipmap.ic_launcher).into(mPhotoView);
        mPhotoView.enable();
    }

}
