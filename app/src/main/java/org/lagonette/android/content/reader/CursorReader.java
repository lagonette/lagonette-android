package org.lagonette.android.content.reader;

import android.database.Cursor;
import android.support.annotation.NonNull;

public abstract class CursorReader {

    @NonNull
    protected Cursor mCursor;

    public CursorReader(@NonNull Cursor cursor) {
        this.mCursor = cursor;
    }

    public boolean moveToNext() {
        return mCursor.moveToNext();
    }

    public boolean moveToPosition(int position) {
        return mCursor.moveToPosition(position);
    }

    public int getPosition() {
        return mCursor.getPosition();
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public boolean moveToFirst() {
        return mCursor.moveToFirst();
    }

}
