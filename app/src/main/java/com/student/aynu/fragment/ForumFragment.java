package com.student.aynu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.student.aynu.R;
import com.student.aynu.activity.PublishActivity;
import com.student.aynu.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 论坛
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class ForumFragment extends BaseFragment {

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_forum, null);
        ButterKnife.bind(this, v);
        return v;
    }


    @OnClick(R.id.forum_toolbar_right)
    public void onClick(View view) {
        startActivity(new Intent(mContext, PublishActivity.class));
    }

}
