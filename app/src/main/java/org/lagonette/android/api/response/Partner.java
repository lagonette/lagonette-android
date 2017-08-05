package org.lagonette.android.api.response;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.android.room.embedded.Address;

import java.util.List;

public class Partner {

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
    private long mMainCategoryId;

    @SerializedName("sideCategories")
    private List<Long> mSideCategoryIds;


    @Override
    public String toString() {
        return mId + " [" + mClientCode + " - " + mName + "]";
    }


    public void prepareInsert(
            @NonNull List<org.lagonette.android.room.entity.Partner> partners,
            @NonNull List<org.lagonette.android.room.entity.PartnerMetadata> partnerMetadataList,
            @NonNull List<org.lagonette.android.room.entity.PartnerSideCategory> partnerSideCategories) {
        org.lagonette.android.room.entity.Partner partner = new org.lagonette.android.room.entity.Partner();
        partner.id = mId;
        partner.clientCode = mClientCode;
        partner.name = mName;
        partner.logo = mLogo;
        partner.address = new Address();
        partner.address.street = mAddress;
        partner.address.city = mCity;
        partner.address.zipCode = mZipCode;
        partner.latitude = mLatitude;
        partner.longitude = mLongitude;
        partner.phone = mPhone;
        partner.website = mWebsite;
        partner.email = mEmail;
        partner.description = mDescription;
        partner.openingHours = mOpeningHours;
        partner.isExchangeOffice = mIsExchangeOffice;
        partner.shortDescription = mShortDescription;
        partner.mainCategoryId = mMainCategoryId;
        partners.add(partner);

        org.lagonette.android.room.entity.PartnerMetadata partnerMetadata = new org.lagonette.android.room.entity.PartnerMetadata();
        partnerMetadata.partnerId = partner.id;
        partnerMetadata.isVisible = true;
        partnerMetadataList.add(partnerMetadata);

        for (Long sideCategoryId : mSideCategoryIds) {
            if (sideCategoryId != null) {
                org.lagonette.android.room.entity.PartnerSideCategory sideCategory = new org.lagonette.android.room.entity.PartnerSideCategory();
                sideCategory.categoryId = sideCategoryId;
                sideCategory.partnerId = mId;
                partnerSideCategories.add(sideCategory);
            }
        }
    }
}
