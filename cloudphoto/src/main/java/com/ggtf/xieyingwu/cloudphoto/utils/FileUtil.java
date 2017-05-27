package com.ggtf.xieyingwu.cloudphoto.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class FileUtil {
    static final String TAG = FileUtil.class.getName();
    private static File cacheDir;

    public static boolean initCacheDir(String dirName) {
        File dir = Environment.getExternalStorageDirectory();
        cacheDir = new File(dir, dirName);
        return (cacheDir.exists() && cacheDir.isDirectory()) || cacheDir.mkdirs();
    }

    public static void writeContent(String fileName, String content) {
        if (TextUtils.isEmpty(content)) return;
        try {
            File file = new File(cacheDir, fileName + ".json");
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                Log.w(TAG, "fileName = " + fileName + " newFile = " + newFile);
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] contentBytes = content.getBytes();
            fos.write(contentBytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readContent(String fileName) {
        try {
            File file = new File(cacheDir, fileName);
            if (!file.exists()) return null;
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            String result = new String(baos.toByteArray());
            fis.close();
            baos.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
