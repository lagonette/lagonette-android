package org.lagonette.android.database;

public interface FilterColumns {

    String VALUE_NULL = "NULL";

    String ROW_TYPE = "row_type";

    int VALUE_ROW_CATEGORY = 0;

    int VALUE_ROW_MAIN_PARTNER = 1;

    int VALUE_ROW_SIDE_PARTNER = 2;

    int VALUE_ROW_FOOTER = 3;

    String SQL = "CREATE VIEW " + Views.FILTERS + " AS "

            // CATEGORIES
            + "SELECT "
            + "\"" + VALUE_ROW_CATEGORY + "\" AS " + ROW_TYPE + ", "
            + CategoryColumns.ID + ", "
            + CategoryColumns.LABEL + ", "
            + VALUE_NULL + " AS " + PartnerColumns.ID + ", "
            + VALUE_NULL + " AS " + PartnerColumns.NAME + ", "
            + VALUE_NULL + " AS " + PartnerMetadataColumns.IS_VISIBLE
            + " FROM " + Tables.CATEGORY

            + " UNION "

            // PARTNERS
            + "SELECT "
            + "\"" + VALUE_ROW_MAIN_PARTNER + "\"" + " AS " + ROW_TYPE + ", "
            + CategoryColumns.ID + ", "
            + VALUE_NULL + " AS " + CategoryColumns.LABEL + ", "
            + PartnerColumns.ID + ", "
            + PartnerColumns.NAME + ", "
            + PartnerMetadataColumns.IS_VISIBLE
            + " FROM " + Tables.CATEGORY
            + " JOIN " + Tables.PARTNER
            + " ON " + CategoryColumns.ID + " = " + PartnerColumns.MAIN_CATEGORY
            + " JOIN " + Tables.PARTNER_METADATA
            + " ON " + PartnerColumns.ID + " = " + PartnerMetadataColumns.PARTNER_ID

            + " UNION "

            // SIDE PARTNERS
            + "SELECT "
            + "\"" + VALUE_ROW_SIDE_PARTNER + "\"" + " AS " + ROW_TYPE + ", "
            + CategoryColumns.ID + ", "
            + VALUE_NULL + " AS " + CategoryColumns.LABEL + ", "
            + PartnerColumns.ID + ", "
            + PartnerColumns.NAME + ", "
            + PartnerMetadataColumns.IS_VISIBLE
            + " FROM " + Tables.CATEGORY
            + " JOIN " + Tables.PARTNER_SIDE_CATEGORIES
            + " ON " + CategoryColumns.ID + " = " + PartnerSideCategoriesColumns.CATEGORY_ID
            + " JOIN " + Tables.PARTNER
            + " ON " + PartnerSideCategoriesColumns.PARTNER_ID + " = " + PartnerColumns.ID
            + " JOIN " + Tables.PARTNER_METADATA
            + " ON " + PartnerColumns.ID + " = " + PartnerMetadataColumns.PARTNER_ID

            + " UNION "

            // FOOTERS
            + "SELECT "
            + "\"" + VALUE_ROW_FOOTER + "\" AS " + ROW_TYPE + ", "
            + CategoryColumns.ID + ", "
            + VALUE_NULL + " AS " + CategoryColumns.LABEL + ", "
            + VALUE_NULL + " AS " + PartnerColumns.ID + ", "
            + VALUE_NULL + " AS " + PartnerColumns.NAME + ", "
            + VALUE_NULL + " AS " + PartnerMetadataColumns.IS_VISIBLE
            + " FROM " + Tables.CATEGORY

            + " ORDER BY " + CategoryColumns.ID + " ASC, " + ROW_TYPE + " ASC";

}
