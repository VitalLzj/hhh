package com.student.aynu.webView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻
 */
@SuppressLint("SetJavaScriptEnabled")
public class InfoWebViewActivity extends BaseActivity {

    @BindView(R.id.info_WebView_web)
    WebView mWebView;
    private Context mContext;
    //标题
    @BindView(R.id.info_WebView_yx_name)
    TextView mYXName;
    //院系链接
    private String mContent_url = null;
    //院系名字
    private String mName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_info);
        this.mContext = this;
        ButterKnife.bind(this);

        initData();

    }

    //初始化数据
    private void initData() {
        mContent_url = getIntent().getStringExtra("NEWS_URL");
        mName = getIntent().getStringExtra("NEWS_TYPE");

        if (mContent_url == null || mName == null ) {
            ToastUtil.showFaliureToast(mContext, "加载数据失败，请重新进入");
            return;
        }

        mYXName.setText(mName);

        mWebView.loadUrl(mContent_url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(new webViewClient());
    }

    //返回键
    @OnClick(R.id.info_WebView_back)
    public void onBackClick() {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        finish();
        return false;
    }

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
}
