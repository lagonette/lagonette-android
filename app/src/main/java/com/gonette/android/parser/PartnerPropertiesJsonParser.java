package com.gonette.android.parser;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.gonette.android.content.contract.GonetteContract;
import com.gonette.android.parser.base.ToContentValuesJsonParser;

import java.io.IOException;

public class PartnerPropertiesJsonParser
        extends ToContentValuesJsonParser {

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public PartnerPropertiesJsonParser(ContentValues partnerContentValues) {
        super(partnerContentValues);
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (NAME.equals(name)) {
            mContentValues.put(GonetteContract.Partner.NAME, jsonReader.nextString());
            return true;
        } else if (DESCRIPTION.equals(name)) {
            mContentValues.put(GonetteContract.Partner.DESCRIPTION, jsonReader.nextString());
            return true;
        } else {
            return false;
        }
    }
}
