package org.lagonette.app.api.response;

import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.embedded.Address;
import org.lagonette.app.room.entity.LocationMetadata;

import java.util.List;

public class Location {

    @SerializedName("id")
    public long id;

    @SerializedName("openingHours")
    public String openingHours;

    @SerializedName("isExchangeOffice")
    public boolean isExchangeOffice;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("zipCode")
    public String zipCode;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("displayLocation")
    public boolean displayLocation;

    @Ignore
    public long partnerId;


    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Location> locationEntities,
            @NonNull List<LocationMetadata> locationMetadataEntities) {
        org.lagonette.app.room.entity.Location location = new org.lagonette.app.room.entity.Location();
        location.address = new Address();
        location.id = id;
        location.address.street = address;
        location.address.city = city;
        location.address.zipCode = zipCode;
        location.latitude = latitude;
        location.longitude = longitude;
        location.openingHours = openingHours;
        location.isExchangeOffice = isExchangeOffice;
        location.displayLocation = displayLocation;
        location.partnerId = partnerId;
        locationEntities.add(location);

        LocationMetadata locationMetadata = new LocationMetadata();
        locationMetadata.locationId = location.id;
        locationMetadata.isVisible = true;
        locationMetadataEntities.add(locationMetadata);
    }
}
