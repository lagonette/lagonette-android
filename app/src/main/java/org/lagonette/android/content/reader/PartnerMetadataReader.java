package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.base.CursorReader;

public class PartnerMetadataReader extends CursorReader {

    @Nullable
    public static PartnerMetadataReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new PartnerMetadataReader(cursor)
                : null;
    }

    public PartnerMetadataReader(@NonNull Cursor cursor) {
        super(cursor);
    }

    public boolean isVisible() {
        return 1 == mCursor.getInt(
                mCursor.getColumnIndex(
                        LaGonetteContract.PartnerMetadata.IS_VISIBLE
                )
        );
    }
}
