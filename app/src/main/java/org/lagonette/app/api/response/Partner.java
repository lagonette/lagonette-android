package org.lagonette.app.api.response;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.embedded.Address;

import java.util.List;

public class Partner {

    @SerializedName("id")
    public long id;

    @SerializedName("clientCode")
    public String clientCode;

    @SerializedName("name")
    public String name;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("logo")
    public String logo;

    @SerializedName("zipCode")
    public String zipCode;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("phone")
    public String phone;

    @SerializedName("website")
    public String website;

    @SerializedName("email")
    public String email;

    @SerializedName("description")
    public String description;

    @SerializedName("openingHours")
    public String openingHours;

    @SerializedName("isExchangeOffice")
    public Boolean isExchangeOffice; // TODO Use custom adapter to handle boolean correctly.

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("mainCategory")
    public long mainCategoryId;

    @SerializedName("sideCategories")
    public List<Long> sideCategoryIds;

    @Override
    public String toString() {
        return id + " [" + clientCode + " - " + name + "]";
    }

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Partner> partners,
            @NonNull List<org.lagonette.app.room.entity.PartnerMetadata> partnerMetadataList,
            @NonNull List<org.lagonette.app.room.entity.PartnerSideCategory> partnerSideCategories) {
        org.lagonette.app.room.entity.Partner partner = new org.lagonette.app.room.entity.Partner();
        partner.id = id;
        partner.clientCode = clientCode;
        partner.name = name;
        partner.logo = logo;
        partner.address = new Address();
        partner.address.street = address;
        partner.address.city = city;
        partner.address.zipCode = zipCode;
        partner.latitude = latitude;
        partner.longitude = longitude;
        partner.phone = phone; // TODO Format
        partner.website = website;
        partner.email = email;
        partner.description = description;
        partner.openingHours = openingHours;
        partner.isExchangeOffice = isExchangeOffice;
        partner.shortDescription = shortDescription;
        partner.mainCategoryId = mainCategoryId;
        partners.add(partner);

        org.lagonette.app.room.entity.PartnerMetadata partnerMetadata = new org.lagonette.app.room.entity.PartnerMetadata();
        partnerMetadata.partnerId = partner.id;
        partnerMetadata.isVisible = true;
        partnerMetadataList.add(partnerMetadata);

        for (Long sideCategoryId : sideCategoryIds) {
            if (sideCategoryId != null) {
                org.lagonette.app.room.entity.PartnerSideCategory sideCategory = new org.lagonette.app.room.entity.PartnerSideCategory();
                sideCategory.categoryId = sideCategoryId;
                sideCategory.partnerId = id;
                partnerSideCategories.add(sideCategory);
            }
        }
    }
}
