package com.ggtf.xieyingwu.runtimepermissiontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private static void logW(String msg) {
        Log.w(TAG, "" + msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void checkPermission() {
        boolean checkCalendarPer = RPUtil.checkCalendarPer(this);
        logW("checkCalendarPer = " + checkCalendarPer);
        if (!checkCalendarPer) {
            RPUtil.requestCalendarPer(this);
        }

        boolean checkCameraPer = RPUtil.checkCameraPer(this);
        logW("checkCameraPer = " + checkCameraPer);
        if (!checkCameraPer) {
            RPUtil.requestCameraPer(this);
        }

        boolean checkContactsPer = RPUtil.checkContactsPer(this);
        logW("checkContactsPer = " + checkContactsPer);
        if (!checkContactsPer) {
            RPUtil.requestContactsPer(this);
        }

        boolean checkLocationPer = RPUtil.checkLocationPer(this);
        logW("checkLocationPer = " + checkLocationPer);
        if (!checkLocationPer) {
            RPUtil.requestLocationPer(this);
        }

        boolean checkMicrophonePer = RPUtil.checkMicrophonePer(this);
        logW("checkMicrophonePer = " + checkMicrophonePer);
        if (!checkMicrophonePer) {
            RPUtil.requestMicrophonePer(this);
        }

        boolean checkPhonePer = RPUtil.checkPhonePer(this);
        logW("checkPhonePer = " + checkPhonePer);
        if (!checkPhonePer) {
            RPUtil.requestPhonePer(this);
        }

        boolean checkSensorsPer = RPUtil.checkSensorsPer(this);
        logW("checkSensorsPer = " + checkSensorsPer);
        if (!checkSensorsPer) {
            RPUtil.requestSensorsPer(this);
        }

        boolean checkSmsPer = RPUtil.checkSmsPer(this);
        logW("checkSmsPer = " + checkSmsPer);
        if (!checkSmsPer) {
            RPUtil.requestSmsPer(this);
        }

        boolean checkStoragePer = RPUtil.checkStoragePer(this);
        logW("checkStoragePer = " + checkStoragePer);
        if (!checkStoragePer) {
            RPUtil.requestStoragePer(this);
        }
    }

    private void checkSystemAlertWindowPer() {
        boolean checkSystemAlertWindowPer = RPUtil.checkSystemAlertWindowPer(this);
        logW("checkSystemAlertWindowPer = " + checkSystemAlertWindowPer);
        if (!checkSystemAlertWindowPer) {
            RPUtil.requestSystemAlertWindowPer(this);
        }
    }

    private void checkWriteSettingPer() {
        boolean checkWriteSettingPre = RPUtil.checkWriteSettingPre(this);
        logW("checkWriteSettingPre = " + checkWriteSettingPre);
        if (!checkWriteSettingPre) {
            RPUtil.requestWriteSettingPer(this);
        }
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.check_runtime_per:
                checkPermission();
                break;
            case R.id.check_system_alert_window_per:
                checkSystemAlertWindowPer();
                break;
            case R.id.check_write_setting_per:
                checkWriteSettingPer();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RPUtil.requestPermissionResult(requestCode, grantResults);
    }
}
