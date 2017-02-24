package com.zxcv.gonette.parser;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.parser.base.JsonParser;
import com.zxcv.gonette.parser.base.ToOperationsJsonParser;

import java.io.IOException;
import java.util.List;

public class PartnerJsonParser
        extends ToOperationsJsonParser {

    public static final String PROPERTIES = "properties";

    public static final String GEOMETRY = "geometry";

    private JsonParser mPropertiesJsonParser;

    private JsonParser mGeometryJsonParser;

    public PartnerJsonParser(
            @NonNull List<ContentProviderOperation> operations,
            @NonNull ContentValues partnerContentValues,
            @NonNull JsonParser propertiesJsonParser,
            @NonNull JsonParser geometryJsonParser) {
        super(operations, partnerContentValues, GonetteContract.Partner.CONTENT_URI);
        mPropertiesJsonParser = propertiesJsonParser;
        mGeometryJsonParser = geometryJsonParser;
    }

    @Override
    protected boolean onParse(@NonNull String name, @NonNull JsonReader jsonReader)
            throws IOException {
        if (PROPERTIES.equals(name)) {
            mPropertiesJsonParser.parse(jsonReader);
            return true;
        } else if (GEOMETRY.equals(name)) {
            mGeometryJsonParser.parse(jsonReader);
            return true;
        } else {
            return false;
        }
    }

}
