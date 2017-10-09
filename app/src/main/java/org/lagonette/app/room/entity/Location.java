package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.lagonette.app.room.embedded.Address;

@Entity(
        tableName = "location"
)
public class Location {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "opening_hours")
    public String openingHours;

    @ColumnInfo(name = "is_exchange_office")
    public boolean isExchangeOffice;

    @Embedded
    public Address address;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "display_location")
    public boolean displayLocation;

    @ColumnInfo(name = "partner_id")
    public long partnerId;
}
