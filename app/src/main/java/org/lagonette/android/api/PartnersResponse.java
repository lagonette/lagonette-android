package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.util.PreferenceUtil;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    @SerializedName("partners")
    private List<Partner> mPartners;

    @Override
    public void prepareInsert(@NonNull Context context, @NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String md5Sum = preferences.getString(
                PreferenceUtil.KEY_PARTNER_MD5_SUM,
                PreferenceUtil.DEFAULT_VALUE_PARTNER_MD5_SUM
        );

        if (!mMd5Sum.equals(md5Sum)) {
            operations.add(
                    ContentProviderOperation.newDelete(LaGonetteContract.PartnerSideCategories.CONTENT_URI)
                            .withYieldAllowed(true)
                            .build()
            );

            operations.add(
                    ContentProviderOperation.newDelete(LaGonetteContract.Partner.CONTENT_URI)
                            .withYieldAllowed(true)
                            .build()
            );

            for (Partner partner : mPartners) {
                partner.prepareInsert(context, operations, contentValues);
            }

            // TODO Ensure data are saved before saving md5 sum
            preferences.edit()
                    .putString(PreferenceUtil.KEY_PARTNER_MD5_SUM, mMd5Sum)
                    .apply();
        }
    }

}