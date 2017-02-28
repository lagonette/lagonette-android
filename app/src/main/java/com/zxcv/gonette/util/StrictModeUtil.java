package com.zxcv.gonette.util;

import android.os.StrictMode;

import com.zxcv.gonette.BuildConfig;

public class StrictModeUtil {

    public static void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .build()
            );
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .build()
            );
        }
    }
}
