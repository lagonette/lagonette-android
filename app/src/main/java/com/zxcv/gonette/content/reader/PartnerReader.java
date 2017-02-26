package com.zxcv.gonette.content.reader;

import android.database.Cursor;

import com.zxcv.gonette.content.contract.GonetteContract;

public class PartnerReader
        extends CursorReader {

    public PartnerReader(Cursor mCursor) {
        super(mCursor);
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
}
