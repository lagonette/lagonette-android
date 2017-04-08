package com.gonette.android.app;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.gonette.android.BuildConfig;
import com.gonette.android.util.StrictModeUtil;

public class GonetteApplication
        extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            FragmentManager.enableDebugLogging(true);
            LoaderManager.enableDebugLogging(true);
            StrictModeUtil.enableStrictMode();
        }
    }
}