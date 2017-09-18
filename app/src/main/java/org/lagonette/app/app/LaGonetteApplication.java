package org.lagonette.app.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.locator.API;
import org.lagonette.app.locator.DB;
import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.MainRepo;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.util.DatabaseUtil;
import org.lagonette.app.util.StrictModeUtil;

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