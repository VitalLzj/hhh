package com.student.aynu.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.student.aynu.R;
import com.student.aynu.bean.Book_Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 图书检索头部
 * Created by Administrator on 2016/9/6 0006.
 */
public class BookActivity_header extends LinearLayout {

    //轮播banner
    public BGABanner mBanner;
    //list存放图片
    public List<Book_Banner.DataBean> mLists = new ArrayList<>();
    private List<String> mImgLists = new ArrayList<>();

    public BookActivity_header(Context context) {
        super(context);
        init(context);
    }

    public BookActivity_header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BookActivity_header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View v = inflate(context, R.layout.activity_book_header, this);
        mBanner = (BGABanner) v.findViewById(R.id.book_banner);
        mBanner.setAutoPlayAble(true);
    }

    public void setData(List<Book_Banner.DataBean> mLists) {
        this.mLists = mLists;
        mImgLists.clear();
        for (int i = 0; i < mLists.size(); i++) {
            mImgLists.add(mLists.get(i).getBburl());
        }

        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(banner.getContext()).load(model).fitCenter().into((ImageView) view);
            }
        });
        mBanner.setAutoPlayAble(true);
        mBanner.setData(mImgLists, null);
        mBanner.setAutoPlayAble(true);

    }

}
