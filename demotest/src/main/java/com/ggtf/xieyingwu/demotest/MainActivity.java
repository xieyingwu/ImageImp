package com.ggtf.xieyingwu.demotest;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "DRAG";
    private static final int DEFAULT_OFFSET = 100;
    private static final int OFFSET = 15;
    private Button dragBtn1;
    private Button dragBtn2;
    private ImageView dragImageView;
    private WindowManager.LayoutParams lp;
    private WindowManager wm;
    private boolean isAdded;
    private LinearLayout parent;
    private EvenBusTest evenBusTest;
    private float lastX;
    private float lastY;
    private int viewHeight;
    private float downY;

    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(macSerial)) {
            macSerial = macSerial.replaceAll(":", "").toUpperCase();
        }

        return macSerial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        requestPer();
//        initView();
//        setContentView(R.layout.drag_group);
//        initDragView();
//        setContentView(R.layout.png_test);
        setContentView(R.layout.even_bus);
        EventBus.getDefault().register(this);
        Log.e(TAG, "注册EventBus");
        evenBusTest = new EvenBusTest();
        EventBusTest eventBusTest = new EventBusTest();
        Log.e(TAG, "getMac = " + getMac());
        testDimen();
//        testRes();
        startAC();
    }

    private void startAC() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    private void testRes() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        int densityDpi = dm.densityDpi;
        Log.e(TAG, "density = " + density + " ;densityDpi = " + densityDpi);
        for (int i = 16; i < 80; i++) {
            float toSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i, dm);
            Log.e(TAG, "i = " + i + " toSp =  " + toSp);
        }
    }

    private void testDimen() {
        List<String> values = new ArrayList<>();
        DecimalFormat df = new DecimalFormat(".00");
//        for (int i = 1; i <= 1920; i++) {
//            String format = df.format(i / 1.5f);
//            if (i < 1.5f) {
//                format = "0" + format;
//            }
//            String dimen = "<dimen name=\"space_" + format + "dp\">" + format + "dp" + "</dimen>\n";
//            values.add(dimen);
//        }
        for (int i = 16; i <= 80; i++) {
            String format = df.format(i / 1.5f);
            if (i < 1.5f) {
                format = "0" + format;
            }
            String dimen = "<dimen name=\"text_" + i + "px\">" + (i - 2) + "px" + "</dimen>\n";
            values.add(dimen);
        }
        File dimenFile = new File(Environment.getExternalStorageDirectory(), "dimenFile.txt");
        try {
            FileOutputStream fos = new FileOutputStream(dimenFile, false);
            for (String value : values) {
                byte[] bytes = value.getBytes();
                fos.write(bytes);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 20)
    public void respond(Object value) {
        Log.e(EvenBusTest.TAG, value.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initDragView() {
        GridView rightContainer = (GridView) findViewById(R.id.right_container);
        List<Integer> resIds = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            resIds.add(R.mipmap.ic_launcher);
        }
        AdapterGrid adapter = new AdapterGrid(resIds, this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "selected true");
                ImageView img = (ImageView) v;
                if (img.getTag() == null) {
                    img.setTag(true);
                    img.setImageResource(R.mipmap.ic_launcher_round);
                } else {
                    img.setTag(null);
                    img.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });
        rightContainer.setAdapter(adapter);
    }

    private void requestPer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean systemAlertWindowPreGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
            Log.e(TAG, " systemAlertWindowPreGranted = " + systemAlertWindowPreGranted);
            boolean canDrawOverlays = Settings.canDrawOverlays(this);
            if (canDrawOverlays) {
                Log.e(TAG, "获取到了悬浮权限");
            } else {
                Log.e(TAG, "未获取到悬浮权限；调转到悬浮权限界面提醒用户设置悬浮权限");
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                Log.e(TAG, permissions[i] + ";isGranted = " + grantResults[i]);
            }
        }
    }

    private void initView() {
        parent = (LinearLayout) findViewById(R.id.parent);
        dragBtn1 = (Button) findViewById(R.id.btn_1);
        dragBtn2 = (Button) findViewById(R.id.btn_2);
        dragBtn1.setOnTouchListener(this);
        dragBtn2.setOnTouchListener(this);
        dragBtn1.setOnClickListener(this);
        dragBtn2.setOnClickListener(this);
        dragImageView = new ImageView(this);
        lp = new WindowManager.LayoutParams();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.equals(dragBtn1)) {
            showDragView(v);
        }
        return false;
    }

    private void pickUpAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 1f, 50f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(3 * 1000);
        animator.start();
    }

    private void showDragView(View dragView) {
        Log.i(TAG, "showDragView");
        dragView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = dragView.getDrawingCache(true);
        if (drawingCache == null) return;
        dragImageView.setImageBitmap(drawingCache);
        lp.width = dragView.getWidth() - DEFAULT_OFFSET;
        lp.height = dragView.getHeight();
        Log.i(TAG, "width = " + lp.width + " ;height = " + lp.height);
        lp.gravity = Gravity.TOP;
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        lp.format = PixelFormat.TRANSPARENT;
//        lp.windowAnimations=android.R.style.Animation_Translucent;/*入场出场动画*/
        lp.y = (int) lastY;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!isAdded) {
            wm.addView(dragImageView, lp);
            isAdded = true;
        } else {
            wm.updateViewLayout(dragImageView, lp);
        }
        Log.i(TAG, "lp.y = " + lp.y + " ;dragImageViewGetY = " + dragImageView.getY());
