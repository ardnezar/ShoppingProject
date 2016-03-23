package com.test.shopping.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    private static final int START_MESSAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                Intent intent = new Intent(SplashActivity.this
                        , MainActivity.class);
                startActivity(intent);
                finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(START_MESSAGE, SPLASH_DELAY);
    }
}