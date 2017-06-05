package org.lagonette.android.api;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.LaGonetteContract;

import java.util.List;

public class PartnersResponse extends ApiResponse {

    @SerializedName("partners")
    private List<Partner> mPartners;

    @Override
    public void prepareInsert(@NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues) {
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
            partner.prepareInsert(operations, contentValues);
        }
    }
}