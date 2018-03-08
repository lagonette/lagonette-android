package org.lagonette.app.background.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.client.EntityConverter;
import org.lagonette.app.background.client.RetrofitClient;
import org.lagonette.app.background.client.dispatcher.ApiResponseDispatcher;
import org.lagonette.app.background.client.dispatcher.SignatureDispatcher;
import org.lagonette.app.background.client.store.EntitiesStore;
import org.lagonette.app.background.client.store.SignatureStore;
import org.lagonette.app.background.client.store.WorkerStateStore;
import org.lagonette.app.locator.Api;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.util.PreferenceUtils;

public class DataRefreshWorker
        extends BackgroundWorker {

    private static final String TAG = "DataRefreshWorker";

    public DataRefreshWorker(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected WorkerState doWork() {

        // Construct
        LaGonetteDatabase database = DB.get();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LaGonetteService.Category categoryService = Api.category();
        LaGonetteService.Partner partnerService = Api.partner();

        SignatureStore categorySignatureStore = new SignatureStore();
        RetrofitClient<Md5SumResponse> categorySignatureClient = new RetrofitClient<>();
        RetrofitClient<CategoriesResponse> categoryClient = new RetrofitClient<>();
        SignatureDispatcher categorySignatureDispatcher = new SignatureDispatcher();
        ApiResponseDispatcher<CategoriesResponse> categoryDispatcher = new ApiResponseDispatcher<>();

        SignatureStore partnerSignatureStore = new SignatureStore();
        RetrofitClient<Md5SumResponse> partnerSignatureClient = new RetrofitClient<>();
        RetrofitClient<PartnersResponse> partnerClient = new RetrofitClient<>();
        SignatureDispatcher partnerSignatureDispatcher = new SignatureDispatcher();
        ApiResponseDispatcher<PartnersResponse> partnerDispatcher = new ApiResponseDispatcher<>();

        EntityConverter entityConverter = new EntityConverter();
        EntitiesStore entitiesStore = new EntitiesStore();

        WorkerStateStore workerStateStore = new WorkerStateStore();


        // Supply
        partnerSignatureStore.retrieveLocalSignature = () -> preferences.getString(
                PreferenceUtils.KEY_PARTNER_MD5_SUM,
                PreferenceUtils.DEFAULT_VALUE_PARTNER_MD5_SUM
        );
        partnerSignatureStore.saveSignature = signature -> preferences.edit()
                .putString(PreferenceUtils.KEY_PARTNER_MD5_SUM, signature)
                .apply();

        categorySignatureStore.retrieveLocalSignature = () -> preferences.getString(
                PreferenceUtils.KEY_CATEGORY_MD5_SUM,
                PreferenceUtils.DEFAULT_VALUE_CATEGORY_MD5_SUM
        );
        categorySignatureStore.saveSignature = signature -> preferences.edit()
                .putString(PreferenceUtils.KEY_CATEGORY_MD5_SUM, signature)
                .apply();

        partnerSignatureClient.callFactory = partnerService::getMd5Sum;
        partnerClient.callFactory = partnerService::getPartners;

        categorySignatureClient.callFactory = categoryService::getMd5Sum;
        categoryClient.callFactory = categoryService::getCategories;


        // --- Update categories --- //

        // Get category signature
        categorySignatureClient.onResponseSuccessful = categorySignatureDispatcher::onResponseSuccessful;
        categorySignatureClient.onResponseError = (code, message, body) -> {
            Log.e(TAG, "Response error → " + code + ": " + message);
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };
        categorySignatureClient.onException = exc -> {
            Log.e(TAG, "Caught: ", exc);
            FirebaseCrash.report(exc);
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };

        // Dispatch category signature
        categorySignatureDispatcher.onBodyError = () -> {
            Log.e(TAG, "Remote category signature is NULL.");
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };
        categorySignatureDispatcher.hasSignatureChanged = categorySignatureStore::hasSignatureChanged;
        categorySignatureDispatcher.onSignatureNotChanged = partnerSignatureClient::call;
        categorySignatureDispatcher.onSignatureChanged = signature -> {
            Log.d(TAG, "Category signature has changed → update categories");
            categoryClient.call();
        };

        // Get categories
        categoryClient.onException = exc -> {
            Log.e(TAG, "Caught: ", exc);
            FirebaseCrash.report(exc);
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };
        categoryClient.onResponseError = (code, message, ResponseBody) -> {
            Log.e(TAG, "Response error → " + code + ": " + message);
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };
        categoryClient.onResponseSuccessful = categoryDispatcher::onResponseSuccessful;

        // Dispatch categories
        categoryDispatcher.onBodyError = errors -> {
            Log.e(TAG, errors);
            workerStateStore.store(WorkerState.error(Error.CATEGORIES_NOT_UPDATED));
        };
        categoryDispatcher.onSuccessful = entityConverter::convertEntity;

        // Convert API entities and store it
        entityConverter.storeCategorySignature = categorySignatureStore::store;
        entityConverter.storeCategories = entitiesStore::storeCategories;
        entityConverter.storeCategoryMetadata = entitiesStore::storeCategoryMetadata;
        entityConverter.onCategoryConversionFinished = partnerSignatureClient::call;
        // ------- //


        // --- Update partners --- //

        // Get partner signature
        partnerSignatureClient.onException = exc -> {
            Log.e(TAG, "Caught: ", exc);
            FirebaseCrash.report(exc);
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerSignatureClient.onResponseError = (code, message, ResponseBody) -> {
            Log.e(TAG, "Response error → " + code + ": " + message);
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerSignatureClient.onResponseSuccessful = partnerSignatureDispatcher::onResponseSuccessful;

        // Dispatch partner signature
        partnerSignatureDispatcher.onBodyError = () -> {
            Log.e(TAG, "Partner remote signature is NULL.");
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerSignatureDispatcher.hasSignatureChanged = partnerSignatureStore::hasSignatureChanged;
        partnerSignatureDispatcher.onSignatureNotChanged = entitiesStore::saveEntities;
        partnerSignatureDispatcher.onSignatureChanged = signature -> {
            Log.d(TAG, "Partner signature has changed → update partners");
            partnerClient.call();
        };

        // Get partners
        partnerClient.onException = exc -> {
            Log.e(TAG, "Caught: ", exc);
            FirebaseCrash.report(exc);
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerClient.onResponseError = (code, message, ResponseBody) -> {
            Log.e(TAG, "Response error → " + code + ": " + message);
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerClient.onResponseSuccessful = partnerDispatcher::onResponseSuccessful;

        // Dispatch partners
        partnerDispatcher.onBodyError = (errors) -> {
            Log.d(TAG, errors);
            workerStateStore.store(WorkerState.error(Error.PARTNERS_NOT_UPDATED));
        };
        partnerDispatcher.onSuccessful = entityConverter::convertEntity;

        // Convert API entities and store it
        entityConverter.storePartnerSignature = partnerSignatureStore::store;
        entityConverter.storePartners = entitiesStore::storePartners;
        entityConverter.storeLocations = entitiesStore::storeLocations;
        entityConverter.storeLocationMetadata = entitiesStore::storeLocationMetadata;
        entityConverter.storePartnerSideCategories = entitiesStore::storePartnerSideCategories;
        entityConverter.onPartnerConversionFinished = entitiesStore::saveEntities;
        // ------- //

        // --- Save entities --- //

        // Save entities
        entitiesStore.saveEntities = database.writerDao()::insert;
        entitiesStore.entitiesSaved = categorySignatureStore::save;

        categorySignatureStore.signatureSaved = partnerSignatureStore::save;
        partnerSignatureStore.signatureSaved = () -> workerStateStore.store(WorkerState.success());
        // -------- //

        // --- Start --- //
        categorySignatureClient.call();

        return workerStateStore.getState();
    }

}