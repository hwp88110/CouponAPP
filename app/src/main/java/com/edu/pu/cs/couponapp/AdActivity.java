package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2016/10/19.
 */

public class AdActivity extends AutoLayoutActivity {
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String adwebsite = getIntent().getStringExtra("adwebsite");
        //setContentView(R.layout.activity_main);
        //实例化WebView对象
        webview = new WebView(this);
//        //设置WebView属性，能够执行Javascript脚本
//        webview.getSettings().setJavaScriptEnabled(true);
//        try {
//            //设置打开的页面地址
//            webview.loadUrl(adwebsite);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        setContentView(webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(adwebsite);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
