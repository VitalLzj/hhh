package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/3/3 0003.
 * 邮箱：976623696@qq.com
 * 学生信息
 */
public class StudentActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.student_layout1, R.id.student_layout2, R.id.student_layout3, R.id.student_toolbar_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_layout1:
                startActivity(new Intent(mContext, StudentInfoActivity.class));
                break;
            case R.id.student_layout2:
                startActivity(new Intent(mContext, StudentScoreActivity.class));
                break;
            case R.id.student_layout3:
                break;
            case R.id.student_toolbar_left:
                finish();
                break;

        }
    }

}
