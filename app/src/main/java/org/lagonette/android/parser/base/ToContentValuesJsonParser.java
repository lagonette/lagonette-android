package org.lagonette.android.parser.base;

import android.content.ContentValues;
import android.support.annotation.NonNull;

public abstract class ToContentValuesJsonParser
        extends ObjectJsonParser {

    @NonNull
    protected ContentValues mContentValues;

    public ToContentValuesJsonParser(@NonNull ContentValues contentValues) {
        mContentValues = contentValues;
    }
}
