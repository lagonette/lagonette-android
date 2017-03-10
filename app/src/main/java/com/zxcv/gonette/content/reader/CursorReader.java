package com.zxcv.gonette.content.reader;

import android.database.Cursor;

public abstract class CursorReader {

    protected Cursor mCursor;

    public CursorReader(Cursor mCursor) {
        this.mCursor = mCursor;
    }

    public boolean moveToNext() {
        return mCursor.moveToNext();
    }

    public boolean moveToPosition(int positon) {
        return mCursor.moveToPosition(positon);
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
