package com.student.aynu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.student.aynu.R;
import com.student.aynu.base.BaseFragment;
import com.student.aynu.entity.Home_Title;
import com.student.aynu.entity.Home_banner;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.nohttp.JavaBeanRequest;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.ToastUtil;
import com.student.aynu.widget.ViewPagerIndicator;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class HomeFragment extends BaseFragment {

    BGABanner mBanner;
    private List<Home_banner.DataBean> mBannerList;

    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    //viewPager的标题
    private List<String> mTitles;

    private ViewPagerIndicator mIndicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(getActivity());
        mBanner = (BGABanner) v.findViewById(R.id.banner_content);
        initData();
        mViewPager = (ViewPager) v.findViewById(R.id.id_vp);
        mIndicator = (ViewPagerIndicator) v.findViewById(R.id.id_indicator);
        return v;
    }

    //定义handler，显示图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取banner成功
                case 0:
                    mBanner.setAdapter(new BGABanner.Adapter() {
                        @Override
                        public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                            Glide.with(banner.getContext()).load(model).placeholder(R.mipmap.ic_launcher).into((ImageView) view);
                        }
                    });
                    mBanner.setAutoPlayAble(true);
                    mBanner.setData(mImgs, null);
                    mBanner.setAutoPlayAble(true);
                    //轮播图点击事件
                    mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
                        @Override
                        public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
                            Log.i("TAG", "点击了第" + (position + 1) + "页");
                        }
                    });
                    break;
                //获取title成功
                case 1:
                    mIndicator.setTabItemTitles(mTitles);
                    //title的数量占时固定
                    HomeTitle1Fragment fragment1 = HomeTitle1Fragment.newInstance(mTitles.get(0),"1");
                    HomeTitle1Fragment fragment2 = HomeTitle1Fragment.newInstance(mTitles.get(1),"2");
                    HomeTitle1Fragment fragment3 = HomeTitle1Fragment.newInstance(mTitles.get(2),"3");
                    HomeTitle1Fragment fragment4 = HomeTitle1Fragment.newInstance(mTitles.get(3),"4");
                    HomeTitle1Fragment fragment5 = HomeTitle1Fragment.newInstance(mTitles.get(4),"5");
                    HomeTitle1Fragment fragment6 = HomeTitle1Fragment.newInstance(mTitles.get(5),"6");
                    HomeTitle1Fragment fragment7 = HomeTitle1Fragment.newInstance(mTitles.get(6),"7");
                    HomeTitle1Fragment fragment8 = HomeTitle1Fragment.newInstance(mTitles.get(7),"8");

                    mTabContents.add(fragment1);
                    mTabContents.add(fragment2);
                    mTabContents.add(fragment3);
                    mTabContents.add(fragment4);
                    mTabContents.add(fragment5);
                    mTabContents.add(fragment6);
                    mTabContents.add(fragment7);
                    mTabContents.add(fragment8);

                    mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
                        @Override
                        public int getCount() {
                            return mTabContents.size();
                        }

                        @Override
                        public Fragment getItem(int position) {
                            return mTabContents.get(position);
                        }
                    };
                    mViewPager.setAdapter(mAdapter);
                    mIndicator.setViewPager(mViewPager, 0);
                    break;
            }
        }
    };

    //获取数据
    public void initData() {
        mBannerList = new ArrayList<>();
        mTitles = new ArrayList<>();
        getBanner();
        getTitle();
    }


    //获取标题
    private void getTitle() {
        JavaBeanRequest<Home_Title> request = new JavaBeanRequest<>(IpUtil.getHomeTitle, Home_Title.class);
        request(0, request, callback2, false, true);
    }

    HttpListener<Home_Title> callback2 = new HttpListener<Home_Title>() {
        @Override
        public void onSucceed(int what, Response<Home_Title> response) {
            if (response.get().getCode() == 0) {
                //请求数据成功
                for (int i = 0; i < response.get().getData().size(); i++) {
                    mTitles.add(response.get().getData().get(i).getTitle_name());
                }
                mHandler.sendEmptyMessage(1);
            } else {
                ToastUtil.showFaliureToast(getActivity(), response.get().getMessage());
            }
        }

        @Override
        public void onFailed(int what, Response<Home_Title> response) {

        }
    };

    //获取banner
    private void getBanner() {
        JavaBeanRequest<Home_banner> request = new JavaBeanRequest<Home_banner>(IpUtil.getHomeBanner, Home_banner.class);
        request(0, request, callback, false, true);
    }


    List<String> mImgs = new ArrayList<>();
    HttpListener<Home_banner> callback = new HttpListener<Home_banner>() {
        @Override
        public void onSucceed(int what, Response<Home_banner> response) {
            if (response.get().getCode() == 0) {
                //请求数据成功
                mBannerList = response.get().getData();
                //将图片的链接存入list
                for (int i = 0; i < mBannerList.size(); i++) {
                    mImgs.add(mBannerList.get(i).getImg_url());
                }
                mHandler.sendEmptyMessage(0);
            } else {
                ToastUtil.showFaliureToast(getActivity(), response.get().getMessage());
            }
        }

        @Override
        public void onFailed(int what, Response<Home_banner> response) {

        }
    };
}
