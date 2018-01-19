package org.lagonette.app.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.squareup.moshi.Moshi;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.adapter.BooleanAdapter;
import org.lagonette.app.api.adapter.LongAdapter;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.MainRepo;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.util.DatabaseUtils;
import org.lagonette.app.util.StrictModeUtils;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class LaGonetteApplication
        extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            FragmentManager.enableDebugLogging(true);
            LoaderManager.enableDebugLogging(true);
            StrictModeUtils.enableStrictMode();
        }

        setUpDb();
        setUpApi();
        setUpRepo();
    }

    private void setUpDb() {
        DB.set(
                Room
                        .databaseBuilder(
                                LaGonetteApplication.this,
                                LaGonetteDatabase.class,
                                DatabaseUtils.DATABASE_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build()
        );
    }

    private void setUpApi() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            httpClient.addInterceptor(logging);
        }

        Moshi moshi = new Moshi.Builder()
                .add(new BooleanAdapter())
                .add(new LongAdapter())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LaGonetteService.HOST)
                .client(httpClient.build())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        Api.setPartnerService(
                retrofit.create(LaGonetteService.Partner.class)
        );

        Api.setCategoryService(
                retrofit.create(LaGonetteService.Category.class)
        );
    }

    private void setUpRepo() {
        Repo.set(
                new MainRepo(
                        LaGonetteApplication.this,
                        Executors.newCachedThreadPool()
                )
        );
    }
}