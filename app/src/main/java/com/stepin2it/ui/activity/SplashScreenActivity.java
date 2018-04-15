package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.stepin2it.R;
import com.stepin2it.ui.dashboard.DashBoardMvpActivity;
import com.stepin2it.ui.login.LoginMvpActivity;
import com.stepin2it.utils.IConstants;
import com.stepin2it.data.PreferenceHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
//    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreePause();
    }

    private void splashScreePause() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                loginCheck();
            }
        }, SPLASH_TIME_OUT);
    }

    private void loginCheck() {
        Intent loginIntent;
        if (PreferenceHelper.getInstance(SplashScreenActivity.this).readString(IConstants.IPreference.PREF_TOKEN) != null) {
            loginIntent = new Intent(SplashScreenActivity.this, DashBoardMvpActivity.class);
        } else {
            loginIntent = new Intent(SplashScreenActivity.this, LoginMvpActivity.class);
        }
        startActivity(loginIntent);
        finish();
    }
}
