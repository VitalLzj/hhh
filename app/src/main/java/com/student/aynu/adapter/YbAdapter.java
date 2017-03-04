package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.student.aynu.R;
import com.student.aynu.bean.Yb_entity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class YbAdapter extends RecyclerView.Adapter<YbAdapter.MyViewHolder> {

    private List<Yb_entity.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;

    public YbAdapter(List<Yb_entity.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.activity_yb_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mYb_name.setText(mLists.get(position).getYb_name());
        holder.mYb_bgs.setText(mLists.get(position).getYb_office());
        holder.mYb_phone.setText(mLists.get(position).getYb_phone());

//        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(getScreenWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
//        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(getScreenWidth() / 2, LinearLayout.LayoutParams.MATCH_PARENT);
//        if (position % 2 == 0) {
//            //view全显示
//            holder.mView.setLayoutParams(params1);
//        } else {
//            holder.mView.setLayoutParams(params2);
//        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.yb_text1)
        TextView mYb_name;
        @BindView(R.id.yb_text2)
        TextView mYb_bgs;
        @BindView(R.id.yb_text3)
        TextView mYb_phone;
        @BindView(R.id.yb_view)
        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        return (int) width;
    }

    //刷新
    public void refresh(List<Yb_entity.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<Yb_entity.DataBean> mLists) {
        this.mLists = (mLists);
    }
}
