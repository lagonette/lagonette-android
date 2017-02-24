package com.zxcv.gonette.parser;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.parser.base.JsonParser;

import java.io.IOException;
import java.util.List;

public class FootprintCoordinatesJsonParser
        implements JsonParser {

    @NonNull
    protected List<ContentProviderOperation> mOperations;

    @NonNull
    protected ContentValues mContentValues;

    public FootprintCoordinatesJsonParser(
            @NonNull List<ContentProviderOperation> operations,
            @NonNull ContentValues contentValues) {
        mOperations = operations;
        mContentValues = contentValues;
    }

    @Override
    public void parse(@NonNull JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            mContentValues.clear();
            jsonReader.beginArray();
            mContentValues.put(GonetteContract.Footprint.LATITUDE, jsonReader.nextDouble());
            mContentValues.put(GonetteContract.Footprint.LONGITUDE, jsonReader.nextDouble());
            jsonReader.endArray();
            mOperations.add(
                    ContentProviderOperation.newInsert(GonetteContract.Footprint.CONTENT_URI)
                                            .withValues(mContentValues)
                                            .withYieldAllowed(true)
                                            .build()
            );
        }
        jsonReader.endArray();
    }
}
