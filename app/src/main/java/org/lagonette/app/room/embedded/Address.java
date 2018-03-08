package org.lagonette.app.room.embedded;

import android.arch.persistence.room.ColumnInfo;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.lagonette.app.R;

public class Address {

    @ColumnInfo(name = "zip_code")
    public String zipCode;

    @ColumnInfo(name = "street")
    public String street;

    @ColumnInfo(name = "city")
    public String city;

    public String format(@NonNull Resources resources) {
        return resources.getString(R.string.all_format_address_full, street, zipCode, city);
    }
}