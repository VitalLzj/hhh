package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.entity.Forum_Pl;
import com.student.aynu.entity.My_Forum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class MyForumAdapter extends RecyclerView.Adapter<MyForumAdapter.MyViewHolder> {

    private List<My_Forum.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private onDeleteListener onDeleteListener;

    public MyForumAdapter(List<My_Forum.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.activity_myforum_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mContentText.setText(mLists.get(position).getFcontent());
        holder.mHuiFuNumText.setText(mLists.get(position).getFping_num());
        holder.mZanNumText.setText(mLists.get(position).getFzan_num());

        holder.mDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(holder.mDeleteLayout, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void setOnDeleteListener(MyForumAdapter.onDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.myForum_recycler_title)
        TextView mContentText;
        @BindView(R.id.myForum_Recycler_delete)
        RelativeLayout mDeleteLayout;
        @BindView(R.id.myForum_recycler_bottom_huifu_num)
        TextView mHuiFuNumText;
        @BindView(R.id.myForum_recycler_bottom_zan_num)
        TextView mZanNumText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<My_Forum.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<My_Forum.DataBean> mLists) {
        this.mLists = (mLists);
    }

    //删除接口
    public interface onDeleteListener {
        void onDelete(View v, int position);
    }
}
