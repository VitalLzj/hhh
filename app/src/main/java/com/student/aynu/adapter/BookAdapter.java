package com.student.aynu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.student.aynu.R;
import com.student.aynu.entity.Book_List;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图书检索适配器
 * Created by Administrator on 2016/9/5 0005.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private List<Book_List.DataBean> mLists;
    private LayoutInflater mInflater;
    private Context mContext;

    public BookAdapter(List<Book_List.DataBean> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_book_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getScreenWidth(), getScreenWidth());
        holder.mPicImage.setLayoutParams(params);
        Glide.with(mContext).load(mLists.get(position).getBthumb()).placeholder(R.mipmap.ic_launcher).into(holder.mPicImage);
        holder.mNameText.setText(mLists.get(position).getBname());
        List<Book_List.DataBean.BAuthorBean> mAuthors = mLists.get(position).getBAuthor();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mAuthors.size(); i++) {
            sb.append(mAuthors.get(i).getBaathor() + " ");
        }
        holder.mPersonText.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_recycler_pic)
        RoundedImageView mPicImage;
        @BindView(R.id.book_recycler_title)
        TextView mNameText;
        @BindView(R.id.book_recycler_name)
        TextView mPersonText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //刷新
    public void refresh(List<Book_List.DataBean> Lists) {
        this.mLists.clear();
        this.mLists = Lists;
    }

    //加载更多
    public void loadMore(List<Book_List.DataBean> mLists) {
        this.mLists = mLists;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        return (int) width / 2 - 24;
    }
}
