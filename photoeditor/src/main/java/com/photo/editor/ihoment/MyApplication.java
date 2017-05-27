package com.photo.editor.ihoment;

import android.app.Application;

import ly.img.android.PESDK;

/**
 * Created by xieyingwu on 2017/5/15.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*初始化*/
        PESDK.init(this, "photoEditorSdk_android_license");
    }
}
