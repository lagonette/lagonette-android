package org.lagonette.android.parser;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.stream.JsonReader;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.parser.base.JsonParser;
import org.lagonette.android.parser.base.ToOperationsJsonParser;

import java.io.IOException;
import java.util.List;

public class PartnerJsonParser
        extends ToOperationsJsonParser {

    public static final String PROPERTIES = "properties";

    public static final String GEOMETRY = "geometry";

    private JsonParser mPropertiesJsonParser;

    private JsonParser mGeometryJsonParser;

    private long currentPartnerId = 0;

    public PartnerJsonParser(
            @NonNull List<ContentProviderOperation> operations,
            @NonNull ContentValues partnerContentValues,
            @NonNull JsonParser propertiesJsonParser,
            @NonNull JsonParser geometryJsonParser) {
        super(operations, partnerContentValues, LaGonetteContract.Partner.CONTENT_URI);
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

    @Override
    protected void onAddOperations() {
        mContentValues.put(LaGonetteContract.Partner.ID, currentPartnerId);
        super.onAddOperations();
        mContentValues.clear();
        mContentValues.put(LaGonetteContract.PartnerMetadata.PARTNER_ID, currentPartnerId);
        mContentValues.put(LaGonetteContract.PartnerMetadata.IS_VISIBLE, true);
        mOperations.add(
                ContentProviderOperation.newInsert(LaGonetteContract.PartnerMetadata.CONTENT_URI)
                        .withValues(mContentValues)
                        .withYieldAllowed(true)
                        .build()
        );
        currentPartnerId++;
    }

}
