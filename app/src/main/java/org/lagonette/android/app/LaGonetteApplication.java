package org.lagonette.android.app;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.util.StrictModeUtil;

public class LaGonetteApplication
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