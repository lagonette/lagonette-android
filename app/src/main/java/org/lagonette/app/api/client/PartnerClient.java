package org.lagonette.app.api.client;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.api.client.exception.ApiClientException;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerMetadata;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PartnerClient
        extends ApiClient<PartnersResponse> {

    private static final String TAG = "CategoryClient";

    @NonNull
    private final LaGonetteDatabase mDatabase;

    @NonNull
    private final LaGonetteService.Partner mService;

    @NonNull
    private String mMd5Sum;

    public PartnerClient(
            @NonNull LaGonetteDatabase database,
            @NonNull LaGonetteService.Partner service) {
        mDatabase = database;
        mService = service;
    }

    @Override
    protected String getEndpoint() {
        return LaGonetteService.ENDPOINT_PARTNERS;
    }

    @Override
    protected Call<PartnersResponse> createCall() {
        return mService.getPartners();
    }

    @Override
    protected void onSuccessfulResponse(int code, @NonNull PartnersResponse body) throws ApiClientException {
        List<Partner> partners = new ArrayList<>();
        List<PartnerMetadata> partnerMetadataList = new ArrayList<>();
        List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

        mMd5Sum = body.md5Sum;
        body.prepareInsert(partners, partnerMetadataList, partnerSideCategories);
        mDatabase.partnerDao().deletePartners();
        mDatabase.partnerDao().insertPartners(partners);
        mDatabase.partnerDao().insertPartnersMetadatas(partnerMetadataList);
        mDatabase.partnerDao().deletePartnerSideCategories();
        mDatabase.partnerDao().insertPartnersSideCategories(partnerSideCategories); // TODO Make metadata insert only by SQL
    }

    @Override
    protected void onErrorResponse(int code, @NonNull String message, @NonNull ResponseBody errorBody) throws ApiClientException {
        FirebaseCrash.logcat(Log.ERROR, TAG, code + ": " + message);
        throw new ApiClientException("Response is not successful!");
    }

    @Nullable
    public String getLocalMd5Sum(@NonNull SharedPreferences preferences) {
        return preferences.getString(
                PreferenceUtil.KEY_PARTNER_MD5_SUM,
                PreferenceUtil.DEFAULT_VALUE_PARTNER_MD5_SUM
        );
    }

    public void saveRemoteMd5Sum(@NonNull SharedPreferences preferences) {
        preferences.edit()
                .putString(PreferenceUtil.KEY_PARTNER_MD5_SUM, mMd5Sum)
                .apply();
    }
}
