package org.lagonette.android.database;

import org.lagonette.android.util.SqlUtil;

public interface FilterColumns {

    String VALUE_NULL = "NULL";

    String ROW_TYPE = "row_type";

    int VALUE_ROW_CATEGORY = 0;

    int VALUE_ROW_MAIN_PARTNER = 1;

    int VALUE_ROW_SIDE_PARTNER = 2;

    int VALUE_ROW_FOOTER = 3;

    String SQL = SqlUtil.build()
            .createViewAs(Views.FILTERS)

            // CATEGORIES
            .select()
                .column(String.valueOf(VALUE_ROW_CATEGORY), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(CategoryColumns.LABEL)
                .column(VALUE_NULL, PartnerColumns.ID)
                .column(VALUE_NULL, PartnerColumns.NAME)
                .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)

            .union()

            // MAIN PARTNERS
            .select()
                .column(String.valueOf(VALUE_ROW_MAIN_PARTNER), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(VALUE_NULL, CategoryColumns.LABEL)
                .column(PartnerColumns.ID)
                .column(PartnerColumns.NAME)
                .column(PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)
            .join(Tables.PARTNER)
                .on(CategoryColumns.ID, PartnerColumns.MAIN_CATEGORY)
            .join(Tables.PARTNER_METADATA)
                .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)

            .union()

            // SIDE PARTNERS
            .select()
                .column(String.valueOf(VALUE_ROW_SIDE_PARTNER), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(VALUE_NULL, CategoryColumns.LABEL)
                .column(PartnerColumns.ID)
                .column(PartnerColumns.NAME)
                .column(PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)
            .join(Tables.PARTNER_SIDE_CATEGORIES)
                .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
            .join(Tables.PARTNER)
                .on(PartnerSideCategoriesColumns.PARTNER_ID, PartnerColumns.ID)
            .join(Tables.PARTNER_METADATA)
                .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)

            .union()

            // Footers
            .select()
                .column(String.valueOf(VALUE_ROW_FOOTER), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(VALUE_NULL, CategoryColumns.LABEL)
                .column(VALUE_NULL, PartnerColumns.ID)
                .column(VALUE_NULL, PartnerColumns.NAME)
                .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)

            .order()
                .by(CategoryColumns.ID, true)
                .by(ROW_TYPE, true)

            .toString();

}
