package com.gonette.android.parser.base;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.List;

public abstract class ToOperationsJsonParser
        extends ObjectJsonParser {

    @NonNull
    protected List<ContentProviderOperation> mOperations;

    @NonNull
    protected ContentValues mContentValues;

    @NonNull
    private Uri mUri;

    public ToOperationsJsonParser(
            @NonNull List<ContentProviderOperation> operations,
            @NonNull ContentValues contentValues,
            @NonNull Uri uri) {
        mOperations = operations;
        mContentValues = contentValues;
        mUri = uri;
    }

    @Override
    public void parse(@NonNull JsonReader jsonReader) throws IOException {
        onClearContentValues();
        super.parse(jsonReader);
        onAddOperations();
    }

    protected void onClearContentValues() {
        mContentValues.clear();
    }

    protected void onAddOperations() {
        mOperations.add(
                ContentProviderOperation
                        .newInsert(mUri)
                        .withValues(mContentValues)
                        .withYieldAllowed(true)
                        .build()
        );
    }
}
