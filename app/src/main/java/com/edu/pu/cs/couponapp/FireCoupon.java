package com.edu.pu.cs.couponapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;


/**
 * Created by Administrator on 2016/10/30.
 */

public class FireCoupon extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://couponapp.wilddogio.com").build();
        WilddogApp.initializeApp(this, options);


        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                Log.d("mytoken",deviceToken);
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

    }

}