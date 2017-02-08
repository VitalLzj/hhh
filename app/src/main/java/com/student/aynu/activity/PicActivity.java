package com.student.aynu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private String url = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("img_url");

        Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher).into(mPhotoView);
        mPhotoView.enable();
    }

}
