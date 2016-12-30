package com.student.aynu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.entity.Info_Data;

import java.util.List;

/**
 * Created by lzj on 2016/12/26 0026.
 * 邮箱：976623696@qq.com
 */
public class Info_LeftAdapter extends BaseAdapter {


    private Context mContext;
    private List<Info_Data.DataBean> mLists;
    private LayoutInflater mInflater;
    //选择的item的position
    private int mCurPosition;

    public void setmCurPosition(int mCurPosition) {
        this.mCurPosition = mCurPosition;
    }


    public Info_LeftAdapter(Context mContext, List<Info_Data.DataBean> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.fragment_info_left_item, null);
            holder = new ViewHolder();

            holder.mText = (TextView) view.findViewById(R.id.info_left_text);
            holder.mView = view.findViewById(R.id.info_left_view);
            holder.mRela = (RelativeLayout) view.findViewById(R.id.info_left_relative);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mText.setText(mLists.get(i).getYx_name());

        //选中状态
        if (i == mCurPosition) {
            holder.mView.setVisibility(View.VISIBLE);
            holder.mText.setTextColor(Color.parseColor("#A10100"));
            holder.mRela.setBackgroundColor(Color.parseColor("#e2e2e2"));
        } else {
            holder.mView.setVisibility(View.GONE);
            holder.mText.setTextColor(Color.parseColor("#222222"));
            holder.mRela.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return view;
    }

    class ViewHolder {
        TextView mText;
        View mView;
        RelativeLayout mRela;
    }


}
