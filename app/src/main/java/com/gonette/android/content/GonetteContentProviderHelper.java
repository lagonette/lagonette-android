package com.gonette.android.content;

import com.gonette.android.database.PartnerColumns;
import com.gonette.android.database.PartnerMetadataColumns;
import com.gonette.android.database.Tables;

public abstract class GonetteContentProviderHelper {

    public static String getPartnerWithMetadataStatement() {
        return Tables.PARTNER +
                " JOIN " + Tables.PARTNER_METADATA +
                " ON " + PartnerColumns.ID + " = " + PartnerMetadataColumns.PARTNER_ID;
    }

}
