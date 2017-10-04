package org.lagonette.app.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.LambdaApiClient;
import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerMetadata;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class DataRefreshWorker
        extends BackgroundWorker {

    private static final String TAG = "DataRefreshWorker";

    public DataRefreshWorker(@NonNull Context context) {
        super(context);
    }

    private void logCall(@NonNull String endpoint) {
        Log.d(TAG, "Call " + LaGonetteService.HOST + endpoint);
    }

    @Override
    protected void doWork(@NonNull WorkerResponse response) {

        LaGonetteDatabase database = DB.get();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LaGonetteService.Category categoryService = Api.category();
        LaGonetteService.Partner partnerService = Api.partner();

        List<Category> categories = new ArrayList<>();
        List<CategoryMetadata> categoryMetadataList = new ArrayList<>();
        List<Partner> partners = new ArrayList<>();
        List<PartnerMetadata> partnerMetadataList = new ArrayList<>();
        List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

        LambdaApiClient<CategoriesResponse> categoryClient = getCategoriesClient(
                categories,
                categoryMetadataList,
                preferences,
                database,
                categoryService
        );

        LambdaApiClient<Md5SumResponse> categoryMd5Client = getCategoriesMd5SumClient(
                preferences,
                categoryClient,
                categoryService
        );

        LambdaApiClient<PartnersResponse> partnersClient = getPartnersClient(
                partners,
                partnerMetadataList,
                partnerSideCategories,
                preferences,
                database,
                partnerService
        );

        LambdaApiClient<Md5SumResponse> partnersMd5SumClient = getPartnersMd5SumClient(
                preferences,
                partnersClient,
                partnerService
        );

        try {
            database.beginTransaction();

            categoryMd5Client.call();

            partnersMd5SumClient.call();

            cleanUpDatabase(database);

            database.setTransactionSuccessful();
            response.setIsSuccessful(true);
        } catch (ApiClientException e) {
            Log.e(TAG, "loadInBackground: ", e);
            FirebaseCrash.report(e);
            response.setIsSuccessful(false);
            // TODO set message
        } finally {
            database.endTransaction();
        }
    }

    private LambdaApiClient<CategoriesResponse> getCategoriesClient(
            @NonNull List<Category> categories,
            @NonNull List<CategoryMetadata> categoryMetadataList,
            @NonNull SharedPreferences preferences,
            @NonNull LaGonetteDatabase database,
            @NonNull LaGonetteService.Category service) {
        return new LambdaApiClient<>(
                () -> logCall(LaGonetteService.ENDPOINT_CATEGORIES),
                service::getCategories,
                (code, body) -> {
                    body.prepareInsert(getContext(), categories, categoryMetadataList);
                    database.categoryDao().deleteCategories();
                    database.categoryDao().insertCategories(categories);
                    database.categoryDao().insertCategoriesMetadatas(categoryMetadataList); // TODO Make metadata insert only by SQL
                    // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
                    preferences.edit()
                            .putString(PreferenceUtil.KEY_CATEGORY_MD5_SUM, body.md5Sum)
                            .apply();
                },
                (code, message, errorBody) -> {
                    FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
                    throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
                }
        );
    }

    private LambdaApiClient<PartnersResponse> getPartnersClient(
            @NonNull List<Partner> partners,
            @NonNull List<PartnerMetadata> partnerMetadataList,
             @NonNull List<PartnerSideCategory> partnerSideCategories,
            @NonNull SharedPreferences preferences,
            @NonNull LaGonetteDatabase database,
            @NonNull LaGonetteService.Partner service) {
        return new LambdaApiClient<>(
                () -> logCall(LaGonetteService.ENDPOINT_PARTNERS),
                service::getPartners,
                (code, body) -> {
                    body.prepareInsert(getContext(), partners, partnerMetadataList, partnerSideCategories);
                    database.partnerDao().deletePartners();
                    database.partnerDao().insertPartners(partners);
                    database.partnerDao().insertPartnersMetadatas(partnerMetadataList);
                    database.partnerDao().deletePartnerSideCategories();
                    database.partnerDao().insertPartnersSideCategories(partnerSideCategories); // TODO Make metadata insert only by SQL
                    // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
                    preferences.edit()
                            .putString(PreferenceUtil.KEY_PARTNER_MD5_SUM, body.md5Sum)
                            .apply();
                },
                (code, message, errorBody) -> {
                    FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
                    throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
                }
        );
    }

    private LambdaApiClient<Md5SumResponse> getPartnersMd5SumClient(
            @NonNull SharedPreferences preferences,
            @NonNull LambdaApiClient<PartnersResponse> partnersClient,
            @NonNull LaGonetteService.Partner service) {
        return new LambdaApiClient<>(
                () -> logCall(LaGonetteService.ENDPOINT_CATEGORIES_MD5),
                service::getPartnersMd5,
                (code, body) -> {
                    // Get local categories MD5 sum
                    String localMd5Sum = preferences.getString(
                            PreferenceUtil.KEY_PARTNER_MD5_SUM,
                            PreferenceUtil.DEFAULT_VALUE_PARTNER_MD5_SUM
                    );
                    if (!localMd5Sum.equals(body.md5Sum)) {
                        partnersClient.call();
                    }
                },
                (code, message, errorBody) -> {
                    FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
                    throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
                }
        );
    }

    private LambdaApiClient<Md5SumResponse> getCategoriesMd5SumClient(
            @NonNull SharedPreferences preferences,
            @NonNull LambdaApiClient<CategoriesResponse> categoryClient,
            @NonNull LaGonetteService.Category service) {
        return new LambdaApiClient<>(
                () -> logCall(LaGonetteService.ENDPOINT_CATEGORIES_MD5),
                service::getCategoriesMd5,
                (code, body) -> {
                    // Get local categories MD5 sum
                    String localMd5Sum = preferences.getString(
                            PreferenceUtil.KEY_CATEGORY_MD5_SUM,
                            PreferenceUtil.DEFAULT_VALUE_CATEGORY_MD5_SUM
                    );
                    if (!localMd5Sum.equals(body.md5Sum)) {
                        categoryClient.call();
                    }
                },
                (code, message, errorBody) -> {
                    FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
                    throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
                }
        );
    }

//    @Override
//    protected void doWork(@NonNull WorkerResponse response) {
//
//        LaGonetteDatabase database = DB.get();
//        try {
//            LaGonetteService.Category categoryService = Api.category();
//            LaGonetteService.Partner partnerService = Api.partner();
//
//            List<Category> categories = new ArrayList<>();
//            List<CategoryMetadata> categoryMetadataList = new ArrayList<>();
//            List<Partner> partners = new ArrayList<>();
//            List<PartnerMetadata> partnerMetadataList = new ArrayList<>();
//            List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();
//
//            database.beginTransaction();
//
//            if (getCategories(getContext(), categoryService, categories, categoryMetadataList)) {
//                database.categoryDao().deleteCategories();
//                database.categoryDao().insertCategories(categories);
//                database.categoryDao().insertCategoriesMetadatas(categoryMetadataList);
//            }
//
//            if (getPartners(getContext(), partnerService, partners, partnerMetadataList, partnerSideCategories)) {
//                database.partnerDao().deletePartners();
//                database.partnerDao().insertPartners(partners);
//                database.partnerDao().insertPartnersMetadatas(partnerMetadataList);
//                database.partnerDao().deletePartnerSideCategories();
//                database.partnerDao().insertPartnersSideCategories(partnerSideCategories);
//            }
//
//            cleanUpDatabase(database);
//
//            database.setTransactionSuccessful();
//            response.setIsSuccessful(true);
//
//            // TODO Maybe find a way to avoid request data again if they not change
//
//        } catch (IOException | RemoteException | OperationApplicationException e) {
//            Log.e(TAG, "loadInBackground: ", e);
//            FirebaseCrash.report(e);
//            response.setIsSuccessful(false);
//            // TODO set message
//        } finally {
//            database.endTransaction();
//        }
//    }

//    private void requestCategories(@NonNull Context context, @NonNull LaGonetteService.Category service) {
//        // Get local categories MD5 sum
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String localMd5Sum = preferences.getString(
//                PreferenceUtil.KEY_CATEGORY_MD5_SUM,
//                PreferenceUtil.DEFAULT_VALUE_CATEGORY_MD5_SUM
//        );
//
//        // Request API categories MD5 sum
//        Call<String> md5Call = service.getCategoriesMd5();
//        Log.d(TAG, "Call " + LaGonetteService.HOST + LaGonetteService.ENDPOINT_CATEGORIES_MD5);
//        Response<String> md5Response = md5Call.execute();
//
//        if (!mMd5Sum.equals(md5Sum)) {
//
//            for (org.lagonette.app.api.response.Category category : mCategories) {
//                category.prepareInsert(categories, categoryMetadataList);
//            }
//
//            // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
//            preferences.edit()
//                    .putString(PreferenceUtil.KEY_CATEGORY_MD5_SUM, mMd5Sum)
//                    .apply();
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean getCategories(
//            @NonNull Context context,
//            @NonNull LaGonetteService.Category service,
//            @NonNull List<Category> categories,
//            @NonNull List<CategoryMetadata> categoryMetadataList)
//            throws IOException, RemoteException, OperationApplicationException {
//
//        Call<CategoriesResponse> categoryCall = service.getCategories();
//        Log.d(TAG, "Call " + LaGonetteService.HOST + LaGonetteService.ENDPOINT_CATEGORIES);
//        Response<CategoriesResponse> response = categoryCall.execute();
//
//        if (response.isSuccessful()) {
//            CategoriesResponse result = response.body();
//            return result.prepareInsert(context, categories, categoryMetadataList);
//        } else {
//            FirebaseCrash.logcat(Log.ERROR, TAG, response.code() + ": " + response.message());
//            throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
//        }
//    }
//
//    private boolean getPartners(
//            @NonNull Context context,
//            @NonNull LaGonetteService.Partner service,
//            @NonNull List<Partner> partners,
//            @NonNull List<PartnerMetadata> partnerMetadataList,
//            @NonNull List<PartnerSideCategory> partnerSideCategories)
//            throws IOException, RemoteException, OperationApplicationException {
//
//        Call<PartnersResponse> partnersCall = service.getPartners();
//        Log.d(TAG, "Call " + LaGonetteService.HOST + LaGonetteService.ENDPOINT_PARTNERS);
//        Response<PartnersResponse> response = partnersCall.execute();
//
//        if (response.isSuccessful()) {
//            PartnersResponse result = response.body();
//            return result.prepareInsert(context, partners, partnerMetadataList, partnerSideCategories);
//        } else {
//            FirebaseCrash.logcat(Log.ERROR, TAG, response.code() + ": " + response.message());
//            throw new IllegalStateException("response is not successful!"); // TODO Use custom exception
//        }
//    }

    private void cleanUpDatabase(@NonNull LaGonetteDatabase db) {
        db.partnerDao().cleanPartner();
//        db.partnerDao().cleanPartnerMetadata(); // TODO clean partner metadata
    }

}