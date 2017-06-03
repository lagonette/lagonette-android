package org.lagonette.android.database.statement;

import org.lagonette.android.database.Tables;
import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.CategoryMetadataColumns;
import org.lagonette.android.database.columns.MapColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.database.columns.PartnerSideCategoriesColumns;
import org.lagonette.android.helper.SqlBuilder;

public class MapStatement {

    private static final String SIDE_CATEGORY_METADATA = "side_category_metadata";

    private static final String MAIN_CATEGORY_METADATA = "main_category_metadata";

    private static final String SIDE_CATEGORY = "side_category";

    private static final String MAIN_CATEGORY = "main_category";

    // TODO: How to ensure selected columns by adapter are the right ones because he do not take account of table label ?
    // TODO: Maybe Link a statement with CP, StatementReader, StatementColumns, etc
    public static String getMapsPartnerStatement() {
        return new SqlBuilder().build()
                .startGroup()
                    .select()
                        .allColumns()
                        .sum(SIDE_CATEGORY_METADATA + "." + CategoryMetadataColumns.IS_VISIBLE, MapColumns.SIDE_CATEGORY_METADATA_IS_VISIBLE_SUM)
                    .from(Tables.PARTNER)
                    .join(Tables.PARTNER_METADATA)
                        .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)

                    .join(Tables.CATEGORY, MAIN_CATEGORY)
                        .on(PartnerColumns.MAIN_CATEGORY, MAIN_CATEGORY + "." + CategoryColumns.ID)
                    .join(Tables.CATEGORY_METADATA, MAIN_CATEGORY_METADATA)
                        .on(MAIN_CATEGORY + "." + CategoryColumns.ID, MAIN_CATEGORY_METADATA + "." + CategoryMetadataColumns.CATEGORY_ID)

                    .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                        .on(PartnerSideCategoriesColumns.PARTNER_ID, PartnerColumns.ID)
                    .leftJoin(Tables.CATEGORY, SIDE_CATEGORY)
                        .on(PartnerSideCategoriesColumns.CATEGORY_ID, SIDE_CATEGORY + "." + CategoryColumns.ID)
                    .leftJoin(Tables.CATEGORY_METADATA, SIDE_CATEGORY_METADATA)
                        .on(SIDE_CATEGORY + "." + CategoryColumns.ID, SIDE_CATEGORY_METADATA + "." + CategoryMetadataColumns.CATEGORY_ID)

                    .groupBy()
                        .by(PartnerColumns.ID)
                .endGroup()
                .toString();
    }
}
