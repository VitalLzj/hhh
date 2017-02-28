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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.entity.Forum_Reply;
import com.student.aynu.entity.My_Forum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的帖子适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class MyReplyAdapter extends RecyclerView.Adapter<MyReplyAdapter.MyViewHolder> {

    private List<Forum_Reply.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private onDeleteListener onDeleteListener;

    public MyReplyAdapter(List<Forum_Reply.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.activity_myreply_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //获取本地以保存的头像
        String mUserHead = mContext.getSharedPreferences("TOKEN", 0).getString("user_face", "");
        //设置头像
        if (!"".equals(mUserHead)) {
            Glide.with(mContext).load(mUserHead).dontAnimate().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(holder.mHeadImg);
        }

        holder.mTimeText.setText(mLists.get(position).getFpltime());
        holder.mHfText.setText("\u3000\u3000\u3000" + mLists.get(position).getFplinfo());
        holder.mYtText.setText(mLists.get(position).getY_fContent());

        holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(holder.mDeleteImg, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void setOnDeleteListener(MyReplyAdapter.onDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.myReply_recycler_head_img)
        RoundedImageView mHeadImg;
        @BindView(R.id.myReply_recycler_head_time)
        TextView mTimeText;
        @BindView(R.id.myReply_recycler_delete)
        ImageView mDeleteImg;

        @BindView(R.id.myReply_recycler_hf_text)
        TextView mHfText;
        @BindView(R.id.myReply_recycler_yt_text)
        TextView mYtText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<Forum_Reply.DataBean> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<Forum_Reply.DataBean> mLists) {
        this.mLists = (mLists);
    }

    //删除接口
    public interface onDeleteListener {
        void onDelete(View v, int position);
    }
}
