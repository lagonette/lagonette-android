package org.lagonette.app.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.api.adapter.BooleanTypeAdapter;
import org.lagonette.app.api.adapter.LongTypeAdapter;
import org.lagonette.app.api.adapter.PartnerListTypeAdapter;
import org.lagonette.app.api.adapter.PartnerTypeAdapter;
import org.lagonette.app.api.adapter.StringTypeAdapter;
import org.lagonette.app.api.response.Partner;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.MainRepo;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.migration.VoidMigration;
import org.lagonette.app.util.DatabaseUtil;
import org.lagonette.app.util.StrictModeUtil;

import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                                DatabaseUtil.DATABASE_NAME
                        )
                        .addMigrations(new VoidMigration(1, 2))
                        .addMigrations(new VoidMigration(2, 3))
                        //.fallbackToDestructiveMigration() //TODO Remove migration
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

        Gson mainGson = new GsonBuilder()
                .registerTypeAdapter(String.class, new StringTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                .create();

        PartnerListTypeAdapter partnerListTypeAdapter = new PartnerListTypeAdapter(mainGson);
        PartnerTypeAdapter partnerTypeAdapter = new PartnerTypeAdapter(mainGson, partnerListTypeAdapter);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class, partnerListTypeAdapter)
                .registerTypeAdapter(Partner.class, partnerTypeAdapter)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LaGonetteService.HOST)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api.setPartnerService(
                retrofit.create(LaGonetteService.Partner.class)
        );

        retrofit = new Retrofit.Builder()
                .baseUrl(LaGonetteService.HOST)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(mainGson))
                .build();

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