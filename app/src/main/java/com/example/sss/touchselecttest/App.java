package com.example.sss.touchselecttest;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by SSS on 2015-12-27.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Timber */
        Timber.plant(new Timber.DebugTree());
    }
}
