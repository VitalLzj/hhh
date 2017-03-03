package com.student.aynu.webView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import com.yolanda.nohttp.NoHttp;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/21.
 * 可以附带cookie header的webView
 */
public class MyWebView extends android.webkit.WebView {

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public void loadUrl(String url, Map<String, String> httpHeader) {
        if (httpHeader == null) {
            httpHeader = new HashMap<>();
        }

        // 这里你还可以添加一些自定头。
        httpHeader.put("AppVersion", "1.0.0"); // 比如添加app版本信息，当然实际开发中要自动获取哦。

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri != null) {
            java.net.CookieStore cookieStore = NoHttp.getCookieManager().getCookieStore();
            List<HttpCookie> cookies = cookieStore.get(uri);

            // 同步到WebView。
            android.webkit.CookieManager webCookieManager = android.webkit.CookieManager.getInstance();
            webCookieManager.setAcceptCookie(true);
            for (HttpCookie cookie : cookies) {
                String cookieUrl = cookie.getDomain();
                String cookieValue = cookie.getName() + "=" + cookie.getValue()
                        + "; path=" + cookie.getPath()
                        + "; domain=" + cookie.getDomain();

                webCookieManager.setCookie(cookieUrl, cookieValue);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                webCookieManager.flush();
            } else {
                android.webkit.CookieSyncManager.createInstance(NoHttp.getContext()).sync();
            }
        }
        super.loadUrl(url, httpHeader);
    }

}