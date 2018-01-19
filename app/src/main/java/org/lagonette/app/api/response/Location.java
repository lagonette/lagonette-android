package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import org.lagonette.app.room.embedded.Address;
import org.lagonette.app.room.entity.LocationMetadata;

import java.util.List;

public class Location {

    @Json(name = "id")
    public long id;

    @Json(name = "openingHours")
    public String openingHours;

    @Json(name = "isExchangeOffice")
    public boolean isExchangeOffice;

    @Json(name = "address")
    public String address;

    @Json(name = "city")
    public String city;

    @Json(name = "zipCode")
    public String zipCode;

    @Json(name = "latitude")
    public double latitude;

    @Json(name = "longitude")
    public double longitude;

    @Json(name = "displayLocation")
    public boolean displayLocation;

    public void prepareInsert(
            long partnerId,
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
