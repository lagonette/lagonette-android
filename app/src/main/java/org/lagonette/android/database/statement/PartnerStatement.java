package org.lagonette.android.database.statement;

import org.lagonette.android.database.Tables;
import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.util.SqlUtil;

public class PartnerStatement {

    public static String getExtendedPartnerStatement() {
        return SqlUtil.build()
                .startGroup()
                .select()
                    .allColumns()
                .from(Tables.PARTNER)
                .join(Tables.PARTNER_METADATA)
                    .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)
                .join(Tables.CATEGORY)
                    .on(PartnerColumns.MAIN_CATEGORY, CategoryColumns.ID)
                .endGroup()
                .toString();
    }
}
