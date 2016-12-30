package com.student.aynu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.student.aynu.R;
import com.student.aynu.base.BaseFragment;

/**
 * 资讯
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class ForumFragment extends BaseFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forum, null);
        return v;
    }

}
