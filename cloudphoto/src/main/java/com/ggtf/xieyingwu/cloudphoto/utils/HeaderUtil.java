package com.ggtf.xieyingwu.cloudphoto.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by xieyingwu on 2017/5/22.
 */

public class HeaderUtil {
    private static final int REQUEST_CODE_TAKE = 100;
    private static final int REQUEST_CODE_PICK = 101;
    private static final int REQUEST_CODE_CROP = 102;
    private File headerFile;

    public void openCameraForHeader(Activity ac, File headerFile) {
        this.headerFile = headerFile;
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(headerFile));
        ac.startActivityForResult(takeIntent, REQUEST_CODE_TAKE);
    }

    public void pickPicForHeader(Activity ac) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ac.startActivityForResult(pickIntent, REQUEST_CODE_PICK);
    }

    private void cropPicForHeader(Activity ac, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        ac.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public void onActivityResult(Activity ac, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_TAKE:
                cropPicForHeader(ac, Uri.fromFile(headerFile));
                break;
            case REQUEST_CODE_PICK:
                cropPicForHeader(ac, data.getData());
                break;
            case REQUEST_CODE_CROP:
                saveHeader(data);
                break;
        }
    }

    private void saveHeader(Intent data) {
        Log.w("TAG", "saveHeader()");
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // TODO: 2017/5/22 存储裁剪的最终图片到本地
        }
    }
}
