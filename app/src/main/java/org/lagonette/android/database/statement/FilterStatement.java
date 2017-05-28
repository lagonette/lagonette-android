package org.lagonette.android.database.statement;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lagonette.android.database.Tables;
import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.FilterColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.database.columns.PartnerSideCategoriesColumns;
import org.lagonette.android.util.SqlUtil;

public class FilterStatement implements FilterColumns {

    private static final String SIDE_PARTNER = "side_partner";

    private static final String MAIN_PARTNER = "main_partner";

    public static String getFilterStatement() {
        return SqlUtil.build()
                .startGroup()

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
                .leftJoin(Tables.PARTNER, MAIN_PARTNER)
                    .on(CategoryColumns.ID, MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY)
                .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                    .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
                .leftJoin(Tables.PARTNER, SIDE_PARTNER)
                    .on(PartnerSideCategoriesColumns.PARTNER_ID, SIDE_PARTNER + "." + PartnerColumns.ID)
                .where()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY).isNotNull()
                    .and()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.NAME + " LIKE ?")
                    .or()
                    .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull()
                    .and()
                    .statement(SIDE_PARTNER + "." + PartnerColumns.NAME + " LIKE ?")
                .groupBy().by(CategoryColumns.ID)

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
                .leftJoin(Tables.PARTNER, MAIN_PARTNER)
                    .on(CategoryColumns.ID, MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY)
                .leftJoin(Tables.PARTNER_SIDE_CATEGORIES)
                    .on(CategoryColumns.ID, PartnerSideCategoriesColumns.CATEGORY_ID)
                .leftJoin(Tables.PARTNER, SIDE_PARTNER)
                    .on(PartnerSideCategoriesColumns.PARTNER_ID, SIDE_PARTNER + "." + PartnerColumns.ID)
                .where()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.MAIN_CATEGORY).isNotNull()
                    .and()
                    .statement(MAIN_PARTNER + "." + PartnerColumns.NAME + " LIKE ?")
                    .or()
                    .statement(PartnerSideCategoriesColumns.CATEGORY_ID).isNotNull()
                    .and()
                    .statement(SIDE_PARTNER + "." + PartnerColumns.NAME + " LIKE ?")
                .groupBy().by(CategoryColumns.ID)

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
                .where()
                    .statement(PartnerColumns.NAME + " LIKE ?")

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
                .where()
                    .statement(PartnerColumns.NAME + " LIKE ?")

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
