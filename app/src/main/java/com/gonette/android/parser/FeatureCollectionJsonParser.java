package com.gonette.android.parser;

import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.gonette.android.parser.base.JsonParser;
import com.gonette.android.parser.base.ObjectJsonParser;

import java.io.IOException;

public class FeatureCollectionJsonParser
        extends ObjectJsonParser {

    private static final String TAG = "FeatureCollectionJsonPa";

    private static final String FEATURES = "features";

    private JsonParser mFeatureParser;

    public FeatureCollectionJsonParser(@NonNull JsonParser featureParser) {
        mFeatureParser = featureParser;
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (FEATURES.equals(name)) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                mFeatureParser.parse(jsonReader);
            }
            jsonReader.endArray();
            return true;
        } else {
            return false;
        }
    }
}
