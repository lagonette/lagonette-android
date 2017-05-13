package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.GonetteContract;

public class PartnerReader
        extends CursorReader {

    public static PartnerReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new PartnerReader(cursor)
                : null;
    }

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

    @NonNull
    public String getName() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.NAME
                )
        );
    }

    @NonNull
    public String getDescription() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.DESCRIPTION
                )
        );
    }

    public double getLatitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.LATITUDE
                )
        );
    }

    public double getLongitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.LONGITUDE
                )
        );
    }

    public boolean isExchangeOffice() {
        return 1 == mCursor.getInt(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.IS_EXCHANGE_OFFICE
                )
        );
    }

    public boolean isVisible() {
        return 1 == mCursor.getInt(
                mCursor.getColumnIndex(
                        GonetteContract.PartnerMetadata.IS_VISIBLE
                )
        );
    }

    @NonNull
    public String getCategoryIconUrl() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Category.ICON
                )
        );
    }

    public long getMainCategoryId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.MAIN_CATEGORY
                )
        );
    }
}
