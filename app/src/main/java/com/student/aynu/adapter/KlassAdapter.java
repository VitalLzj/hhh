package com.student.aynu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.bean.Kclass;

import java.util.List;

/**
 * 成绩学期
 * Created by Administrator on 2016/9/7 0007.
 */
public class KlassAdapter extends BaseAdapter {

    private List<Kclass.DataBean> mLists;
    private LayoutInflater mInflater;

    public KlassAdapter(Context mContext, List<Kclass.DataBean> mLists) {
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
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.activity_student_xn_pop_item, null);
            holder = new ViewHolder();

            holder.mText = (TextView) view.findViewById(R.id.appointment_pop_list_text);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mText.setText(mLists.get(i).getCname());
        return view;
    }

    class ViewHolder {
        TextView mText;
    }
}
