package com.student.aynu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.util.IpUtil;
import com.student.aynu.webView.MyWebView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/3/3 0003.
 * 邮箱：976623696@qq.com
 * 学生学籍信息
 */
public class StudentInfoActivity extends BaseActivity {

    @BindView(R.id.student_info_web)
    MyWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        ButterKnife.bind(this);
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "http://202.196.240.43/jwweb/xsxj/Stu_MyInfo.aspx");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.loadUrl(IpUtil.get_Student_Info, header);
    }

    @OnClick(R.id.student_info_toolbar_left)
    public void onClick() {
        finish();
    }

}
