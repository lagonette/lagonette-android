package com.gonette.android.content.reader;

import android.database.Cursor;

import com.gonette.android.content.contract.GonetteContract;

public class PartnerReader
        extends CursorReader {

    public PartnerReader(Cursor cursor) {
        super(cursor);
    }

    public long getId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.ID
                )
        );
    }

    public String getName() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.NAME
                )
        );
    }

    public String getDescription() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.DESCRIPTION
                )
        );
    }

    public Double getLatitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.LATITUDE
                )
        );
    }

    public Double getLongitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.LONGITUDE
                )
        );
    }

    public boolean getIsVisible() {
        return 1 == mCursor.getInt(
                mCursor.getColumnIndex(
                        GonetteContract.PartnerMetadata.IS_VISIBLE
                )
        );
    }
}
