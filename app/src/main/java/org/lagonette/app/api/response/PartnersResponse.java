package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.entity.PartnerMetadata;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    private static final String TAG = "PartnersResponse";

    @SerializedName("partners")
    private List<Partner> mPartners;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Partner> partners,
            @NonNull List<org.lagonette.app.room.entity.Location> locations,
            @NonNull List<org.lagonette.app.room.entity.PartnerMetadata> partnerMetadataList,
            @NonNull List<org.lagonette.app.room.entity.PartnerSideCategory> partnerSideCategories) {
        for (Partner partner : mPartners) {
            if (partner != null) {
                partner.prepareInsert(partners, locations, partnerMetadataList, partnerSideCategories);
            }
        }
    }

}