package org.lagonette.android.room.embedded;

import android.arch.persistence.room.ColumnInfo;

public class Address {

    @ColumnInfo(name = "zip_code")
    public String zipCode;

    @ColumnInfo(name = "street")
    public String street;

    @ColumnInfo(name = "city")
    public String city;

}