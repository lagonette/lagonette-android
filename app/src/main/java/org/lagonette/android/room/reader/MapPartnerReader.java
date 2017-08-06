package org.lagonette.android.room.reader;


import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.room.reader.base.CursorReader;
import org.lagonette.android.room.statement.MapPartnerStatement;

public class MapPartnerReader
        extends CursorReader
        implements MapPartnerStatement.Contract {

    @Nullable
    public static MapPartnerReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new MapPartnerReader(cursor)
                : null;
    }

    public MapPartnerReader(@NonNull Cursor cursor) {
        super(cursor);
    }

    @Override
    public long getId() {
        return mCursor.getLong(MapPartnerStatement.ID);
    }

    @Override
    public String getName() {
        return mCursor.getString(MapPartnerStatement.NAME);
    }

    @Override
    public String getDescription() {
        return mCursor.getString(MapPartnerStatement.DESCRIPTION);
    }

    @Override
    public double getLatitude() {
        return mCursor.getDouble(MapPartnerStatement.LATITUDE);
    }

    @Override
    public double getLongitude() {
        return mCursor.getDouble(MapPartnerStatement.LONGITUDE);
    }

    @Override
    public Boolean isExchangeOffice() {
        return 0 != mCursor.getInt(MapPartnerStatement.IS_EXCHANGE_OFFICE);
    }

    @Override
    public long getMainCategory() {
        return mCursor.getLong(MapPartnerStatement.MAIN_CATEGORY);
    }

    @Override
    public String getIcon() {
        return mCursor.getString(MapPartnerStatement.ICON);
    }

}