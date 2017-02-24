package com.zxcv.gonette.parser;

import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.parser.base.JsonParser;
import com.zxcv.gonette.parser.base.ObjectJsonParser;

import java.io.IOException;

public class FootprintGeometryJsonParser
        extends ObjectJsonParser {

    public static final String COORDINATES = "coordinates";

    private JsonParser mCoordinatesJsonParser;

    public FootprintGeometryJsonParser(@NonNull JsonParser coordinatesJsonParser) {
        mCoordinatesJsonParser = coordinatesJsonParser;
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (COORDINATES.equals(name)) {
            mCoordinatesJsonParser.parse(jsonReader);
            return true;
        } else {
            return false;
        }
    }
}
