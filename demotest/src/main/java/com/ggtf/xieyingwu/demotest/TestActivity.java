package com.ggtf.xieyingwu.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_test);
        readFocus();
    }

    private void readFocus() {
        new Thread(new Runnable() {
            int count;

            @Override
            public void run() {
                while (true) {
                    View currentFocus = getCurrentFocus();
                    Log.e("Focus", "view = " + currentFocus);
                    if (count > 100) break;
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void btnClick(View view) {
        Toast.makeText(this, "Focus", Toast.LENGTH_SHORT).show();
    }
}
