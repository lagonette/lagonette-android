package com.gonette.android.parser.base;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public abstract class ObjectJsonParser
        implements JsonParser {

    private static final String TAG = "ObjectJsonParser";

    @Override
    public void parse(@NonNull JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if ("type".equals(name)) {
                Log.d(TAG, "parse: Skip " + name + ": " + jsonReader.nextString());
            } else if (!onParse(name, jsonReader)) {
                Log.d(TAG, "parse: Skip " + name);
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
    }

    protected abstract boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException;
}
