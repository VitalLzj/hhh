package com.student.aynu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.student.aynu.adapter.StudentXnAdapter;
import com.student.aynu.adapter.StudentXqAdapter;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Student;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
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
 * Created by lzj on 2017/3/3 0003.
 * 邮箱：976623696@qq.com
 * 学生成绩
 */
public class StudentScoreActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.student_score_xn_text)
    TextView mXnText;
    @BindView(R.id.student_score_xq_text)
    TextView mXqText;
    @BindView(R.id.student_score_img)
    ImageView mScoreImg;

    private PopupWindow mXnPop, mXqPop;
    private List<Student.DataBean> mLists;
    private List<Student.DataBean.XxxxBean> mXqLists;

    //获取成绩需要拼接的参数
    private String xn, xq;

    //成绩图片链接
    private String imgSrc = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_student_score);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mLists = new ArrayList<>();
        mXqLists = new ArrayList<>();
        getStudentXn();
    }

    /**
     * 获取学年
     */
    private void getStudentXn() {
        StringRequest request = new StringRequest(IpUtil.get_Xn, RequestMethod.GET);
        request(0, request, callback, false, true);
    }

    /**
     * 进行查询
     */
    private void doSearch() {
        StringRequest request = new StringRequest(IpUtil.get_Student_Head, RequestMethod.POST);
        request.set("sel_xn", xn);
        request.set("sel_xq", xq);
        request.set("SJ", "1");
        request.set("btn_search", "����");
        request.set("SelXNXQ", "2");
        request.set("zfx_flag", "0");
        request.set("zxf", "0");
        request(1, request, callback, false, true);
    }

    HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            final String responseInfo = response.get();
            switch (what) {
                case 0:
                    Student student = gson.fromJson(responseInfo, Student.class);
                    if (student.getCode() == 0) {
                        //有数据
                        mLists = student.getData();
                    } else {
                        ToastUtil.showFaliureToast(mContext, "暂无数据");
                    }
                    break;
                case 1:
                    Document document = Jsoup.parse(responseInfo);
                    Elements elements = document.getElementsByTag("img");
                    //获取img的url
                    for (Element element : elements) {
                        imgSrc = element.attr("src");
                    }
                    if (imgSrc.equals("")) {
                        ToastUtil.showFaliureToast(mContext, "暂无成绩");
                    } else {
                        getScoreImg();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 获取成绩图片
     */
    private void getScoreImg() {
        Request<Bitmap> request = NoHttp.createImageRequest(IpUtil.get_Student_Score + imgSrc, RequestMethod.GET);
        request.addHeader("Referer", "http://202.196.240.43/jwweb/xscj/Stu_MyScore_rpt.aspx");
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
                                mScoreImg.setImageBitmap(bitmap);
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

    @OnClick({R.id.student_score_xn_choose, R.id.student_score_xq_choose
            , R.id.student_score_toolbar_left, R.id.student_score_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_score_toolbar_left:
                finish();
                break;
            case R.id.student_score_xn_choose:
                showXnPop();
                break;
            case R.id.student_score_xq_choose:
                if (TextUtils.isEmpty(mXnText.getText().toString())) {
                    ToastUtil.showFaliureToast(mContext, "请选择学年");
                } else {
                    showXqPop();
                }
                break;
            case R.id.student_score_check:
                if (TextUtils.isEmpty(mXqText.getText().toString())) {
                    ToastUtil.showFaliureToast(mContext, "请选择学期");
                } else if (TextUtils.isEmpty(mXnText.getText().toString())) {
                    ToastUtil.showFaliureToast(mContext, "请选择学年");
                } else {
                    doSearch();
                }
                break;
        }
    }

    /**
     * 弹出学期选择框
     */
    private void showXqPop() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_student_xn_pop, null);
        mXqPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mXqPop.setContentView(view);

        ListView mListView = (ListView) view.findViewById(R.id.appointment_pop_list);
        StudentXqAdapter mXqAdapter = new StudentXqAdapter(mContext, mXqLists);
        mListView.setAdapter(mXqAdapter);
        //list点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mXqText.setText("学期：" + mXqLists.get(i).getXq());
                xq = i + "";
                mXqPop.dismiss();
            }
        });

        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_student_score, null);
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
     * 弹出学年选择框
     */
    private void showXnPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_student_xn_pop, null);
        mXnPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().density * 300), true);
        mXnPop.setContentView(view);

        ListView mListView = (ListView) view.findViewById(R.id.appointment_pop_list);
        StudentXnAdapter mXnAdapter = new StudentXnAdapter(mContext, mLists);
        mListView.setAdapter(mXnAdapter);
        //list点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mXnText.setText("学年：" + mLists.get(i).getXntext());
                xn = mLists.get(i).getXntext().substring(0, 4);
                mXqLists.clear();
                mXqLists = mLists.get(i).getXxxx();
                mXqText.setText("学期：请选择");
                mXnPop.dismiss();
            }
        });

        View mainView = LayoutInflater.from(mContext).inflate(R.layout.activity_student_score, null);
        // 点击外部消失
        mXnPop.setBackgroundDrawable(new BitmapDrawable());
        mXnPop.setOutsideTouchable(true);
        //动画加显示
        mXnPop.setAnimationStyle(R.style.animation_bottom);
        mXnPop.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        mXnPop.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        mXnPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

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
        if (bitmap!=null){
            bitmap.recycle();
        }
        super.onDestroy();
    }
}
