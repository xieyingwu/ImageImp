package com.ggtf.xieyingwu.demotest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xieyingwu on 2017/3/23.
 */

public class EvenBusTest {
    public static final String TAG = "EventBus";

    public EvenBusTest() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 30)
    public void onRespond(Father value) {
        Log.e(TAG, this.getClass().getSimpleName() + "-->" + value.toString());
    }
}
