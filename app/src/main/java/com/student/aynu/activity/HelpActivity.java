package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/1/12 0012.
 * 邮箱：976623696@qq.com
 */
public class HelpActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.help_toolbar_left, R.id.help_yb})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_toolbar_left:
                finish();
                break;
            case R.id.help_yb:
                startActivity(new Intent(mContext, YbActivity.class));
                break;
        }
    }
}
