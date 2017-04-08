package com.gonette.android.parser.base;

import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public interface JsonParser {

    void parse(@NonNull JsonReader jsonReader) throws IOException;
}
