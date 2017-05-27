package com.ggtf.xieyingwu.cloudphoto.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class NetUtil {
    static final String TAG = NetUtil.class.getName();
    static final String GET = "GET";
    static final String POST = "POST";
    static final int READ_TIMEOUT = 20 * 1000;
    static final int CONNECT_TIMEOUT = 20 * 1000;

    public static String connectByGet(String urlStr) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(GET);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            is = urlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buf = new byte[1024 * 2];
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            String result = new String(baos.toByteArray());
            Log.w(TAG, "result = " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }

    public static String connectByPost(String urlStr, HashMap<String, String> params) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(POST);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            StringBuilder paramsStr = new StringBuilder();
            for (String key : params.keySet()) {
                String value = params.get(key);
                paramsStr.append(key).append("=").append(value).append("&");
            }
            String params1 = paramsStr.toString();
            Log.w(TAG, "paramsStr = " + params1);
            params1 = params1.substring(0, params1.length() - 1);
            Log.w(TAG, "paramsStr = " + params1);
            writer.write(params1);
            writer.flush();
//            urlConnection.connect();

            is = urlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buf = new byte[1024 * 2];
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            String result = new String(baos.toByteArray());
            Log.w(TAG, "result = " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
        return null;
    }
}
