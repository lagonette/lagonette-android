package org.lagonette.android.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.api.service.LaGonetteService;
import org.lagonette.android.locator.API;
import org.lagonette.android.locator.DB;
import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.MainRepo;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.util.DatabaseUtil;
import org.lagonette.android.util.StrictModeUtil;

import java.util.concurrent.Executors;

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

        DB.set(
                Room
                        .databaseBuilder(
                                LaGonetteApplication.this,
                                LaGonetteDatabase.class,
                                DatabaseUtil.DATABASE_NAME
                        )
                        .build()
        );

        API.set(
                LaGonetteService.retrofit.create(LaGonetteService.class)
        );

        Repo.set(
                new MainRepo(
                        LaGonetteApplication.this,
                        Executors.newCachedThreadPool()
                )
        );
    }
}