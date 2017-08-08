package org.lagonette.android.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
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

        RoomDatabase.Builder<LaGonetteDatabase> builder = Room.
                databaseBuilder(
                        LaGonetteApplication.this,
                        LaGonetteDatabase.class,
                        DatabaseUtil.DATABASE_NAME
                );

        if (BuildConfig.DEBUG) { // TODO /!\ Testing purpose
            builder.allowMainThreadQueries();
        }

        database = builder.build();
    }
}