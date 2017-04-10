package org.lagonette.android.content.reader;


import android.database.Cursor;

public class PartnersVisibilityReader extends CursorReader {

    public static final String SELECTION_PARTNERS_VISIBILITY_COUNT = "partners_visibility_count";

    public static String getPartnerVisibilityCountProjection() {
        return "COUNT(1) AS " + SELECTION_PARTNERS_VISIBILITY_COUNT;
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
