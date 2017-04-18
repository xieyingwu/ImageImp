package com.ggtf.xieyingwu.weatherservice.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public class Net {
    private Net() {

    }

    public static void linkUrl(String url, HttpResult httpResult) {
        try {
            URL urlOpen = new URL(url);
            HttpURLConnection huC = (HttpURLConnection) urlOpen.openConnection();
            huC.setRequestMethod("GET");
            huC.setConnectTimeout(5 * 1000);
            huC.setReadTimeout(5 * 1000);
            huC.setDoInput(true);
            int responseCode = huC.getResponseCode();
            boolean isHttpOk = responseCode == HttpURLConnection.HTTP_OK;
            if (isHttpOk) {
                InputStream is = huC.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                httpResult.successful(new String(baos.toByteArray()));
                is.close();
            } else {
                httpResult.error("responseCode = " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface HttpResult {
        void error(String json);

        void successful(String json);
    }
}
