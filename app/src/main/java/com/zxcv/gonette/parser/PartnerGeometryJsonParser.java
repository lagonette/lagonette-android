package com.zxcv.gonette.parser;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.parser.base.ToContentValuesJsonParser;

import java.io.IOException;

public class PartnerGeometryJsonParser
        extends ToContentValuesJsonParser {

    public static final String COORDINATES = "coordinates";

    public PartnerGeometryJsonParser(ContentValues partnerContentValues) {
        super(partnerContentValues);
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (COORDINATES.equals(name)) {
            jsonReader.beginArray();
            mContentValues.put(GonetteContract.Partner.LATITUDE, jsonReader.nextDouble());
            mContentValues.put(GonetteContract.Partner.LONGITUDE, jsonReader.nextDouble());
            jsonReader.endArray();
            return true;
        } else {
            return false;
        }
    }
}
