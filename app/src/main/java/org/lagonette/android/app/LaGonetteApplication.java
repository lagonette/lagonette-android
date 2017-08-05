package org.lagonette.android.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.util.DatabaseUtil;
import org.lagonette.android.util.StrictModeUtil;

public class LaGonetteApplication
        extends Application {

    public LaGonetteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            FragmentManager.enableDebugLogging(true);
            LoaderManager.enableDebugLogging(true);
            StrictModeUtil.enableStrictMode();
        }

        database = Room
                .databaseBuilder(
                        LaGonetteApplication.this,
                        LaGonetteDatabase.class,
                        DatabaseUtil.DATABASE_NAME
                )
                .allowMainThreadQueries() // TODO /!\ Testing purpose
                .build();
    }
}