package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.base.CursorReader;

public class CategoryMetadataReader extends CursorReader {

    @Nullable
    public static CategoryMetadataReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new CategoryMetadataReader(cursor)
                : null;
    }

    public CategoryMetadataReader(@NonNull Cursor cursor) {
        super(cursor);
    }

    public boolean isVisible() {
        return 1 == mCursor.getLong(
                mCursor.getColumnIndex(
                        LaGonetteContract.CategoryMetadata.IS_VISIBLE
                )
        );
    }

    public boolean isCollapsed() {
        return 1 == mCursor.getLong(
                mCursor.getColumnIndex(
                        LaGonetteContract.CategoryMetadata.IS_COLLAPSED
                )
        );
    }
}
