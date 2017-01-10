/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.student.aynu.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.student.aynu.application.MyApp;
import com.student.aynu.nohttp.CallServer;
import com.student.aynu.nohttp.HttpListener;
import com.yolanda.nohttp.rest.Request;


public abstract class BaseActivity extends AppCompatActivity {

    public MyApp mApplication;
    public Gson gson;
    public long lastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lastClickTime = 0;
        gson = new Gson();
        mApplication = (MyApp) getApplication();
        mApplication.init(this); // 把 this 传过去，，这里主要是要 得到当前的或者是出错的activity的包名和类名
        // 其实这里也可以，把获取的数据，，传过去，，不用传对象；；；
        mApplication.addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApplication.uname_token = getSharedPreferences("TOKEN", MODE_PRIVATE).getString("token", "");
    }

    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, canCancel, isLoading);
    }

    //检测用户是否登录
    public boolean checkIsLogin(Context context) {
        String token = context.getSharedPreferences("TOKEN", 0).getString("token", null);
        if (token == null) {
            return false;
        } else {
            mApplication.uname_token = token;
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
    }
}
