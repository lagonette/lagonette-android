package org.lagonette.android.room.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.android.room.reader.base.CursorReader;
import org.lagonette.android.room.statement.FilterStatement;

// TODO Make entity reader and build them with field position
// TODO Maybe invent our own streamable object
public class FilterReader
        extends CursorReader
        implements FilterStatement.Contract {

    @NonNull
    private final StringBuilder builder;

    public FilterReader(@NonNull Cursor cursor) {
        super(cursor);
        this.builder = new StringBuilder();
    }

    @Nullable
    public static FilterReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new FilterReader(cursor)
                : null;
    }

    @Override
    public int getRowType() {
        return mCursor.getInt(FilterStatement.ROW_TYPE);
    }

    @Override
    public long getCategoryId() {
        return mCursor.getLong(FilterStatement.CATEGORY_ID);
    }

    @Override
    public String getCategoryLabel() {
        return mCursor.getString(FilterStatement.CATEGORY_LABEL);
    }

    @Override
    public String getCategoryIcon() {
        return mCursor.getString(FilterStatement.CATEGORY_ICON);
    }

    @Override
    public boolean isCategoryVisible() {
        return 0 != mCursor.getInt(FilterStatement.CATEGORY_METADATA_IS_VISIBLE);
    }

    @Override
    public boolean isCategoryCollapsed() {
        return 0 != mCursor.getInt(FilterStatement.CATEGORY_METADATA_IS_COLLAPSED);
    }

    @Override
    public int getMainPartnerVisibilitySum() {
        return mCursor.getInt(FilterStatement.MAIN_PARTNER_VISIBILITY_SUM);
    }

    @Override
    public int getSidePartnerVisibilitySum() {
        return mCursor.getInt(FilterStatement.SIDE_PARTNER_VISIBILITY_SUM);
    }

    @Override
    public long getPartnerId() {
        return mCursor.getLong(FilterStatement.PARTNER_ID);
    }

    @Override
    public String getPartnerName() {
        return mCursor.getString(FilterStatement.PARTNER_NAME);
    }

    @Override
    public String getPartnerAddress() {
        // TODO use DisplayUtil.formatAddress
        String street = mCursor.getString(FilterStatement.PARTNER_STREET);
        String zipCode = mCursor.getString(FilterStatement.PARTNER_ZIP_CODE);
        String city = mCursor.getString(FilterStatement.PARTNER_CITY);
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
    public boolean isPartnerExchangeOffice() {
        return 0 != mCursor.getInt(FilterStatement.PARTNER_IS_EXCHANGE_OFFICE);
    }

    @Override
    public boolean isPartnerVisible() {
        return 0 != mCursor.getInt(FilterStatement.PARTNER_METADATA_IS_VISIBLE);
    }
}