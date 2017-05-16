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

    public final CategoryReader categoryReader;

    public PartnerReader(@NonNull Cursor cursor) {
        super(cursor);
        categoryReader = new CategoryReader(cursor);
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

    public long getMainCategoryId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.MAIN_CATEGORY
                )
        );
    }

    public String getShortDescription() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.SHORT_DESCRIPTION
                )
        );
    }

    public String getAddress() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.ADDRESS
                )
        );
    }

    public String getCity() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.CITY
                )
        );
    }

    public String getZipCode() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.ZIP_CODE
                )
        );
    }

    public String getPhone() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.PHONE
                )
        );
    }

    public String getEmail() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.EMAIL
                )
        );
    }

    public String getWebsite() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.WEBSITE
                )
        );
    }

    public String getOpeningHours() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.OPENING_HOURS
                )
        );
    }

    public String getLogo() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        GonetteContract.Partner.LOGO
                )
        );
    }

}
