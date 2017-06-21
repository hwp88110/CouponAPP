package com.edu.pu.cs.couponapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anzewei.parallaxbacklayout.ParallaxBackActivityHelper;
import com.github.anzewei.parallaxbacklayout.ParallaxBackLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import org.w3c.dom.Text;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2016/11/20.
 */

public class AboutActivity extends AutoLayoutActivity {
    TextView Feed,Shareapp,Buildnum;
    LinearLayout Update;
    private ParallaxBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        StatusBarCompat.setStatusBarColor(AboutActivity.this, getResources().getColor(R.color.background_color_orange));
        mHelper = new ParallaxBackActivityHelper(this);
        initView();
        //监听三个按钮
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutActivity.this, "当前已是最新版本，若有更新会消息推送，请注意查收", Toast.LENGTH_SHORT).show();
            }
        });
        Feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AboutActivity.this, FeedBackAcitivity.class);
                startActivity(it);
            }
        });
        Shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AboutActivity.this, ShareAPPActivity.class);
                startActivity(it);
            }
        });
        //设置版本号
        String versionName = BuildConfig.VERSION_NAME;
        Buildnum.setText(versionName);
    }
    public void backbtn(View v) {
        finish();
    }

    private void initView(){
        Feed = (TextView) findViewById(R.id.feed);
        Shareapp = (TextView) findViewById(R.id.shareapp);
        Update = (LinearLayout) findViewById(R.id.update);
        Buildnum = (TextView) findViewById(R.id.text_buildnum);
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

