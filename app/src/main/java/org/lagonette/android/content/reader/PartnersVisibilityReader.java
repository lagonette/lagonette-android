package org.lagonette.android.content.reader;


import android.database.Cursor;
import android.support.annotation.Nullable;

public class PartnersVisibilityReader extends CursorReader {

    public static final String SELECTION_PARTNERS_VISIBILITY_COUNT = "partners_visibility_count";

    public static String getPartnerVisibilityCountProjection() {
        return "COUNT(1) AS " + SELECTION_PARTNERS_VISIBILITY_COUNT;
    }

    @Nullable
    public static PartnersVisibilityReader create(@Nullable Cursor cursor) {
        return cursor != null
                ? new PartnersVisibilityReader(cursor)
                : null;
    }

    public PartnersVisibilityReader(Cursor mCursor) {
        super(mCursor);
    }

    public int getPartnersVisibilityCount() {
        return mCursor.getInt(
                mCursor.getColumnIndex(
                        SELECTION_PARTNERS_VISIBILITY_COUNT
                )
        );
    }
}
