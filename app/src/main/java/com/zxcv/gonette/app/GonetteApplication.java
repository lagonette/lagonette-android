package com.zxcv.gonette.app;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.zxcv.gonette.BuildConfig;
import com.zxcv.gonette.util.StrictModeUtil;

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