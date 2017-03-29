package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.adapter.KlassAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.bean.Kclass;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzj on 2017/3/4 0004.
 * 邮箱：976623696@qq.com
 * 学生课表
 */
public class StudentClassActivity extends BaseActivity {
    private Context mContext;

    @BindView(R.id.student_class_xq_text)
    TextView mXqText;
    @BindView(R.id.student_class_img)
    ImageView mClassImg;
    private List<Kclass.DataBean> mLists;
    private PopupWindow mXqPop;

    private String xn, xq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_student_class);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        getCode();
    }

    /**
     * 首先获取加密后的验证码
     */
    private void getCode() {
        StringRequest request = new StringRequest(IpUtil.get_Class_code, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    /**
     * 获取学期
     */
    private void getXq() {
        mLists = new ArrayList<>();
        StringRequest request = new StringRequest(IpUtil.get_Class_xq, RequestMethod.GET);
        request(1, request, callback, false, true);
    }

    /**
     * 进行检索课表 post方式传参数，获取图片地址
     * 这个m是个随机数，可以固定表示
     */
    private void doSearch() {
        StringRequest request = new StringRequest(IpUtil.get_Class_url + "m=lzj", RequestMethod.POST);
        request.set("txt_yzm", "");
        request.set("Sel_XNXQ", xn + xq);
        request.set("rad", "1");
        request.set("px", "0");
        request.set("hidyzm", code);
        request.set("hidsjyzm", getHidjyzm());
        request(2, request, callback, false, true);
    }

    private String getHidjyzm() {
        String sBuffer = "10479" +
                xn +
                xq +
                "lzj";
        return Sha1Util.encode(sBuffer).toUpperCase();
    }

    //加密后的验证码
    private String code = "";
    //成绩的图片
    private String imgSrc = "";
    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            final String responseInfo = response.get();
            switch (what) {
                case 0:
                    Document document = Jsoup.parse(responseInfo);
                    //获取加密后的验证码
                    code = document.select("input[name=hidyzm]").val();
                    if (code.equals("")) {
                        ToastUtil.showFaliureToast(mContext, "获取后台加密字符串出错，无法继续进行");
                    } else {
                        getXq();
                    }
                    break;
                case 1:
                    Kclass klass = gson.fromJson(responseInfo, Kclass.class);
                    mLists = klass.getData();
                    break;
                case 2:
                    imgSrc = "";
                    Document document2 = Jsoup.parse(responseInfo);
                    Elements elements = document2.getElementsByTag("img");
                    //获取img的url
                    for (Element element : elements) {
                        imgSrc = element.attr("src");
                    }
                    if (imgSrc.equals("")) {
                        ToastUtil.showFaliureToast(mContext, "暂无成绩");
                    } else {
                        getClassImg();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 获取课表
     */
    private void getClassImg() {
        Request<Bitmap> request = NoHttp.createImageRequest(IpUtil.get_Class_img + imgSrc, RequestMethod.GET);
        request.addHeader("Referer", "http://202.196.240.43/jwweb/znpk/Pri_StuSel_rpt.aspx?m=lzj");
        request(0, request, bitmapCallBack, false, true);
    }

    Bitmap bitmap = null;
    HttpListener<Bitmap> bitmapCallBack = new HttpListener<Bitmap>() {
        @Override
        public void onSucceed(int what, Response<Bitmap> response) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            bitmap = response.get();
            switch (what) {
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                mClassImg.setImageBitmap(bitmap);
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

    @OnClick({R.id.student_class_toolbar_left, R.id.student_class_xq_choose, R.id.student_class_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_class_toolbar_left:
                finish();
                break;
            case R.id.student_class_xq_choose:
                showXqPop();
                break;
            case R.id.student_class_check:
                doSearch();
                break;
        }
    }

    private void showXqPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_student_xn_pop, null);
        mXqPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().density * 300), true);
        mXqPop.setContentView(view);

        ListView mListView = (ListView) view.findViewById(R.id.appointment_pop_list);
        KlassAdapter mXqAdapter = new KlassAdapter(mContext, mLists);
        mListView.setAdapter(mXqAdapter);
        //list点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mXqText.setText("学年学期：" + mLists.get(i).getCname());
                xn = mLists.get(i).getCname().substring(0, 4);
                xq = mLists.get(i).getCname().substring(12, 13);
                Log.d("tag", xn + "-" + xq);
                if (xq.equals("一")) {
                    xq = "0";
                } else {
                    xq = "1";
                }
                mXqPop.dismiss();
            }
        });

        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_student_class, null);
        // 点击外部消失
        mXqPop.setBackgroundDrawable(new BitmapDrawable());
        mXqPop.setOutsideTouchable(true);
        //动画加显示
        mXqPop.setAnimationStyle(R.style.animation_bottom);
        mXqPop.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mXqPop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mXqPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
        }
        super.onDestroy();
    }
}
