package com.edu.pu.cs.couponapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.edu.pu.cs.couponapp.ui.TabShow;

import com.github.anzewei.parallaxbacklayout.ParallaxBackActivityHelper;
import com.github.anzewei.parallaxbacklayout.ParallaxBackLayout;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.reflect.Field;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2016/11/20.
 */

public class FeedBackAcitivity extends AutoLayoutActivity {
    WebView webView;
    private String feedbackurl = null;
    private ParallaxBackActivityHelper mHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        StatusBarCompat.setStatusBarColor(FeedBackAcitivity.this, getResources().getColor(R.color.background_color_orange));
//        webView = (WebView) findViewById(R.id.webview);
        mHelper = new ParallaxBackActivityHelper(this);

//        mFirefeedback = new Firebase("https://coupon-ed7bf.firebaseio.com/feedback");
//        mFirefeedback.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                feedbackurl = value;
//                System.out.println(feedbackurl+"----feedback");
//                init();
//
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {}
//        });
//
//        System.out.println(feedbackurl+"----feedback222");
        SyncReference feedback = WilddogSync.getInstance().getReference("feedback/");
        feedback.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                feedbackurl = value;
                Log.e("feedback===",feedbackurl);
//                        Log.e("TAG",img1);
//                        String img1 = dataSnapshot.getValue().toString();
//                        Log.e("content===",dataSnapshot.toString());
                init();
            }
            @Override
            public void onCancelled(SyncError syncError) {}
        });


    }
    private void init(){
        webView = (WebView) findViewById(R.id.webview);
        //WebView加载web资源
        Log.e("feed====",feedbackurl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(feedbackurl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    public void backbtn(View v) {
        finish();
    }

    //@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onActivityDestroy();
    }

    public ParallaxBackLayout getBackLayout() {
        return mHelper.getBackLayout();
    }

    public void setBackEnable(boolean enable) {
        getBackLayout().setEnableGesture(enable);
    }

    public void scrollToFinishActivity() {
        mHelper.scrollToFinishActivity();
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

}
