package com.student.aynu.application;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.student.aynu.db.DB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//异常处理

public class AsExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CatchExcep";
    MyApp application;
    Activity myActivity;

    private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
//    private SimpleDateFormat format = new SimpleDateFormat(
//            "yyyy-MM-dd_HH.mm.ss");// 用于格式化日期,作为日志文件名的一部分

    public AsExceptionHandler(MyApp application,
                              Activity myActivity) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
        this.myActivity = myActivity;

        Class<?> cxx = myActivity.getClass();

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }

//            Intent intent = new Intent(application.getApplicationContext(),
//                    MainActivity.class);
//            PendingIntent restartIntent = PendingIntent.getActivity(
//                    application.getApplicationContext(), 0, intent,
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
            // 退出程序
//            AlarmManager mgr = (AlarmManager) application
//                    .getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                    restartIntent); // 1秒钟后重启应用
            application.finishActivity();

        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(application.getApplicationContext(),
                        "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        collectDeviceInfo(application);
        // 保存日志文件
        saveCrashInfo2File(ex, application);

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity

            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";

                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
                info.put("my_name", "...");

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                Log.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // sqlite数据库
    private DB db;

    private String saveCrashInfo2File(Throwable ex, Context context) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");

            Log.e(key, value);

        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();

        Log.e("错误日志", result);

        sb.append(result);

        // 保存错误日志到本地
        // 获取当时时间
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);

        db = new DB(context);
        // 写
        SQLiteDatabase dbwrite = db.getWritableDatabase();

        //
        ContentValues cv = new ContentValues();
        cv.put("text", sb.toString());
        cv.put("time", time);

        // 写入
        dbwrite.insert("record", null, cv);
        // 关闭
        dbwrite.close();

        return null;
    }

}