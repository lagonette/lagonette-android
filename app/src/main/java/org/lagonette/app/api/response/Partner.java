package org.lagonette.app.api.response;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.entity.LocationMetadata;

import java.util.List;

public class Partner {

    @SerializedName("id")
    public long id;

    @SerializedName("clientCode")
    public String clientCode;

    @SerializedName("name")
    public String name;

    @SerializedName("logo")
    public String logo;

    @SerializedName("phone")
    public String phone;

    @SerializedName("website")
    public String website;

    @SerializedName("email")
    public String email;

    @SerializedName("description")
    public String description;

    @SerializedName("isGonetteHeadquarter")
    public boolean isGonetteHeadquarter;

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("locations")
    public List<Location> locations;

    @SerializedName("mainCategory")
    public long mainCategoryId;

    @SerializedName("sideCategories")
    public List<Long> sideCategoryIds;

    @Override
    public String toString() {
        return id + " [" + clientCode + " - " + name + "]";
    }

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Partner> partnerEntities,
            @NonNull List<org.lagonette.app.room.entity.Location> locationEntities,
            @NonNull List<LocationMetadata> locationMetadataEntities,
            @NonNull List<org.lagonette.app.room.entity.PartnerSideCategory> partnerSideCategoryEntities) {
        org.lagonette.app.room.entity.Partner partner = new org.lagonette.app.room.entity.Partner();
        partner.id = id;
        partner.clientCode = clientCode;
        partner.name = name;
        partner.logo = logo;
        partner.phone = phone; //TODO Format, use google lib phone
        partner.website = website;
        partner.email = email;
        partner.description = description;
        partner.isGonetteHeadquarter = isGonetteHeadquarter;
        partner.shortDescription = shortDescription;
        partner.mainCategoryKey = new CategoryKey();
        partner.mainCategoryKey.id = mainCategoryId;
        partner.mainCategoryKey.type = 0; //TODO Workaround, actually, partner associated with custom/parent category does not exit.
        partnerEntities.add(partner);

        for (Location location : locations) {
            if (location != null) {
                location.prepareInsert(locationEntities, locationMetadataEntities);
            }
        }

        for (Long sideCategoryId : sideCategoryIds) {
            if (sideCategoryId != null) {
                org.lagonette.app.room.entity.PartnerSideCategory sideCategory = new org.lagonette.app.room.entity.PartnerSideCategory();
                sideCategory.categoryKey = new CategoryKey();
                sideCategory.categoryKey.id = sideCategoryId;
                sideCategory.categoryKey.type = 0; //TODO Workaround, actually, partner associated with custom/parent category does not exit.
                sideCategory.partnerId = id;
                partnerSideCategoryEntities.add(sideCategory);
            }
        }
    }
}
