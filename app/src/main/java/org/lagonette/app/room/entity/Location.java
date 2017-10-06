package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

import org.lagonette.app.room.embedded.Address;

@Entity(
        tableName = "location"
)
public class Location {

    @ColumnInfo(name = "opening_hours")
    public String openingHours;

    @ColumnInfo(name = "is_exchangeOffice")
    public String isExchangeOffice;

    @Embedded
    public Address address;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;

    @ColumnInfo(name = "display_location")
    public String displayLocation;
}
