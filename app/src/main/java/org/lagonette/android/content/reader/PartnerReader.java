package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.base.CursorReader;

public class PartnerReader
        extends CursorReader {

    @Nullable
    public static PartnerReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new PartnerReader(cursor)
                : null;
    }

    @NonNull
    public final CategoryReader categoryReader;

    @NonNull
    public final PartnerMetadataReader metadataReader;

    public PartnerReader(@NonNull Cursor cursor) {
        this(cursor, CategoryReader.create(cursor));
    }

    public PartnerReader(@NonNull Cursor cursor, @NonNull CategoryReader categoryReader) {
        super(cursor);
        this.categoryReader = categoryReader;
        this.metadataReader = PartnerMetadataReader.create(cursor);
    }

    public long getId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.ID
                )
        );
    }

    @NonNull
    public String getName() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.NAME
                )
        );
    }

    @NonNull
    public String getDescription() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.DESCRIPTION
                )
        );
    }

    public double getLatitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.LATITUDE
                )
        );
    }

    public double getLongitude() {
        return mCursor.getDouble(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.LONGITUDE
                )
        );
    }

    public boolean isExchangeOffice() {
        return 1 == mCursor.getInt(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.IS_EXCHANGE_OFFICE
                )
        );
    }

    public long getMainCategoryId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.MAIN_CATEGORY
                )
        );
    }

    public String getShortDescription() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.SHORT_DESCRIPTION
                )
        );
    }

    public String getAddress() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.ADDRESS
                )
        );
    }

    public String getCity() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.CITY
                )
        );
    }

    public String getZipCode() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.ZIP_CODE
                )
        );
    }

    public String getPhone() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.PHONE
                )
        );
    }

    public String getEmail() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.EMAIL
                )
        );
    }

    public String getWebsite() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.WEBSITE
                )
        );
    }

    public String getOpeningHours() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.OPENING_HOURS
                )
        );
    }

    public String getLogo() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.LOGO
                )
        );
    }

    public void getFullAddress(@NonNull StringBuilder builder) {
        builder.setLength(0);
        String address = mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Partner.ADDRESS
                )
        );
        if (!TextUtils.isEmpty(address)) {

            builder.append(
                    address
            );
            builder.append(", ");
            builder.append(
                    mCursor.getString(
                            mCursor.getColumnIndex(
                                    LaGonetteContract.Partner.ZIP_CODE
                            )
                    )
            );
            builder.append(" ");
            builder.append(
                    mCursor.getString(
                            mCursor.getColumnIndex(
                                    LaGonetteContract.Partner.CITY
                            )
                    )
            );
        }
    }

}
