package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.edu.pu.cs.couponapp.ui.TabShow;
import com.zhy.autolayout.AutoLayoutActivity;
//import com.nian.preferential.ui.TabShow;

import java.util.Timer;
import java.util.TimerTask;

import qiu.niorgai.StatusBarCompat;

public class OriginActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.origin);
        StatusBarCompat.translucentStatusBar(OriginActivity.this);
        Handler x = new Handler();
        x.postDelayed(new lunchhandler(), 2000);

    }

    class lunchhandler implements Runnable{

        public void run() {
            startActivity(new Intent(getApplication(),TabShow.class));
            OriginActivity.this.finish();
        }

    }
}