package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.student.aynu.R;
import com.student.aynu.bean.Book_Sc;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图书收藏适配器
 * Created by Administrator on 2016/9/5 0005.
 */
public class BookScAdapter extends RecyclerView.Adapter<BookScAdapter.MyViewHolder> {

    private List<Book_Sc.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private onDeleteListener onDeleteListener;

    public BookScAdapter(List<Book_Sc.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_sc_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(mContext).load(mLists.get(position).getB_thumb()).placeholder(R.mipmap.ic_launcher).into(holder.mPicImage);
        holder.mNameText.setText(mLists.get(position).getB_name());
        holder.mQuiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener != null) {
                    onDeleteListener.onQuite(holder.mQuiteLayout, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void setOnDeleteListener(BookScAdapter.onDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sc_recycler_head_img)
        ImageView mPicImage;
        @BindView(R.id.sc_recycler_title)
        TextView mNameText;
        @BindView(R.id.sc_recycler_quite)
        RelativeLayout mQuiteLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<Book_Sc.DataBean> Lists) {
        this.mLists.clear();
        this.mLists = Lists;
    }

    //加载更多
    public void loadMore(List<Book_Sc.DataBean> mLists) {
        this.mLists = mLists;
    }

    public interface onDeleteListener {
        void onQuite(View v, int position);
    }

}
