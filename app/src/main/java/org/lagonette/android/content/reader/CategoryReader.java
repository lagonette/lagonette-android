package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.LaGonetteContract;

public class CategoryReader
        extends CursorReader {

    public static CategoryReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new CategoryReader(cursor)
                : null;
    }

    public CategoryReader(@NonNull Cursor cursor) {
        super(cursor);
    }

    public long getId() {
        return mCursor.getLong(
                mCursor.getColumnIndex(
                        LaGonetteContract.Category.ID
                )
        );
    }

    @NonNull
    public String getLabel() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Category.LABEL
                )
        );
    }

    @NonNull
    public String getIcon() {
        return mCursor.getString(
                mCursor.getColumnIndex(
                        LaGonetteContract.Category.ICON
                )
        );
    }

    // TODO Use a metadata reader
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
