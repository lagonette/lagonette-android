package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.base.CursorReader;

public class CategoryReader
        extends CursorReader {

    @Nullable
    public static CategoryReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new CategoryReader(cursor)
                : null;
    }

    @NonNull
    public final CategoryMetadataReader metadataReader;

    public CategoryReader(@NonNull Cursor cursor) {
        super(cursor);
        metadataReader = CategoryMetadataReader.create(cursor);
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
}
