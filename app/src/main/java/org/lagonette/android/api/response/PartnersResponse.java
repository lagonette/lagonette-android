package org.lagonette.android.api.response;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.util.PreferenceUtil;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    @SerializedName("partners")
    private List<Partner> mPartners;

    public boolean prepareInsert(
            @NonNull Context context,
            @NonNull List<org.lagonette.android.room.entity.Partner> partners,
            @NonNull List<org.lagonette.android.room.entity.PartnerMetadata> partnerMetadataList,
            @NonNull List<org.lagonette.android.room.entity.PartnerSideCategory> partnerSideCategories) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String md5Sum = preferences.getString(
                PreferenceUtil.KEY_PARTNER_MD5_SUM,
                PreferenceUtil.DEFAULT_VALUE_PARTNER_MD5_SUM
        );


        if (!mMd5Sum.equals(md5Sum)) {

            for (Partner partner : mPartners) {
                partner.prepareInsert(partners, partnerMetadataList, partnerSideCategories);
            }

            // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
            preferences.edit()
                    .putString(PreferenceUtil.KEY_PARTNER_MD5_SUM, mMd5Sum)
                    .apply();
            return true;
        }

        return false;
    }
}