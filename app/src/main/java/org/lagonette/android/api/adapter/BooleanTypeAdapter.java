package org.lagonette.android.api.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    private final String TRUE_STRING = "1";

    @Override
    public Boolean read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return false;
        }
        String stringValue = reader.nextString();
        try {
            return TRUE_STRING.equals(stringValue);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException {
        if (value == null) {
            value = Boolean.FALSE;
        }
        writer.value(value);
    }

}