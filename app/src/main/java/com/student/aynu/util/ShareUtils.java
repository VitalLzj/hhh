package com.student.aynu.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzz on 2016/11/28 0028.
 */
public class ShareUtils {
    public static void share(Context context, String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 查询所有可以分享的Activity
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        Log.d("tag", resInfo.size() + "");
        if (resInfo.size() > 0) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resInfo) {
                Intent targeted = new Intent(Intent.ACTION_SEND);
                targeted.setType("text/plain");
                ActivityInfo activityInfo = info.activityInfo;
                Log.v("logcat", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name);

                // 分享出去的内容
                targeted.putExtra(Intent.EXTRA_TEXT, "来自【安师说】分享:" + msg);
                // 分享出去的标题
                targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
                targeted.setPackage(activityInfo.packageName);
                targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                PackageManager pm = context.getApplicationContext().getPackageManager();
                // 微信有2个怎么区分-。- 朋友圈还有微信
                if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("微信")
                        && !info.loadLabel(pm).toString().contains("添加到微信收藏") ||
                        info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("QQ") &&
                                info.loadLabel(pm).toString().contains("发送给好友") ||
                        info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("微博") &&
                                info.loadLabel(pm).toString().contains("微博") ||
                        info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("QQ空间")) {
                    targetedShareIntents.add(targeted);
                }
            }
            // 选择分享时的标题
            if (targetedShareIntents.size() > 0) {
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "选择分享");
                if (chooserIntent == null) {
                    return;
                }
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                try {
                    context.startActivity(chooserIntent);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(context, "找不到该分享应用组件", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "找不到可分享的应用组件", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
