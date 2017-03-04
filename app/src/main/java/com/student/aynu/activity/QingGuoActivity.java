package com.student.aynu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.net.HttpCookie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/3/3 0003.
 * 邮箱：976623696@qq.com
 * 青果系统
 */
public class QingGuoActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.qGuo_number)
    EditText mNumEdit;
    @BindView(R.id.qGuo_password)
    EditText mPwdEdit;
    @BindView(R.id.qGuo_code)
    EditText mCodeEdit;

    @BindView(R.id.qGuo_get)
    TextView mGetText;
    @BindView(R.id.qGuo_code_img)
    ImageView mCodeImg;

    private boolean isGetCookie = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_qingguo);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //在cookie获取成功之前，无法获取验证码
        getCookie();
    }

    /**
     * 获取此次登录的cookie  以后的操作都需要这个cookie来维持，否则无法登录
     */
    private void getCookie() {
        StringRequest request = new StringRequest(IpUtil.get_Cookie, RequestMethod.GET);
        request(0, request, callback, false, false);
    }

    /**
     * 获取验证码 返回的为图片
     */
    private void getCode() {
        Request<Bitmap> request = NoHttp.createImageRequest(IpUtil.get_Code, RequestMethod.POST);
        request.addHeader("Referer", "http://202.196.240.43/jwweb/_data/home_login.aspx");
        request(0, request, bitmapCallBack, false, true);
    }

    /**
     * 检测输入是否完成
     */
    private void checkInput() {
        if (TextUtils.isEmpty(mNumEdit.getText().toString())) {
            ToastUtil.showFaliureToast(mContext, "请输入学号");
        } else if (TextUtils.isEmpty(mPwdEdit.getText().toString())) {
            ToastUtil.showFaliureToast(mContext, "请输入密码");
        } else if (TextUtils.isEmpty(mCodeEdit.getText().toString())) {
            ToastUtil.showFaliureToast(mContext, "请输入验证码");
        } else {
            doLogin();
        }
    }

    /**
     * 进行登录
     */
    private void doLogin() {
        StringRequest request = new StringRequest(IpUtil.do_Login, RequestMethod.POST);
        request.addHeader("Referer", "http://202.196.240.43/jwweb/_data/home_login.aspx");
        HttpCookie cookie = new HttpCookie("name", "value");
        request.addHeader(cookie);
        request.set("__VIEWSTATE", "dDwtNjMyNzQ3NTkxO3Q8O2w8aTwwPjtpPDE+O2k8Mj47PjtsPHQ8cDxsPFRleHQ7PjtsPOmdkuaenOi9r+S7tuWtpumZojs+Pjs7Pjt0PHA8bDxUZXh0Oz47bDxcPHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiXD4KXDwhLS0KZnVuY3Rpb24gQ2hrVmFsdWUoKXsKIHZhciB2VT0kKCdVSUQnKS5pbm5lckhUTUxcOwogdlU9dlUuc3Vic3RyaW5nKDAsMSkrdlUuc3Vic3RyaW5nKDIsMylcOwogdmFyIHZjRmxhZyA9ICJZRVMiXDsgaWYgKCQoJ3R4dF9hc21jZGVmc2Rkc2QnKS52YWx1ZT09JycpewogYWxlcnQoJ+mhu+W9leWFpScrdlUrJ++8gScpXDskKCd0eHRfYXNtY2RlZnNkZHNkJykuZm9jdXMoKVw7cmV0dXJuIGZhbHNlXDsKfQogZWxzZSBpZiAoJCgndHh0X3Bld2Vyd2Vkc2Rmc2RmZicpLnZhbHVlPT0nJyl7CiBhbGVydCgn6aG75b2V5YWl5a+G56CB77yBJylcOyQoJ3R4dF9wZXdlcndlZHNkZnNkZmYnKS5mb2N1cygpXDtyZXR1cm4gZmFsc2VcOwp9CiBlbHNlIGlmICgkKCd0eHRfc2RlcnRmZ3NhZHNjeGNhZHNhZHMnKS52YWx1ZT09JycgJiYgdmNGbGFnID09ICJZRVMiKXsKIGFsZXJ0KCfpobvlvZXlhaXpqozor4HnoIHvvIEnKVw7JCgndHh0X3NkZXJ0ZmdzYWRzY3hjYWRzYWRzJykuZm9jdXMoKVw7cmV0dXJuIGZhbHNlXDsKfQogZWxzZSB7ICQoJ2RpdkxvZ05vdGUnKS5pbm5lckhUTUw9J1w8Zm9udCBjb2xvcj0icmVkIlw+5q2j5Zyo6YCa6L+H6Lqr5Lu96aqM6K+BLi4u6K+356iN5YCZIVw8L2ZvbnRcPidcOwogICBkb2N1bWVudC5nZXRFbGVtZW50QnlJZCgidHh0X3Bld2Vyd2Vkc2Rmc2RmZiIpLnZhbHVlID0gJydcOwogZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoInR4dF9zZGVydGZnc2Fkc2N4Y2Fkc2FkcyIpLnZhbHVlID0gJydcOyAKIHJldHVybiB0cnVlXDt9CiB9CmZ1bmN0aW9uIFNlbFR5cGUob2JqKXsKIHZhciBzPW9iai5vcHRpb25zW29iai5zZWxlY3RlZEluZGV4XS5nZXRBdHRyaWJ1dGUoJ3VzcklEJylcOwogJCgnVUlEJykuaW5uZXJIVE1MPXNcOwogc2VsVHllTmFtZSgpXDsKIGlmKG9iai52YWx1ZT09IlNUVSIpIHsKICAgZG9jdW1lbnQuYWxsLmJ0bkdldFN0dVB3ZC5zdHlsZS5kaXNwbGF5PScnXDsKICAgZG9jdW1lbnQuYWxsLmJ0blJlc2V0LnN0eWxlLmRpc3BsYXk9J25vbmUnXDsKICB9CiBlbHNlIHsKICAgIGRvY3VtZW50LmFsbC5idG5SZXNldC5zdHlsZS5kaXNwbGF5PScnXDsKICAgIGRvY3VtZW50LmFsbC5idG5HZXRTdHVQd2Quc3R5bGUuZGlzcGxheT0nbm9uZSdcOwogIH19CmZ1bmN0aW9uIG9wZW5XaW5Mb2codGhlVVJMLHcsaCl7CnZhciBUZm9ybSxyZXRTdHJcOwpldmFsKCJUZm9ybT0nd2lkdGg9Iit3KyIsaGVpZ2h0PSIraCsiLHNjcm9sbGJhcnM9bm8scmVzaXphYmxlPW5vJyIpXDsKcG9wPXdpbmRvdy5vcGVuKHRoZVVSTCwnd2luS1BUJyxUZm9ybSlcOyAvL3BvcC5tb3ZlVG8oMCw3NSlcOwpldmFsKCJUZm9ybT0nZGlhbG9nV2lkdGg6Iit3KyJweFw7ZGlhbG9nSGVpZ2h0OiIraCsicHhcO3N0YXR1czpub1w7c2Nyb2xsYmFycz1ub1w7aGVscDpubyciKVw7CnBvcC5tb3ZlVG8oKHNjcmVlbi53aWR0aC13KS8yLChzY3JlZW4uaGVpZ2h0LWgpLzIpXDtpZih0eXBlb2YocmV0U3RyKSE9J3VuZGVmaW5lZCcpIGFsZXJ0KHJldFN0cilcOwp9CmZ1bmN0aW9uIHNob3dMYXkoZGl2SWQpewp2YXIgb2JqRGl2ID0gZXZhbChkaXZJZClcOwppZiAob2JqRGl2LnN0eWxlLmRpc3BsYXk9PSJub25lIikKe29iakRpdi5zdHlsZS5kaXNwbGF5PSIiXDt9CmVsc2V7b2JqRGl2LnN0eWxlLmRpc3BsYXk9Im5vbmUiXDt9Cn0KZnVuY3Rpb24gc2VsVHllTmFtZSgpewogICQoJ3R5cGVOYW1lJykudmFsdWU9JE4oJ1NlbF9UeXBlJylbMF0ub3B0aW9uc1skTignU2VsX1R5cGUnKVswXS5zZWxlY3RlZEluZGV4XS50ZXh0XDsKfQp3aW5kb3cub25sb2FkPWZ1bmN0aW9uKCl7Cgl2YXIgc1BDPU1TSUU/d2luZG93Lm5hdmlnYXRvci51c2VyQWdlbnQrd2luZG93Lm5hdmlnYXRvci5jcHVDbGFzcyt3aW5kb3cubmF2aWdhdG9yLmFwcE1pbm9yVmVyc2lvbisnIFNOOk5VTEwnOndpbmRvdy5uYXZpZ2F0b3IudXNlckFnZW50K3dpbmRvdy5uYXZpZ2F0b3Iub3NjcHUrd2luZG93Lm5hdmlnYXRvci5hcHBWZXJzaW9uKycgU046TlVMTCdcOwp0cnl7JCgncGNJbmZvJykudmFsdWU9c1BDXDt9Y2F0Y2goZXJyKXt9CnRyeXskKCd0eHRfYXNtY2RlZnNkZHNkJykuZm9jdXMoKVw7fWNhdGNoKGVycil7fQp0cnl7JCgndHlwZU5hbWUnKS52YWx1ZT0kTignU2VsX1R5cGUnKVswXS5vcHRpb25zWyROKCdTZWxfVHlwZScpWzBdLnNlbGVjdGVkSW5kZXhdLnRleHRcO31jYXRjaChlcnIpe30KfQpmdW5jdGlvbiBvcGVuV2luRGlhbG9nKHVybCxzY3IsdyxoKQp7CnZhciBUZm9ybVw7CmV2YWwoIlRmb3JtPSdkaWFsb2dXaWR0aDoiK3crInB4XDtkaWFsb2dIZWlnaHQ6IitoKyJweFw7c3RhdHVzOiIrc2NyKyJcO3Njcm9sbGJhcnM9bm9cO2hlbHA6bm8nIilcOwp3aW5kb3cuc2hvd01vZGFsRGlhbG9nKHVybCwxLFRmb3JtKVw7Cn0KZnVuY3Rpb24gb3Blbldpbih0aGVVUkwpewp2YXIgVGZvcm0sdyxoXDsKdHJ5ewoJdz13aW5kb3cuc2NyZWVuLndpZHRoLTEwXDsKfWNhdGNoKGUpe30KdHJ5ewpoPXdpbmRvdy5zY3JlZW4uaGVpZ2h0LTMwXDsKfWNhdGNoKGUpe30KdHJ5e2V2YWwoIlRmb3JtPSd3aWR0aD0iK3crIixoZWlnaHQ9IitoKyIsc2Nyb2xsYmFycz1ubyxzdGF0dXM9bm8scmVzaXphYmxlPXllcyciKVw7CnBvcD1wYXJlbnQud2luZG93Lm9wZW4odGhlVVJMLCcnLFRmb3JtKVw7CnBvcC5tb3ZlVG8oMCwwKVw7CnBhcmVudC5vcGVuZXI9bnVsbFw7CnBhcmVudC5jbG9zZSgpXDt9Y2F0Y2goZSl7fQp9CmZ1bmN0aW9uIGNoYW5nZVZhbGlkYXRlQ29kZShPYmopewp2YXIgZHQgPSBuZXcgRGF0ZSgpXDsKT2JqLnNyYz0iLi4vc3lzL1ZhbGlkYXRlQ29kZS5hc3B4P3Q9IitkdC5nZXRNaWxsaXNlY29uZHMoKVw7Cn0KZnVuY3Rpb24gY2hrcHdkKG9iaikgeyAgaWYob2JqLnZhbHVlIT0nJykgIHsgICAgdmFyIHM9bWQ1KGRvY3VtZW50LmFsbC50eHRfYXNtY2RlZnNkZHNkLnZhbHVlK21kNShvYmoudmFsdWUpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpKycxMDQ3OScpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpXDsgICBkb2N1bWVudC5hbGwuZHNkc2RzZHNkeGN4ZGZnZmcudmFsdWU9c1w7fSBlbHNlIHsgZG9jdW1lbnQuYWxsLmRzZHNkc2RzZHhjeGRmZ2ZnLnZhbHVlPW9iai52YWx1ZVw7fSB9ICBmdW5jdGlvbiBjaGt5em0ob2JqKSB7ICBpZihvYmoudmFsdWUhPScnKSB7ICAgdmFyIHM9bWQ1KG1kNShvYmoudmFsdWUudG9VcHBlckNhc2UoKSkuc3Vic3RyaW5nKDAsMzApLnRvVXBwZXJDYXNlKCkrJzEwNDc5Jykuc3Vic3RyaW5nKDAsMzApLnRvVXBwZXJDYXNlKClcOyAgIGRvY3VtZW50LmFsbC5mZ2ZnZ2ZkZ3R5dXV5eXV1Y2tqZy52YWx1ZT1zXDt9IGVsc2UgeyAgICBkb2N1bWVudC5hbGwuZmdmZ2dmZGd0eXV1eXl1dWNramcudmFsdWU9b2JqLnZhbHVlLnRvVXBwZXJDYXNlKClcO319Ly8tLVw+Clw8L3NjcmlwdFw+Oz4+Ozs+O3Q8O2w8aTwxPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8bDxUZXh0Oz47bDxcPG9wdGlvbiB2YWx1ZT0nU1RVJyB1c3JJRD0n5a2m44CA5Y+3J1w+5a2m55SfXDwvb3B0aW9uXD4KXDxvcHRpb24gdmFsdWU9J1RFQScgdXNySUQ9J+W3peOAgOWPtydcPuaVmeW4iOaVmei+heS6uuWRmFw8L29wdGlvblw+Clw8b3B0aW9uIHZhbHVlPSdTWVMnIHVzcklEPSfluJDjgIDlj7cnXD7nrqHnkIbkurrlkZhcPC9vcHRpb25cPgpcPG9wdGlvbiB2YWx1ZT0nQURNJyB1c3JJRD0n5biQ44CA5Y+3J1w+6Zeo5oi357u05oqk5ZGYXDwvb3B0aW9uXD4KOz4+Ozs+Oz4+Oz4+Oz4+Oz4Rt4tAa6w4nw4/YJcvAc5fhxtPzA==");
        request.set("pcInfo", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0Windows NT 10.0; WOW645.0 (Windows) SN:NULL");
        request.set("typeName", "ѧ��");
        request.set("dsdsdsdsdxcxdfgfg", getDs());
        request.set("fgfggfdgtyuuyyuuckjg", getFg());
        request.set("Sel_Type", "STU");
        request.set("txt_asmcdefsddsd", mNumEdit.getText().toString());
        request.set("txt_pewerwedsdfsdff", "");
        request.set("txt_sdertfgsadscxcadsads", "");
        request(1, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String responseInfo = response.get();
            responseInfo = responseInfo.substring(1, 7);
            switch (what) {
                case 0:
                    //cookie获取成功
                    isGetCookie = true;
                    break;
                case 1:
                    Log.d("tag", responseInfo);
                    if (responseInfo.equals("script")) {
                        //登陆成功
                        startActivity(new Intent(mContext, StudentActivity.class));
                    } else {
                        ToastUtil.showFaliureToast(mContext, "登录失败,请检查输入内容");
                    }
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    HttpListener<Bitmap> bitmapCallBack = new HttpListener<Bitmap>() {
        @Override
        public void onSucceed(int what, Response<Bitmap> response) {
            final Bitmap bitmap = response.get();
            switch (what) {
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                mCodeImg.setImageBitmap(bitmap);
                                mGetText.setText("看不清");
                            }
                        }
                    });
                    break;
            }

        }

        @Override
        public void onFailed(int what, Response<Bitmap> response) {

        }
    };

    @OnClick({R.id.qGuo_toolbar_left, R.id.qGuo_get, R.id.qGuo_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qGuo_toolbar_left:
                finish();
                break;
            case R.id.qGuo_get:
                if (isGetCookie) {
                    getCode();
                } else {
                    ToastUtil.showFaliureToast(mContext, "正在获取cookie...");
                }
                break;
            case R.id.qGuo_login:
                checkInput();
                break;
        }
    }

    /**
     * 用户名密码加密
     *
     * @return 加密后的字符串
     */
    public String getDs() {
        StringBuffer textString = new StringBuffer();
        textString.append(mNumEdit.getText().toString());
        textString.append(Sha1Util.encode(mPwdEdit.getText().toString()).substring(0, 30).toUpperCase()
                + "10479");
        String pwdString = Sha1Util.encode(textString.toString()).substring(0, 30)
                .toUpperCase();
        return pwdString;
    }

    /**
     * 验证码加密
     *
     * @return 加密后的验证码
     */
    public String getFg() {
        StringBuffer codeBuffer = new StringBuffer();
        codeBuffer.append(mCodeEdit.getText().toString());
        StringBuffer buff2 = new StringBuffer();
        buff2.append(Sha1Util.encode(codeBuffer.toString().toUpperCase()).substring(0,
                30).toUpperCase());
        buff2.append("10479");
        String code = Sha1Util.encode(buff2.toString()).substring(0, 30).toUpperCase();
        return code;
    }
}
