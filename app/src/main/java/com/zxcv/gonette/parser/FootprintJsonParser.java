package com.zxcv.gonette.parser;

import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.parser.base.JsonParser;
import com.zxcv.gonette.parser.base.ObjectJsonParser;

import java.io.IOException;

public class FootprintJsonParser extends ObjectJsonParser {

    public static final String GEOMETRY = "geometry";

    private JsonParser mGeometryJsonParser;

    public FootprintJsonParser(@NonNull JsonParser geometryJsonParser) {
        mGeometryJsonParser = geometryJsonParser;
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (GEOMETRY.equals(name)) {
            mGeometryJsonParser.parse(jsonReader);
            return true;
        } else {
            return false;
        }
    }
}
