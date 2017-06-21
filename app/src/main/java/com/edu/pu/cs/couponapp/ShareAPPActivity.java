package com.edu.pu.cs.couponapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import com.github.anzewei.parallaxbacklayout.ParallaxBackActivityHelper;
import com.github.anzewei.parallaxbacklayout.ParallaxBackLayout;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.client.receiver.WilddogAuthCastReceiver;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import mehdi.sakout.fancybuttons.FancyButton;
import qiu.niorgai.StatusBarCompat;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;

import com.wilddog.client.WilddogSync;

import java.util.Map;


/**
 * Created by Administrator on 2016/11/20.
 */

public class ShareAPPActivity extends AutoLayoutActivity {
    private ParallaxBackActivityHelper mHelper;
    private FancyButton Btn_share;
    private ImageView imageView;
    String QRcodeurl,APKurl;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shareapp);
        StatusBarCompat.setStatusBarColor(ShareAPPActivity.this, getResources().getColor(R.color.background_color_orange));
        mHelper = new ParallaxBackActivityHelper(this);
        initView();
//        mFiredownload = new Firebase("https://coupon-ed7bf.firebaseio.com/apkdownload");
//        mFiredownload.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                APKurl = dataSnapshot.getValue(String.class);
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {}
//        });
//        mFireqrcode = new Firebase("https://coupon-ed7bf.firebaseio.com/qrcode");
//        mFireqrcode.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                QRcodeurl = dataSnapshot.getValue(String.class);
//                Glide.with(ShareAPPActivity.this).load(QRcodeurl).priority( Priority.HIGH ).error(com.example.gridviewimage.R.mipmap.image_error).into(imageView);
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {}
//        });

        SyncReference download = WilddogSync.getInstance().getReference("apkdownload/");
        download.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                APKurl = dataSnapshot.getValue().toString();
//                        Log.e("TAG",img1);
//                        String img1 = dataSnapshot.getValue().toString();
//                        Log.e("content===",dataSnapshot.toString());
            }
            @Override
            public void onCancelled(SyncError syncError) {}
        });

        SyncReference qrcode = WilddogSync.getInstance().getReference("qrcode/");
        qrcode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QRcodeurl = dataSnapshot.getValue().toString();
                Glide.with(ShareAPPActivity.this).load(QRcodeurl).priority( Priority.HIGH ).error(com.example.gridviewimage.R.mipmap.image_error).into(imageView);
            }

            @Override
            public void onCancelled(SyncError error) {

            }
        });


        Btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });



    }
    public void backbtn(View v) {
        finish();
    }

    private void initView(){
        Btn_share = (FancyButton) findViewById(R.id.btn_share);
        imageView = (ImageView) findViewById(R.id.img_qrcode);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setText(APKurl);
        oks.show(this);
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
