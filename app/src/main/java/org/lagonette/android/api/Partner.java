package org.lagonette.android.api;


import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.LaGonetteContract;

import java.util.List;

public class Partner implements ContentObject {

    @SerializedName("id")
    private long mId;

    @SerializedName("clientCode")
    private String mClientCode;

    @SerializedName("name")
    private String mName;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("city")
    private String mCity;

    @SerializedName("logo")
    private String mLogo;

    @SerializedName("zipCode")
    private String mZipCode;

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("website")
    private String mWebsite;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("openingHours")
    private String mOpeningHours;

    @SerializedName("isExchangeOffice")
    private Boolean mIsExchangeOffice; // TODO Use custom adapter to handle boolean correctly.

    @SerializedName("shortDescription")
    private String mShortDescription;

    @SerializedName("mainCategory")
    private long mMainCategory;

    @SerializedName("sideCategories")
    private List<Long> mSideCategoryIds;


    @Override
    public String toString() {
        return mId + " [" + mClientCode + " - " + mName + "]";
    }

    @Override
    public void prepareInsert(@NonNull List<ContentProviderOperation> operations, @NonNull ContentValues contentValues) {
        // Insert Partner
        contentValues.clear();
        contentValues.put(LaGonetteContract.Partner.ID, mId);
        contentValues.put(LaGonetteContract.Partner.NAME, mName);
        contentValues.put(LaGonetteContract.Partner.DESCRIPTION, mDescription);
        contentValues.put(LaGonetteContract.Partner.LATITUDE, mLatitude);
        contentValues.put(LaGonetteContract.Partner.LONGITUDE, mLongitude);
        contentValues.put(LaGonetteContract.Partner.CLIENT_CODE, mClientCode);
        contentValues.put(LaGonetteContract.Partner.ADDRESS, mAddress);
        contentValues.put(LaGonetteContract.Partner.CITY, mCity);
        contentValues.put(LaGonetteContract.Partner.LOGO, mLogo);
        contentValues.put(LaGonetteContract.Partner.ZIP_CODE, mZipCode);
        contentValues.put(LaGonetteContract.Partner.PHONE, mPhone);
        contentValues.put(LaGonetteContract.Partner.WEBSITE, mWebsite);
        contentValues.put(LaGonetteContract.Partner.EMAIL, mEmail);
        contentValues.put(LaGonetteContract.Partner.OPENING_HOURS, mOpeningHours);
        contentValues.put(LaGonetteContract.Partner.IS_EXCHANGE_OFFICE, mIsExchangeOffice);
        contentValues.put(LaGonetteContract.Partner.SHORT_DESCRIPTION, mShortDescription);
        contentValues.put(LaGonetteContract.Partner.MAIN_CATEGORY, mMainCategory);
        operations.add(
                ContentProviderOperation
                        .newInsert(LaGonetteContract.Partner.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );

        // Insert Partner metadata
        contentValues.clear();
        contentValues.put(LaGonetteContract.PartnerMetadata.PARTNER_ID, mId);
        contentValues.put(LaGonetteContract.PartnerMetadata.IS_VISIBLE, true);
        operations.add(
                ContentProviderOperation
                        .newInsert(LaGonetteContract.PartnerMetadata.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );

        // Insert Partner Side Categories
        for (Long categoryId : mSideCategoryIds) {
            contentValues.clear();
            contentValues.put(LaGonetteContract.PartnerSideCategories.PARTNER_ID, mId);
            contentValues.put(LaGonetteContract.PartnerSideCategories.CATEGORY_ID, categoryId);
            operations.add(
                    ContentProviderOperation
                            .newInsert(LaGonetteContract.PartnerSideCategories.CONTENT_URI)
                            .withValues(contentValues)
                            .withYieldAllowed(true)
                            .build()
            );
        }
    }
}
