package com.ggtf.xieyingwu.runtimepermissiontest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by xieyingwu on 2017/5/27.
 * 运行时权限工具类
 */

public final class RPUtil {
    private static final String TAG = RPUtil.class.getName();
    private static final int REQUEST_CODE = 100;
    private static final int CALENDAR_REQUEST_CODE = REQUEST_CODE + 1;
    private static final int CAMERA_REQUEST_CODE = REQUEST_CODE + 2;
    private static final int CONTACTS_REQUEST_CODE = REQUEST_CODE + 3;
    private static final int LOCATION_REQUEST_CODE = REQUEST_CODE + 4;
    private static final int MICROPHONE_REQUEST_CODE = REQUEST_CODE + 5;
    private static final int PHONE_REQUEST_CODE = REQUEST_CODE + 6;
    private static final int SENSORS_REQUEST_CODE = REQUEST_CODE + 7;
    private static final int SMS_REQUEST_CODE = REQUEST_CODE + 8;
    private static final int STORAGE_REQUEST_CODE = REQUEST_CODE + 9;
    private static String[] RUNTIME_PERMISSION_SET_CALENDAR = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
    };
    private static String[] RUNTIME_PERMISSION_SET_CAMERA = {
            Manifest.permission.CAMERA
    };
    private static String[] RUNTIME_PERMISSION_SET_CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };
    private static String[] RUNTIME_PERMISSION_SET_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static String[] RUNTIME_PERMISSION_SET_MICROPHONE = {
            Manifest.permission.RECORD_AUDIO
    };
    private static String[] RUNTIME_PERMISSION_SET_PHONE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.USE_SIP
    };
    private static String[] RUNTIME_PERMISSION_SET_SENSORS = {
            Manifest.permission.BODY_SENSORS
    };
    private static String[] RUNTIME_PERMISSION_SET_SMS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH
    };
    private static String[] RUNTIME_PERMISSION_SET_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static String[] SPECIAL_PERMISSION_SET = {
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WRITE_SETTINGS
    };

    public static boolean checkCalendarPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_CALENDAR) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestCalendarPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_CALENDAR, CALENDAR_REQUEST_CODE);
    }

    public static boolean checkCameraPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_CAMERA) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestCameraPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_CAMERA, CAMERA_REQUEST_CODE);
    }

    public static boolean checkContactsPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_CONTACTS) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestContactsPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_CONTACTS, CONTACTS_REQUEST_CODE);
    }

    public static boolean checkLocationPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_LOCATION) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestLocationPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_LOCATION, LOCATION_REQUEST_CODE);
    }

    public static boolean checkMicrophonePer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_MICROPHONE) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestMicrophonePer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_MICROPHONE, MICROPHONE_REQUEST_CODE);
    }

    public static boolean checkPhonePer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_PHONE) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestPhonePer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_PHONE, PHONE_REQUEST_CODE);
    }

    public static boolean checkSensorsPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_SENSORS) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestSensorsPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_SENSORS, SENSORS_REQUEST_CODE);
    }

    public static boolean checkSmsPer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_SMS) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestSmsPer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_SMS, SMS_REQUEST_CODE);
    }

    public static boolean checkStoragePer(Context context) {
        boolean result = false;
        for (String permission : RUNTIME_PERMISSION_SET_STORAGE) {
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (result) break;
        }
        return result;
    }

    public static void requestStoragePer(Activity ac) {
        ActivityCompat.requestPermissions(ac, RUNTIME_PERMISSION_SET_STORAGE, STORAGE_REQUEST_CODE);
    }

    public static boolean checkSystemAlertWindowPer(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public static void requestSystemAlertWindowPer(Activity ac) {
        try {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            intent.setData(Uri.parse("package:" + ac.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ac.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkWriteSettingPre(Context context) {
        return Settings.System.canWrite(context);
    }

    public static void requestWriteSettingPer(Activity ac) {
        try {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + ac.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ac.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestPermissionResult(int requestCode, int[] grantResults) {
        boolean grant = isGrant(grantResults);
        Log.w(TAG, "grant = " + grant);
        switch (requestCode) {
            case CALENDAR_REQUEST_CODE:
                break;
            case CAMERA_REQUEST_CODE:
                break;
            case CONTACTS_REQUEST_CODE:
                break;
            case LOCATION_REQUEST_CODE:
                break;
            case MICROPHONE_REQUEST_CODE:
                break;
            case PHONE_REQUEST_CODE:
                break;
            case SENSORS_REQUEST_CODE:
                break;
            case SMS_REQUEST_CODE:
                break;
            case STORAGE_REQUEST_CODE:
                break;
        }
    }

    private static boolean isGrant(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (PackageManager.PERMISSION_GRANTED == grantResult) {
                return true;
            }
        }
        return false;
    }

}
