package org.lagonette.android.api.response;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.annotations.SerializedName;

import org.lagonette.android.room.entity.PartnerMetadata;
import org.lagonette.android.room.entity.special.OfficePartner;
import org.lagonette.android.room.entity.special.OfficePartnerMetadata;
import org.lagonette.android.util.PreferenceUtil;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    private static final String TAG = "PartnersResponse";

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
            addOfficePartner(partners, partnerMetadataList);

            doSomeCheck(partners);

            // TODO Ensure data are saved before saving md5 sum, maybe put this in a runnable an execute it after closing transaction
            preferences.edit()
                    .putString(PreferenceUtil.KEY_PARTNER_MD5_SUM, mMd5Sum)
                    .apply();
            return true;
        }

        return false;
    }

    // TODO Make a custom retrofit TypeConverter
    private void doSomeCheck(@NonNull List<org.lagonette.android.room.entity.Partner> partners) {
        boolean send = false;
        for (org.lagonette.android.room.entity.Partner partner : partners) {
            if (partner.latitude == 0 || partner.longitude == 0) {
                send = true;
                FirebaseCrash.logcat(Log.WARN, TAG, "Wrong coordinates " + partner.name + " (" + partner.id + "): (" + partner.latitude + ", " + partner.longitude + ")" );
            }

            if (TextUtils.isEmpty(partner.description)) {
                send = true;
                FirebaseCrash.logcat(Log.WARN, TAG, "Empty description " + partner.name + " (" + partner.id + ")" );
            }

            if (TextUtils.isEmpty(partner.shortDescription)) {
                send = true;
                FirebaseCrash.logcat(Log.WARN, TAG, "Empty short description " + partner.name + " (" + partner.id + ")" );
            }
        }

        if (send) {
            // TODO Use crashlitics
            FirebaseCrash.report(new Exception("API send incomplete data."));
        }

    }

    private void addOfficePartner(@NonNull List<org.lagonette.android.room.entity.Partner> partners, @NonNull List<PartnerMetadata> partnerMetadataList) {
        partners.add(new OfficePartner());
        partnerMetadataList.add(new OfficePartnerMetadata());
    }
}