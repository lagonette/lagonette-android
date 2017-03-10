package com.zxcv.gonette.content;

import com.zxcv.gonette.database.PartnerColumns;
import com.zxcv.gonette.database.PartnerMetadataColumns;
import com.zxcv.gonette.database.Tables;

public abstract class GonetteContentProviderHelper {

    public static String getPartnerWithMetadataStatement() {
        return Tables.PARTNER +
                " JOIN " + Tables.PARTNER_METADATA +
                " ON " + PartnerColumns.ID + " = " + PartnerMetadataColumns.PARTNER_ID;
    }

}
