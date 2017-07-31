package org.lagonette.android.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    @SerializedName("partners")
    private List<Partner> mPartners;

    public void prepareInsert(
            @NonNull List<org.lagonette.android.room.entity.Partner> partners,
            @NonNull List<org.lagonette.android.room.entity.PartnerMetadata> partnerMetadataList,
            @NonNull List<org.lagonette.android.room.entity.PartnerSideCategory> partnerSideCategories) {
        for (Partner partner : mPartners) {
            partner.prepareInsert(partners, partnerMetadataList, partnerSideCategories);
        }
    }
}