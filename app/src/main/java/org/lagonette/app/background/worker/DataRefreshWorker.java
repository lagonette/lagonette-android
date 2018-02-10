package org.lagonette.app.background.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.lagonette.app.background.client.dispatcher.ApiResponseDispatcher;
import org.lagonette.app.background.client.EntityConverter;
import org.lagonette.app.background.tools.ErrorCatcher;
import org.lagonette.app.background.client.RetrofitClient;
import org.lagonette.app.background.tools.FunctionLogger;
import org.lagonette.app.background.client.store.EntitiesStore;
import org.lagonette.app.background.client.dispatcher.SignatureDispatcher;
import org.lagonette.app.background.client.store.SignatureStore;
import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.Md5SumResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
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

    @Override
    protected void doWork(@NonNull WorkerResponse workerResponse) {


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


        // Produce
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


        // Connect categories endpoint process
        categorySignatureClient.onException = ErrorCatcher::catchException;
        categorySignatureClient.onResponseError = ErrorCatcher.logError((code, message, errorBody) -> "Response error → " + code + ": " + message);
        categorySignatureClient.onResponseSuccessful = categorySignatureDispatcher::onResponseSuccessful;

        categorySignatureDispatcher.onBodyError = ErrorCatcher.logError(() -> "Category remote signature is NULL.");
        categorySignatureDispatcher.hasSignatureChanged = categorySignatureStore::hasSignatureChanged;
        categorySignatureDispatcher.onSignatureNotChanged = partnerSignatureClient::call;
        categorySignatureDispatcher.onSignatureChanged = FunctionLogger.logd(TAG, "Category signature has changed → update categories", signature -> categoryClient.call());

        categoryClient.onException = ErrorCatcher::catchException;
        categoryClient.onResponseError = ErrorCatcher.logError((code, message, ResponseBody) -> code + ": " + message);
        categoryClient.onResponseSuccessful = categoryDispatcher::onResponseSuccessful;

        categoryDispatcher.onBodyError = ErrorCatcher.logError(errors -> "Errors: " + errors);
        categoryDispatcher.onSuccessful = entityConverter::convertEntity;

        entityConverter.storeCategorySignature = categorySignatureStore::store;
        entityConverter.storeCategories = entitiesStore::storeCategories;
        entityConverter.storeCategoryMetadata = entitiesStore::storeCategoryMetadata;
        entityConverter.onCategoryConversionFinished = partnerSignatureClient::call;


        // Connect partners endpoint process
        partnerSignatureClient.onException = ErrorCatcher::catchException;
        partnerSignatureClient.onResponseError = ErrorCatcher.logError((code, message, errorBody) -> "Response error → " + code + ": " + message);
        partnerSignatureClient.onResponseSuccessful = partnerSignatureDispatcher::onResponseSuccessful;

        partnerSignatureDispatcher.onBodyError = ErrorCatcher.logError(() -> "Partner remote signature is NULL.");
        partnerSignatureDispatcher.hasSignatureChanged = partnerSignatureStore::hasSignatureChanged;
        partnerSignatureDispatcher.onSignatureNotChanged = entitiesStore::saveEntities;
        partnerSignatureDispatcher.onSignatureChanged = FunctionLogger.logd(TAG, "Partner signature has changed → update partners", signature -> partnerClient.call());

        partnerClient.onException = ErrorCatcher::catchException;
        partnerClient.onResponseError = ErrorCatcher.logError((code, message, ResponseBody) -> code + ": " + message);
        partnerClient.onResponseSuccessful = partnerDispatcher::onResponseSuccessful;

        partnerDispatcher.onBodyError = ErrorCatcher.logError(errors -> "Errors: " + errors);
        partnerDispatcher.onSuccessful = entityConverter::convertEntity;

        entityConverter.storePartnerSignature = partnerSignatureStore::store;
        entityConverter.storePartners = entitiesStore::storePartners;
        entityConverter.storeLocations = entitiesStore::storeLocations;
        entityConverter.storeLocationMetadata = entitiesStore::storeLocationMetadata;
        entityConverter.storePartnerSideCategories = entitiesStore::storePartnerSideCategories;
        entityConverter.onPartnerConversionFinished = entitiesStore::saveEntities;


        // Save entities
        entitiesStore.saveEntities = database.writerDao()::insert;
        entitiesStore.entitiesSaved = categorySignatureStore::save;

        categorySignatureStore.signatureSaved = partnerSignatureStore::save;
        partnerSignatureStore.signatureSaved = () -> workerResponse.setIsSuccessful(true);


        // Start
        categorySignatureClient.call();

    }

}