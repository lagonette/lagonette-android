package org.lagonette.app.room.entity.statement;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

public class HeadquarterShortcut {

    @ColumnInfo(name = "location_id")
    public final long locationId;

    @NonNull
    @ColumnInfo(name = "icon")
    public final String icon;

    public HeadquarterShortcut(long locationId, @NonNull String icon) {
        this.locationId = locationId;
        this.icon = icon;
    }
}
