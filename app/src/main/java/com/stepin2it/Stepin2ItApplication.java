package com.stepin2it;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class Stepin2ItApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(Stepin2ItApplication.this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
