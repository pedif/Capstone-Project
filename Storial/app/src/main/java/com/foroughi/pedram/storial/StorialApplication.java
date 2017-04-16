package com.foroughi.pedram.storial;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Pedram on 4/16/2017.
 */

public class StorialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }
}
