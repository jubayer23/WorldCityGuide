package com.ismeralda_fabio.worldcityguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private static int DISPLACEMENT = 10;
    private static int FATEST_INTERVAL = 0;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int UPDATE_INTERVAL = 10000;
    private static final int duration = 3000;
    private final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_splash);


        this.mHandler.postDelayed(this.mPendingLauncherRunnable, 3000L);
    }

    private final Runnable mPendingLauncherRunnable = new Runnable() {
        public void run() {
            Intent localIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(localIntent);
            finish();
        }
    };

    private boolean mRequestingLocationUpdates = false;

    static {
        FATEST_INTERVAL = 5000;
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(this.mPendingLauncherRunnable);
    }
}