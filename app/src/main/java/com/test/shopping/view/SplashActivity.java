package com.test.shopping.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;

/*
 * This is the launcher activity to show a splash page to the user when he launched the app in
 * cold mode.
 */

public class SplashActivity extends Activity {

    //Delay the Splash page for 2 seconds
    private static final int SPLASH_DELAY = 2 * 1000;

    private static final int START_MESSAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                Intent intent = new Intent(SplashActivity.this
                        , ProductListingActivity.class);
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
