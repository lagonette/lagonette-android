package org.lagonette.app.api.client;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.api.service.LaGonetteService;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PartnerClient
        extends EntityClient<PartnersResponse> {

    @NonNull
    private final LaGonetteDatabase mDatabase;

    @NonNull
    private final LaGonetteService.Partner mService;

    @Nullable
    private String mMd5Sum;

    public PartnerClient(
            @NonNull LaGonetteDatabase database,
            @NonNull LaGonetteService.Partner service) {
        mDatabase = database;
        mService = service;
    }

    @NonNull
    @Override
    protected Call<PartnersResponse> createCall() {
        return mService.getPartners();
    }

    @Override
    protected void onSuccessfulBody(@NonNull PartnersResponse body) {
        List<Partner> partners = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationMetadata> locationMetadataList = new ArrayList<>();
        List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

        mMd5Sum = body.md5Sum;
        body.prepareInsert(partners, locations, locationMetadataList, partnerSideCategories);

        mDatabase.partnerDao().deletePartners();
        mDatabase.partnerDao().deleteLocations();
        mDatabase.partnerDao().deletePartnerSideCategories();

        mDatabase.partnerDao().insertLocations(locations);
        mDatabase.partnerDao().insertLocationsMetadatas(locationMetadataList); //TODO Make metadata insert only by SQL
        mDatabase.partnerDao().insertPartners(partners);
        mDatabase.partnerDao().insertPartnersSideCategories(partnerSideCategories);
    }

    //TODO Maybe this method should not be here
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
