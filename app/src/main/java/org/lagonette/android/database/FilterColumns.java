package org.lagonette.android.database;

import android.support.annotation.IntDef;

import org.lagonette.android.util.SqlUtil;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface FilterColumns {

    String VALUE_NULL = "NULL";

    String ROW_TYPE = "row_type";

    int VALUE_ROW_CATEGORY = 0;

    int VALUE_ROW_MAIN_PARTNER = 1;

    int VALUE_ROW_SIDE_PARTNER = 2;

    int VALUE_ROW_FOOTER = 3;

    int ROW_TYPE_COUNT = 4;

    @Retention(SOURCE)
    @IntDef({
            VALUE_ROW_CATEGORY,
            VALUE_ROW_MAIN_PARTNER,
            VALUE_ROW_SIDE_PARTNER,
            VALUE_ROW_FOOTER
    })
    public @interface RowType {
    }


    String SQL = SqlUtil.build()
            .createViewAs(Views.FILTERS)

            // CATEGORIES
            .select()
                .column(String.valueOf(VALUE_ROW_CATEGORY), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(CategoryColumns.LABEL)
                .column(CategoryColumns.ICON)
                .column(VALUE_NULL, PartnerColumns.ID)
                .column(VALUE_NULL, PartnerColumns.NAME)
                .column(VALUE_NULL, PartnerColumns.ADDRESS)
                .column(VALUE_NULL, PartnerColumns.ZIP_CODE)
                .column(VALUE_NULL, PartnerColumns.CITY)
                .column(VALUE_NULL, PartnerColumns.IS_EXCHANGE_OFFICE)
                .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)
            .leftJoin(Tables.PARTNER)
                .on(CategoryColumns.ID, PartnerColumns.MAIN_CATEGORY)
            .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
            .where()
                .statement(PartnerColumns.MAIN_CATEGORY).isNotNull().or()
                .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull()

            .union()

            // Footers
            .select()
                .column(String.valueOf(VALUE_ROW_FOOTER), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(VALUE_NULL, CategoryColumns.LABEL)
                .column(VALUE_NULL, CategoryColumns.ICON)
                .column(VALUE_NULL, PartnerColumns.ID)
                .column(VALUE_NULL, PartnerColumns.NAME)
                .column(VALUE_NULL, PartnerColumns.ADDRESS)
                .column(VALUE_NULL, PartnerColumns.ZIP_CODE)
                .column(VALUE_NULL, PartnerColumns.CITY)
                .column(VALUE_NULL, PartnerColumns.IS_EXCHANGE_OFFICE)
                .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)
            .leftJoin(Tables.PARTNER)
                .on(CategoryColumns.ID, PartnerColumns.MAIN_CATEGORY)
            .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
            .where()
                .statement(PartnerColumns.MAIN_CATEGORY).isNotNull().or()
                .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull()

            .union()

            // MAIN PARTNERS
            .select()
                .column(String.valueOf(VALUE_ROW_MAIN_PARTNER), ROW_TYPE)
                .column(CategoryColumns.ID)
                .column(VALUE_NULL, CategoryColumns.LABEL)
                .column(VALUE_NULL, CategoryColumns.ICON)
                .column(PartnerColumns.ID)
                .column(PartnerColumns.NAME)
                .column(PartnerColumns.ADDRESS)
                .column(PartnerColumns.ZIP_CODE)
                .column(PartnerColumns.CITY)
                .column(PartnerColumns.IS_EXCHANGE_OFFICE)
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
                .column(VALUE_NULL, CategoryColumns.ICON)
                .column(PartnerColumns.ID)
                .column(PartnerColumns.NAME)
                .column(PartnerColumns.ADDRESS)
                .column(PartnerColumns.ZIP_CODE)
                .column(PartnerColumns.CITY)
                .column(PartnerColumns.IS_EXCHANGE_OFFICE)
                .column(PartnerMetadataColumns.IS_VISIBLE)
            .from(Tables.CATEGORY)
            .join(Tables.PARTNER_SIDE_CATEGORIES)
                .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
            .join(Tables.PARTNER)
                .on(PartnerSideCategoriesColumns.PARTNER_ID, PartnerColumns.ID)
            .join(Tables.PARTNER_METADATA)
                .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)

            .order()
                .by(CategoryColumns.ID, true)
                .by(ROW_TYPE, true)

            .toString();

}
