package com.student.aynu.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by lzj on 2016/12/24 0024.
 * 邮箱：976623696@qq.com
 */
public abstract class BaseViewpagerFragment extends BaseFragment {

    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据

    private View convertView;
    private SparseArray<View> mViews;

    public Context mContext;

    //新闻类型
    public String mNews_Type;
    //新闻索引
    public String mNav;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mNews_Type = arguments.getString("TYPE");
            mNav=arguments.getString("NAV");
        }

        if (convertView == null) {
            mContext = getActivity();
            convertView = inflater.inflate(getLayoutId(), container, false);
            mViews = new SparseArray<>();
            initView();
            isInitView = true;
            lazyLoadData();
        } else {
            ViewGroup viewGroup = (ViewGroup) convertView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(convertView);
        }
        return convertView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        LogUtil.m("context" + "   " + this.getClass().getSimpleName());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (isFirstLoad) {
//            Log.d("第一次加载 " + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        } else {
//            LogUtil.m("不是第一次加载" + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
//            LogUtil.m("不加载" + "   " + this.getClass().getSimpleName());
            return;
        }

//        LogUtil.m("完成数据第一次加载");
        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     *
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findView(int viewId) {
        if (convertView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
    }
}
