package com.student.aynu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取版本信息
 * Created by zzz on 2016/10/27 0027.
 */
public class VersionUtil {

    private static int mVersionCode;
    private static String mVersionName;

    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
            mVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionName + "(" + mVersionCode + ")";
    }

    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            mVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionCode;
    }
}