//        dragView.setDrawingCacheEnabled(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setVisibility(View.INVISIBLE);
                lastX = v.getX();
                lastY = v.getY();
                viewHeight = v.getHeight();
                downY = event.getRawY();
                Log.i(TAG, "lastX = " + lastX + " ;lastY = " + lastY + " ;height = " + viewHeight);
                showDragView(v);
//                pickUpAnimation(v);
                break;
            case MotionEvent.ACTION_MOVE:
                float movedY = event.getRawY() - downY;
                if (movedY > viewHeight + OFFSET) {
                    /*移动距离超过高度；且是正值，说明待移动的控件想向下移动一个位置*/
                    View childAt = parent.getChildAt(1);
//                    Log.e(TAG, "childAt1 = " + childAt);
                    if (!childAt.equals(v)) {
                        parent.removeView(childAt);
                        parent.addView(childAt, 0);
                    }
                } else if (movedY < -(viewHeight + OFFSET)) {
                    /*移动距离超过高度，且是负值，说明控件想向上移动一个位置*/
                    View childAt = parent.getChildAt(0);
                    if (!childAt.equals(v)) {
                        parent.removeView(childAt);
                        parent.addView(childAt, 1);
                    }
                }
//                v.setTranslationY(movedY);
//                Log.i(TAG,"downY = "+downY+" ;movedY = "+movedY);
                updateDragView(movedY);
                break;
            case MotionEvent.ACTION_UP:
                v.setVisibility(View.VISIBLE);
                resetPosition(v);
                break;
        }
        return true;
    }

    private void updateDragView(float movedY) {
        if (isAdded) {
            lp.y = (int) (lastY + movedY);
            wm.updateViewLayout(dragImageView, lp);
        }
    }

    private void resetPosition(View v) {
//        v.scrollTo((int) lastX, (int) lastY);
        v.setTranslationX(0);
        v.setTranslationY(0);
        if (isAdded) {
//            lp.y= (int) v.getY();
//            wm.updateViewLayout(dragImageView,lp);
//            移除悬浮拖拽界面
            wm.removeViewImmediate(dragImageView);
            isAdded = false;
        }
        v.destroyDrawingCache();/*释放缓存的bitmap*/

    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick = " + v);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post("Android");
                    }
                }).start();
                break;
            case R.id.btn1:
                Log.e(TAG, "POST EVENT BUS");
                EventBus.getDefault().post("Java");
                break;
            case R.id.btn2:
                EventBus.getDefault().post(new Father(""));
                break;
        }
    }
}
