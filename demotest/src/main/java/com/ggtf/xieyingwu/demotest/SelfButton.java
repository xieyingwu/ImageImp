package com.ggtf.xieyingwu.demotest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xieyingwu on 2017/3/30.
 */

public class SelfButton extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener {
    private static final String TAG = SelfButton.class.getName();

    public SelfButton(Context context) {
        this(context, null);
    }

    public SelfButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelfButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e(TAG, "onTouch action = " + event.getAction());
        return true;
    }
}
