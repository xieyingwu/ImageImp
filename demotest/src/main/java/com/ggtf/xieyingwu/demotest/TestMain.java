package com.ggtf.xieyingwu.demotest;

import android.util.Log;

/**
 * Created by xieyingwu on 2017/3/23.
 */

public class TestMain {
    public static final String TAG = "TestMain";

    public static void main(String[] args) {
        int heightPX = 1920;
        int widthPX = 1080;
        float screenSize = 15.0f;
        double ppi = Math.sqrt(heightPX * heightPX + widthPX * widthPX);
        Log.e(TAG, "ppi = " + ppi);
    }
}
