package com.edu.pu.cs.couponapp;

/**
 * Created by Administrator on 2016/11/16.
 */

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class VibratorHelper {
    public static boolean Vibrate(final Activity activity, long milliseconds) {
        Vibrator vibrator = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
        return false;
    }

    public static void Vibrate(final Activity activity, long[] pattern,
                               boolean isRepeat) {
        Vibrator vibrator = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);
    }
}