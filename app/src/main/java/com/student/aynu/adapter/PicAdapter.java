package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.entity.Home_News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片适配器
 * Created by Administrator on 2016/9/6 0006.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyViewHolder> {

    private List<String> mLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private onPhotoClcikListener onPhotoClcikListener;

    public PicAdapter(List<String> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.fragment_pic_recycler_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(mContext).load(mLists.get(position)).placeholder(R.mipmap.ic_launcher).into(holder.mPhoto);

        holder.mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPhotoClcikListener != null) {
                    onPhotoClcikListener.onClick(holder.mPhoto, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void setOnPhotoClcikListener(PicAdapter.onPhotoClcikListener onPhotoClcikListener) {
        this.onPhotoClcikListener = onPhotoClcikListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pic_recycler_photo)
        RoundedImageView mPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onPhotoClcikListener {
        void onClick(View v, int position);
    }

    //刷新
    public void refresh(List<String> mLists) {
        this.mLists.clear();
        this.mLists = mLists;
    }

    //加载更多
    public void loadMore(List<String> mLists) {
        this.mLists = (mLists);
    }
}
