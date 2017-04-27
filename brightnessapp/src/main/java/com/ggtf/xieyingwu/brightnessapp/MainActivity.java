package com.ggtf.xieyingwu.brightnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int brightness;
    private TextView tv;
    private BrightnessUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        utils = new BrightnessUtils(this);
        brightness = utils.getScreenBrightness();
        showText(false);

    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                brightness += 5;
                break;
            case R.id.btn_down:
                brightness -= 5;
                break;
        }
        checkBrightnessValue();
        showText(true);
    }

    private void showText(boolean change) {
        String text = "当前屏幕亮度：" + brightness;
        tv.setText(text);
        if (change) {
            utils.saveScreenBrightness(brightness);
            BrightnessUtils.saveBrightness(getContentResolver(), brightness);
        }
    }

    private void checkBrightnessValue() {
//        亮度0-255
        if (brightness >= 255) {
            brightness = 0;
        } else if (brightness <= 0) {
            brightness = 255;
        }
    }
}
