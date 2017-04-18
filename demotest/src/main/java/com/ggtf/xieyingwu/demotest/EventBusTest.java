package com.ggtf.xieyingwu.demotest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xieyingwu on 2017/3/23.
 */

public class EventBusTest {
    public EventBusTest() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onRespond(Son value) {
        Log.e(EvenBusTest.TAG, this.getClass().getSimpleName() + "-->" + value);
    }
}
