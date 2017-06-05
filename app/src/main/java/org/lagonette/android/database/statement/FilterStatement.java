package org.lagonette.android.database.statement;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.database.Tables;
import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.CategoryMetadataColumns;
import org.lagonette.android.database.columns.FilterColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.database.columns.PartnerSideCategoriesColumns;
import org.lagonette.android.helper.SqlBuilder;

public class FilterStatement implements FilterColumns {

    private static final String SIDE_PARTNER_METADATA = "side_partner_metadata";

    private static final String MAIN_PARTNER_METADATA = "main_partner_metadata";

    private static final String SIDE_PARTNER = "side_partner";

    private static final String MAIN_PARTNER = "main_partner";

    public static String getFilterStatement() {
        return new SqlBuilder().build()
                .startGroup()

                // CATEGORIES
                .select()
                    .column(String.valueOf(VALUE_ROW_CATEGORY), ROW_TYPE)
                    .column(CategoryColumns.ID)
                    .column(CategoryColumns.LABEL)
                    .column(CategoryColumns.ICON)
                    .column(CategoryMetadataColumns.IS_VISIBLE)
                    .column(CategoryMetadataColumns.IS_COLLAPSED)
                    .sum(MAIN_PARTNER_METADATA + "." + PartnerMetadataColumns.IS_VISIBLE, FilterColumns.MAIN_PARTNER_VISIBILITY_SUM)
                    .sum(SIDE_PARTNER_METADATA + "." + PartnerMetadataColumns.IS_VISIBLE, FilterColumns.SIDE_PARTNER_VISIBILITY_SUM)
                    .column(VALUE_NULL, PartnerColumns.ID)
                    .column(VALUE_NULL, PartnerColumns.NAME)
                    .column(VALUE_NULL, PartnerColumns.ADDRESS)
                    .column(VALUE_NULL, PartnerColumns.ZIP_CODE)
                    .column(VALUE_NULL, PartnerColumns.CITY)
                    .column(VALUE_NULL, PartnerColumns.IS_EXCHANGE_OFFICE)
                    .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
                .from(Tables.CATEGORY)
                .join(Tables.CATEGORY_METADATA)
                    .on(CategoryColumns.ID, LaGonetteContract.CategoryMetadata.CATEGORY_ID)
                .leftJoin(Tables.PARTNER, MAIN_PARTNER)
                    .on(CategoryColumns.ID, MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY)
                .leftJoin(Tables.PARTNER_METADATA, MAIN_PARTNER_METADATA)
                    .on(MAIN_PARTNER + "." + PartnerColumns.ID, MAIN_PARTNER_METADATA + "." + PartnerMetadataColumns.PARTNER_ID)
                .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                    .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
                .leftJoin(Tables.PARTNER, SIDE_PARTNER)
                    .on(PartnerSideCategoriesColumns.PARTNER_ID, SIDE_PARTNER + "." + PartnerColumns.ID)
                .leftJoin(Tables.PARTNER_METADATA, SIDE_PARTNER_METADATA)
                    .on(SIDE_PARTNER + "." + PartnerColumns.ID, SIDE_PARTNER_METADATA + "." + PartnerMetadataColumns.PARTNER_ID)
                .where()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY).isNotNull()
                    .and()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.NAME).like().wildcard()
                    .or()
                    .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull() // TODO Maybe test SIDE_PARTNER + "." + PartnerColumns.NAME
                    .and()
                    .statement(SIDE_PARTNER + "." + PartnerColumns.NAME).like().wildcard()
                .groupBy().by(CategoryColumns.ID)

                .union()

                // Footers
                .select()
                    .column(String.valueOf(VALUE_ROW_FOOTER), ROW_TYPE)
                    .column(CategoryColumns.ID)
                    .column(VALUE_NULL, CategoryColumns.LABEL)
                    .column(VALUE_NULL, CategoryColumns.ICON)
                    .column(VALUE_NULL, CategoryMetadataColumns.IS_VISIBLE)
                    .column(VALUE_NULL, CategoryMetadataColumns.IS_COLLAPSED)
                    .column(VALUE_NULL, FilterColumns.MAIN_PARTNER_VISIBILITY_SUM)
                    .column(VALUE_NULL, FilterColumns.SIDE_PARTNER_VISIBILITY_SUM)
                    .column(VALUE_NULL, PartnerColumns.ID)
                    .column(VALUE_NULL, PartnerColumns.NAME)
                    .column(VALUE_NULL, PartnerColumns.ADDRESS)
                    .column(VALUE_NULL, PartnerColumns.ZIP_CODE)
                    .column(VALUE_NULL, PartnerColumns.CITY)
                    .column(VALUE_NULL, PartnerColumns.IS_EXCHANGE_OFFICE)
                    .column(VALUE_NULL, PartnerMetadataColumns.IS_VISIBLE)
                .from(Tables.CATEGORY)
                .leftJoin(Tables.PARTNER, MAIN_PARTNER)
                    .on(CategoryColumns.ID, MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY)
                .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                    .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
                .leftJoin(Tables.PARTNER, SIDE_PARTNER)
                    .on(PartnerSideCategoriesColumns.PARTNER_ID, SIDE_PARTNER + "." + PartnerColumns.ID)
                .where()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY).isNotNull()
                    .and()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.NAME).like().wildcard()
                    .or()
                    .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull()
                    .and()
                    .statement(SIDE_PARTNER + "." + PartnerColumns.NAME).like().wildcard()
                .groupBy().by(CategoryColumns.ID)

                .union()

                // MAIN PARTNERS
                .select()
                    .column(String.valueOf(VALUE_ROW_MAIN_PARTNER), ROW_TYPE)
                    .column(CategoryColumns.ID)
                    .column(VALUE_NULL, CategoryColumns.LABEL)
                    .column(VALUE_NULL, CategoryColumns.ICON)
                    .column(CategoryMetadataColumns.IS_VISIBLE)
                    .column(VALUE_NULL, CategoryMetadataColumns.IS_COLLAPSED)
                    .column(VALUE_NULL, FilterColumns.MAIN_PARTNER_VISIBILITY_SUM)
                    .column(VALUE_NULL, FilterColumns.SIDE_PARTNER_VISIBILITY_SUM)
                    .column(PartnerColumns.ID)
                    .column(PartnerColumns.NAME)
                    .column(PartnerColumns.ADDRESS)
                    .column(PartnerColumns.ZIP_CODE)
                    .column(PartnerColumns.CITY)
                    .column(PartnerColumns.IS_EXCHANGE_OFFICE)
                    .column(PartnerMetadataColumns.IS_VISIBLE)
                .from(Tables.CATEGORY)
                .join(Tables.CATEGORY_METADATA)
                    .on(CategoryColumns.ID, LaGonetteContract.CategoryMetadata.CATEGORY_ID)
                .join(Tables.PARTNER)
                    .on(CategoryColumns.ID, PartnerColumns.MAIN_CATEGORY)
                .join(Tables.PARTNER_METADATA)
                    .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)
                .where()
                    .statement(PartnerColumns.NAME).like().wildcard()
                    .and()
                    .statement(LaGonetteContract.CategoryMetadata.IS_COLLAPSED + " = 0")

                .union()

                // SIDE PARTNERS
                .select()
                    .column(String.valueOf(VALUE_ROW_SIDE_PARTNER), ROW_TYPE)
                    .column(CategoryColumns.ID)
                    .column(VALUE_NULL, CategoryColumns.LABEL)
                    .column(VALUE_NULL, CategoryColumns.ICON)
                    .column(CategoryMetadataColumns.IS_VISIBLE)
                    .column(VALUE_NULL, CategoryMetadataColumns.IS_COLLAPSED)
                    .column(VALUE_NULL, FilterColumns.MAIN_PARTNER_VISIBILITY_SUM)
                    .column(VALUE_NULL, FilterColumns.SIDE_PARTNER_VISIBILITY_SUM)
                    .column(PartnerColumns.ID)
                    .column(PartnerColumns.NAME)
                    .column(PartnerColumns.ADDRESS)
                    .column(PartnerColumns.ZIP_CODE)
                    .column(PartnerColumns.CITY)
                    .column(PartnerColumns.IS_EXCHANGE_OFFICE)
                    .column(PartnerMetadataColumns.IS_VISIBLE)
                .from(Tables.CATEGORY)
                .join(Tables.CATEGORY_METADATA)
                    .on(CategoryColumns.ID, LaGonetteContract.CategoryMetadata.CATEGORY_ID)
                .join(Tables.PARTNER_SIDE_CATEGORIES)
                    .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
                .join(Tables.PARTNER)
                    .on(PartnerSideCategoriesColumns.PARTNER_ID, PartnerColumns.ID)
                .join(Tables.PARTNER_METADATA)
                    .on(PartnerColumns.ID, PartnerMetadataColumns.PARTNER_ID)
                .where()
                    .statement(PartnerColumns.NAME).like().wildcard()
                    .and()
                    .statement(LaGonetteContract.CategoryMetadata.IS_COLLAPSED + " = 0")

                .order()
                    .by(CategoryColumns.ID, true)
                    .by(ROW_TYPE, true)

                .endGroup()
                .toString();
    }

    public static String[] getSelectionsArgs(@Nullable String search) {
        search = !TextUtils.isEmpty(search) ? "%" + search + "%" : "%";
        return new String[]{
                search,
                search,
                search,
                search,
                search,
                search
        };
    }
}
