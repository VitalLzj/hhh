package com.student.aynu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2016/12/27 0027.
 * 邮箱：976623696@qq.com
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_regist)
    TextView mRegisterText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_regist)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regist:
                startActivity(new Intent(this, Register1Activity.class));
                break;
        }
    }

}
