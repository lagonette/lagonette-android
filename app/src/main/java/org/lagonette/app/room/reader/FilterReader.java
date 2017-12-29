package org.lagonette.app.room.reader;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.room.embedded.Address;
import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.reader.base.CursorReader;
import org.lagonette.app.room.statement.FilterStatement;

//TODO Make entity reader and build them with field position
//TODO Maybe invent our own streamable object
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
    public CategoryKey getCategoryKey() {
        return getCategoryKey(new CategoryKey());
    }

    @Override
    public CategoryKey getCategoryKey(@NonNull CategoryKey recycle) {
        recycle.id = mCursor.getLong(FilterStatement.CATEGORY_ID);
        recycle.type = mCursor.getLong(FilterStatement.CATEGORY_TYPE);
        return recycle;
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
    public boolean isCategoryPartnersVisible() {
        return 0 != mCursor.getInt(FilterStatement.CATEGORY_IS_PARTNERS_VISIBLE);
    }

    @Override
    public long getLocationId() {
        return mCursor.getLong(FilterStatement.LOCATION_ID);
    }

    @Override
    public String getPartnerName() {
        return mCursor.getString(FilterStatement.PARTNER_NAME);
    }

    @NonNull
    @Override
    public String getLocationAddress(@NonNull Resources resources) {
        Address address = FilterStatement.from(mCursor);
        return address.format(resources);
    }

    @Override
    public boolean isLocationExchangeOffice() {
        return 0 != mCursor.getInt(FilterStatement.LOCATION_IS_EXCHANGE_OFFICE);
    }

    @Override
    public boolean isLocationVisible() {
        return 0 != mCursor.getInt(FilterStatement.LOCATION_METADATA_IS_VISIBLE);
    }
}
