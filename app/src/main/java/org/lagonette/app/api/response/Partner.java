package org.lagonette.app.api.response;


import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import org.lagonette.app.room.entity.LocationMetadata;

import java.util.List;

public class Partner {

    @Json(name = "id")
    public long id;

    @Json(name = "clientCode")
    public String clientCode;

    @Json(name = "name")
    public String name;

    @Json(name = "logo")
    public String logo;

    @Json(name = "phone")
    public String phone;

    @Json(name = "website")
    public String website;

    @Json(name = "email")
    public String email;

    @Json(name = "description")
    public String description;

    @Json(name = "isGonetteHeadquarter")
    public boolean isGonetteHeadquarter;

    @Json(name = "shortDescription")
    public String shortDescription;

    @Json(name = "locations")
    public List<Location> locations;

    @Json(name = "mainCategory")
    public long mainCategoryId;

    @Json(name = "sideCategories")
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
        partner.phone = phone;
        partner.website = website;
        partner.email = email;
        partner.description = description;
        partner.isGonetteHeadquarter = isGonetteHeadquarter;
        partner.shortDescription = shortDescription;
        partner.mainCategoryId = mainCategoryId;
        partnerEntities.add(partner);

        for (Location location : locations) {
            if (location != null) {
                location.prepareInsert(id, locationEntities, locationMetadataEntities);
            }
        }

        for (Long sideCategoryId : sideCategoryIds) {
            if (sideCategoryId != null) {
                org.lagonette.app.room.entity.PartnerSideCategory sideCategory = new org.lagonette.app.room.entity.PartnerSideCategory();
                sideCategory.categoryId = sideCategoryId;
                sideCategory.partnerId = id;
                partnerSideCategoryEntities.add(sideCategory);
            }
        }
    }
}
