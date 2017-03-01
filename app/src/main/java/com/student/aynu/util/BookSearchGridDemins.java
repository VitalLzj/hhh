package com.student.aynu.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 修改recycler的边距
 * Created by Administrator on 2016/9/5 0005.
 */
public class BookSearchGridDemins extends RecyclerView.ItemDecoration {

    private int space;

    public BookSearchGridDemins(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


        //由于每行都只有2个，左边的左边距底边距,右边距/2
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = space;
            outRect.bottom = space;
            outRect.right = space / 2;
        }
        //由于每行都只有2个，右边的加右边距，左边距/2，底边距
        if (parent.getChildLayoutPosition(view) % 2 != 0) {

            outRect.left = space / 2;
            outRect.bottom = space;
            outRect.right = space;
        }
    }

}
