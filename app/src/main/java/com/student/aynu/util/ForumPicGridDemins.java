package com.student.aynu.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * 修改recycler的边距
 * Created by Administrator on 2016/9/5 0005.
 */
public class ForumPicGridDemins extends RecyclerView.ItemDecoration {

    private int space;

    public ForumPicGridDemins(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            if (i == 0) {
                params.setMargins(space, space, space / 2, space);
            } else if (i == 1) {
                params.setMargins(space / 2, space, space / 2, space);
            } else {
                params.setMargins(space / 2, space, space, space);
            }
            child.setLayoutParams(params);
        }
    }

}
