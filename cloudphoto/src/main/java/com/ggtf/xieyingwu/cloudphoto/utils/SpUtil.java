package com.ggtf.xieyingwu.cloudphoto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class SpUtil {
    public static SharedPreferences sp;

    public static void initSp(Context context, String spName) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static void saveStr(String key, String value) {
        if (TextUtils.isEmpty(value)) return;
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getStr(String key) {
        return sp.getString(key, null);
    }
}
