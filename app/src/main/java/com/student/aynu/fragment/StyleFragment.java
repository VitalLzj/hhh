package com.student.aynu.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 校园风采
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class StyleFragment extends BaseFragment {

    private Context mContext;

    //左侧区域
    @BindView(R.id.style_text_left)
    TextView mLeftText;
    @BindView(R.id.style_view_left)
    View mLeftView;

    //右侧区域
    @BindView(R.id.style_text_right)
    TextView mRightText;
    @BindView(R.id.style_view_right)
    View mRightView;

    //ViewPager
    @BindView(R.id.style_viewPager)
    ViewPager mViewPager;

    FragmentPagerAdapter mAdapter;
    List<Fragment> mFragmentLists;

    Fragment mLeftFragment = null, mRightFragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_find, null);
        ButterKnife.bind(this, v);
        initData();
        setSelect(0);
        return v;
    }

    private void initData() {
        mFragmentLists = new ArrayList<>();

        mLeftFragment = new PicFragment();
        mRightFragment = new VideoFragment();

        mFragmentLists.add(mLeftFragment);
        mFragmentLists.add(mRightFragment);

        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentLists.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int i) {
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    /**
     * @param currentItem 当前索引
     *                    修改状态
     */
    private void setTab(int currentItem) {
        resetViews();
        switch (currentItem) {
            case 0:
                mLeftText.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                mLeftView.setVisibility(View.VISIBLE);
                break;
            case 1:
                mRightText.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                mRightView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.style_relayout_left, R.id.style_relayout_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.style_relayout_left:
                setSelect(0);
                break;
            case R.id.style_relayout_right:
                setSelect(1);
                break;
        }
    }

    /**
     * 视图还原
     */
    private void resetViews() {
        mLeftText.setTextColor(Color.parseColor("#222222"));
        mLeftView.setVisibility(View.GONE);
        mRightText.setTextColor(Color.parseColor("#222222"));
        mRightView.setVisibility(View.GONE);
    }
}
