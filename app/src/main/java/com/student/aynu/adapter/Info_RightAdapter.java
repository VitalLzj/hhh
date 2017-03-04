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
import com.student.aynu.bean.Info_Array;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class Info_RightAdapter extends RecyclerView.Adapter<Info_RightAdapter.MyViewHolder> {

    private List<Info_Array> mLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private onItemClick onInfoItemClickListener;

    public Info_RightAdapter(Context mContext, List<Info_Array> mLists) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.fragment_info_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitle.setText(mLists.get(position).getNews_title());
        holder.mTime.setText(mLists.get(position).getNews_time());
        Glide.with(mContext).load(mLists.get(position).getNews_thumbnail()).placeholder(R.mipmap.ic_launcher).into(holder.mImg);
        if (onInfoItemClickListener != null) {
            holder.mGoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onInfoItemClickListener.onClick(holder.mGoLayout, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void setOnInfoItemClickListener(onItemClick onInfoItemClickListener) {
        this.onInfoItemClickListener = onInfoItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.info_recycler_title)
        TextView mTitle;
        @BindView(R.id.info_recycler_time)
        TextView mTime;
        @BindView(R.id.info_recycler_img)
        ImageView mImg;
        @BindView(R.id.info_recycler_go)
        RelativeLayout mGoLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onItemClick {
        void onClick(View v, int position);
    }

}
