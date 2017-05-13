package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.GonetteContract;

public class FilterReader extends CursorReader {

    public static FilterReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new FilterReader(cursor)
                : null;
    }

    @NonNull
    public CategoryReader categoryReader;

    @NonNull
    public PartnerReader partnerReader;

    public FilterReader(@NonNull Cursor cursor) {
        super(cursor);
        categoryReader = new CategoryReader(cursor);
        partnerReader = new PartnerReader(cursor);
    }

    public int getRowType() {
        return mCursor.getInt(
                mCursor.getColumnIndex(
                        GonetteContract.Filter.ROW_TYPE
                )
        );
    }

}
