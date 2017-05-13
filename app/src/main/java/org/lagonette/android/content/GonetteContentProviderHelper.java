package org.lagonette.android.content;

import org.lagonette.android.database.CategoryColumns;
import org.lagonette.android.database.PartnerColumns;
import org.lagonette.android.database.PartnerMetadataColumns;
import org.lagonette.android.database.Tables;

public abstract class GonetteContentProviderHelper {

    public static String getExtendedPartnerStatement() {
        return Tables.PARTNER +
                " JOIN " + Tables.PARTNER_METADATA +
                " ON " + PartnerColumns.ID + " = " + PartnerMetadataColumns.PARTNER_ID +
                " JOIN " + Tables.CATEGORY +
                " ON " + PartnerColumns.MAIN_CATEGORY + " = " + CategoryColumns.ID;
    }

}
