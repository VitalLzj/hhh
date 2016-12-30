package com.student.aynu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.student.aynu.R;

/**
 * 自定义dialog
 * Created by zzz on 2016/11/23 0023.
 */
public class MyDialog {
    public static Dialog createLoadingDialog(Context context, String msg) {


        final View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);

        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) view.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog selectorDialog = new Dialog(context, R.style.selectorDialog);
        selectorDialog.setContentView(view);

        selectorDialog.show();
        selectorDialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = selectorDialog.getWindow().getAttributes();
        lp.width = 150;
        lp.height = 150;
        lp.alpha = 0.2f;
        selectorDialog.getWindow().setAttributes(lp);

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.dialog, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//        // main.xml中的ImageView
//        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//        // 加载动画
//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.loading_animation);
//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//
//        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return selectorDialog;
    }
}
