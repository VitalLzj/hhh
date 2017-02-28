package com.student.aynu.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.fragment.PicFragment;
import com.student.aynu.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/2/7 0007.
 * 校园风采
 * 邮箱：976623696@qq.com
 */
public class StyleActivity extends BaseActivity {

    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_style);
        ButterKnife.bind(this);
    }



}
