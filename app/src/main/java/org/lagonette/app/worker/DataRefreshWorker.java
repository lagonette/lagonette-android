package org.lagonette.app.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.CategoryClient;
import org.lagonette.app.api.client.Md5SumClient;
import org.lagonette.app.api.client.PartnerClient;
import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;

public class DataRefreshWorker
        extends BackgroundWorker {

    private static final String TAG = "DataRefreshWorker";

    public DataRefreshWorker(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void doWork(@NonNull WorkerResponse workerResponse) {

        LaGonetteDatabase database = DB.get();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LaGonetteService.Category categoryService = Api.category();
        LaGonetteService.Partner partnerService = Api.partner();


        CategoryClient categoryClient = new CategoryClient(database, categoryService);

        Md5SumClient categoryMd5Client = new Md5SumClient(
                categoryService::getMd5Sum,
                () -> categoryClient.getLocalMd5Sum(preferences)
        );

        PartnerClient partnerClient = new PartnerClient(database, partnerService);

        Md5SumClient partnerMd5SumClient = new Md5SumClient(
                partnerService::getMd5Sum,
                () -> partnerClient.getLocalMd5Sum(preferences)
        );

        try {
            database.beginTransaction();

            // Get Categories
            categoryMd5Client.call();
            if (!categoryMd5Client.isSuccess()) {
                workerResponse.setIsSuccessful(false);
                return;
            }

            if (categoryMd5Client.isMd5SumChanged()) {
                Log.d(TAG, "Category MD5 sum has changed -> update categories");
                categoryClient.call();
            }

            if (!categoryClient.isSuccess()) {
                workerResponse.setIsSuccessful(false);
                return;
            }

            // Get Partners
            partnerMd5SumClient.call();
            if (!partnerMd5SumClient.isSuccess()) {
                workerResponse.setIsSuccessful(false);
                return;
            }

            if (partnerMd5SumClient.isMd5SumChanged()) {
                Log.d(TAG, "Partner MD5 sum has changed -> update partners");
                partnerClient.call();
            }

            if (!partnerClient.isSuccess()) {
                workerResponse.setIsSuccessful(false);
                return;
            }

            // Clean up and end database transaction
            cleanUpDatabase(database);
            database.setTransactionSuccessful();

            // Save new md5
            if (categoryMd5Client.isMd5SumChanged()) {
                categoryClient.saveRemoteMd5Sum(preferences);
            }

            if (partnerMd5SumClient.isMd5SumChanged()) {
                partnerClient.saveRemoteMd5Sum(preferences);
            }

            // Set worker successful
            workerResponse.setIsSuccessful(true);
        } catch (ApiClientException e) {
            Log.e(TAG, "loadInBackground: ", e);
            FirebaseCrash.report(e);
            workerResponse.setIsSuccessful(false);
            // TODO set message
        } finally {
            database.endTransaction();
        }
    }

    private void cleanUpDatabase(@NonNull LaGonetteDatabase db) {
        db.partnerDao().cleanPartner();
//        db.partnerDao().cleanPartnerMetadata(); // TODO clean partner metadata
    }

}