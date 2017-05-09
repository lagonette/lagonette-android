package org.lagonette.android.api;


import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.content.contract.GonetteContract;

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
        contentValues.put(GonetteContract.Partner.ID, mId);
        contentValues.put(GonetteContract.Partner.NAME, mName);
        contentValues.put(GonetteContract.Partner.DESCRIPTION, mDescription);
        contentValues.put(GonetteContract.Partner.LATITUDE, mLatitude);
        contentValues.put(GonetteContract.Partner.LONGITUDE, mLongitude);
        contentValues.put(GonetteContract.Partner.CLIENT_CODE, mClientCode);
        contentValues.put(GonetteContract.Partner.ADDRESS, mAddress);
        contentValues.put(GonetteContract.Partner.CITY, mCity);
        contentValues.put(GonetteContract.Partner.LOGO, mLogo);
        contentValues.put(GonetteContract.Partner.ZIP_CODE, mZipCode);
        contentValues.put(GonetteContract.Partner.PHONE, mPhone);
        contentValues.put(GonetteContract.Partner.WEBSITE, mWebsite);
        contentValues.put(GonetteContract.Partner.EMAIL, mEmail);
        contentValues.put(GonetteContract.Partner.OPENING_HOURS, mOpeningHours);
        contentValues.put(GonetteContract.Partner.IS_EXCHANGE_OFFICE, mIsExchangeOffice);
        contentValues.put(GonetteContract.Partner.SHORT_DESCRIPTION, mShortDescription);
        contentValues.put(GonetteContract.Partner.MAIN_CATEGORY, mMainCategory);
        operations.add(
                ContentProviderOperation
                        .newInsert(GonetteContract.Partner.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );

        // Insert Partner metadata
        contentValues.clear();
        contentValues.put(GonetteContract.PartnerMetadata.PARTNER_ID, mId);
        contentValues.put(GonetteContract.PartnerMetadata.IS_VISIBLE, true);
        operations.add(
                ContentProviderOperation
                        .newInsert(GonetteContract.PartnerMetadata.CONTENT_URI)
                        .withValues(contentValues)
                        .withYieldAllowed(true)
                        .build()
        );

        // Insert Partner Side Categories
        for (Long categoryId : mSideCategoryIds) {
            contentValues.clear();
            contentValues.put(GonetteContract.PartnerSideCategories.PARTNER_ID, mId);
            contentValues.put(GonetteContract.PartnerSideCategories.CATEGORY_ID, categoryId);
            operations.add(
                    ContentProviderOperation
                            .newInsert(GonetteContract.PartnerSideCategories.CONTENT_URI)
                            .withValues(contentValues)
                            .withYieldAllowed(true)
                            .build()
            );
        }
    }
}
