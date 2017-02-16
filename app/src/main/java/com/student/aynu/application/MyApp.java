package com.student.aynu.application;

import android.app.Activity;
import android.app.Application;

import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.util.ArrayList;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");// 设置NoHttp打印Log的tag。

        // 一般情况下你只需要这样初始化：
        NoHttp.initialize(this, new NoHttp.Config()
                .setNetworkExecutor(new OkHttpNetworkExecutor())
                .setConnectTimeout(5 * 1000)
                .setReadTimeout(5 * 1000));
    }

    ArrayList<Activity> list = new ArrayList<Activity>();

    public String uname_token = ""; // 令牌

    public void init(Activity myActivity) {
        // 设置该CrashHandler为程序的默认处理器
        AsExceptionHandler catchExcep = new AsExceptionHandler(this, myActivity);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }


    /**
     * 关闭Activity列表中的后俩Activity
     */
    public void finishLastTwoActivity() {
        for (int i = list.size() - 1; i > list.size() - 3; i--) {
            if (null != list.get(i)) {
                list.get(i).finish();
            }
        }
    }

    /**
     * 关闭Activity列表中的其他Activity
     */
    public void finishOtherActivity() {
        for (int i = 0; i < list.size() - 1; i++) {
            if (null != list.get(i)) {
                list.get(i).finish();
            }
        }
    }

    /**
     * 关闭Activity列表中除了mainActivity的其他Activity
     */
    public void finishOtherActivityExceptMain() {
        for (int i = 2; i < list.size() - 1; i++) {
            if (null != list.get(i)) {
                list.get(i).finish();
            }
        }
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
