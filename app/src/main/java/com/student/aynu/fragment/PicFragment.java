package com.student.aynu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.student.aynu.R;
import com.student.aynu.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by lzj on 2017/2/7 0007.
 * 邮箱：976623696@qq.com
 */
public class PicFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pic, null);
        ButterKnife.bind(this, v);
        return v;
    }
}
