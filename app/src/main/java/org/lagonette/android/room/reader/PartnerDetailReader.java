package org.lagonette.android.room.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.android.room.reader.base.CursorReader;
import org.lagonette.android.room.statement.PartnerDetailStatement;

public class PartnerDetailReader extends CursorReader implements PartnerDetailStatement.Contract {

    @Nullable
    public static PartnerDetailReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new PartnerDetailReader(cursor)
                : null;
    }

    @NonNull
    private final StringBuilder builder;

    public PartnerDetailReader(@NonNull Cursor cursor) {
        super(cursor);
        this.builder = new StringBuilder();
    }


    @Override
    public long getId() {
        return mCursor.getLong(PartnerDetailStatement.ID);
    }

    @Override
    public String getName() {
        return mCursor.getString(PartnerDetailStatement.NAME);
    }

    @Override
    public String getDescription() {
        return mCursor.getString(PartnerDetailStatement.DESCRIPTION);
    }

    @Override
    public double getLatitude() {
        return mCursor.getDouble(PartnerDetailStatement.LATITUDE);
    }

    @Override
    public double getLongitude() {
        return mCursor.getDouble(PartnerDetailStatement.LONGITUDE);
    }

    @Override
    public String getLogo() {
        return mCursor.getString(PartnerDetailStatement.LOGO);
    }

    @Override
    public String getPhone() {
        return mCursor.getString(PartnerDetailStatement.PHONE);
    }

    @Override
    public String getWebsite() {
        return mCursor.getString(PartnerDetailStatement.WEBSITE);
    }

    @Override
    public String getEmail() {
        return mCursor.getString(PartnerDetailStatement.EMAIL);
    }

    @Override
    public String getOpeningHours() {
        return mCursor.getString(PartnerDetailStatement.OPENING_HOURS);
    }

    @Override
    public boolean isExchangeOffice() {
        return 0 != mCursor.getInt(PartnerDetailStatement.IS_EXCHANGE_OFFICE);
    }

    @Override
    public String getShortDescription() {
        return mCursor.getString(PartnerDetailStatement.SHORT_DESCRIPTION);
    }

    @Override
    public long getMainCategory() {
        return mCursor.getLong(PartnerDetailStatement.MAIN_CATEGORY);
    }

    @Override
    public String getAddress() {
        // TODO use DisplayUtil.formatAddress
        String street = mCursor.getString(PartnerDetailStatement.STREET);
        String zipCode = mCursor.getString(PartnerDetailStatement.ZIP_CODE);
        String city = mCursor.getString(PartnerDetailStatement.CITY);
        builder.setLength(0);
        if (!TextUtils.isEmpty(street) && !TextUtils.isEmpty(zipCode) && !TextUtils.isEmpty(city)) {
            builder.append(street);
            builder.append(", ");
            builder.append(zipCode);
            builder.append(" ");
            builder.append(city);
        }
        return builder.toString();
    }

    @Override
    public String getCategoryLabel() {
        return mCursor.getString(PartnerDetailStatement.CATEGORY_LABEL);
    }

    @Override
    public String getCategoryIcon() {
        return mCursor.getString(PartnerDetailStatement.CATEGORY_ICON);
    }
}
