package com.ggtf.xieyingwu.cloudphoto.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class GsonUtil {
    public static <T> T json2Object(String json, Class<T> classOf) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, classOf);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
