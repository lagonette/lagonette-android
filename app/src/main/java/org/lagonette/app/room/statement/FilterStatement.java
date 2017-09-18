package org.lagonette.app.room.statement;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public abstract class FilterStatement
        extends Statement {

    public static final String sql =
            "SELECT 0 AS row_type, " +
                    "category.id, " +
                    "category.label, " +
                    "category.icon, " +
                    "category_metadata.is_visible, " +
                    "category_metadata.is_collapsed, " +
                    "SUM (main_partner_metadata.is_visible) + SUM (side_partner_metadata.is_visible) AS category_is_partners_visible, " +
                    "NULL AS id, " +
                    "NULL AS name, " +
                    "NULL AS street, " +
                    "NULL AS zip_code, " +
                    "NULL AS city, " +
                    "NULL AS is_exchange_office, " +
                    "NULL AS is_visible " +
                    "FROM category " +
                    "JOIN category_metadata " +
                    "ON category.id = category_metadata.category_id " +
                    "LEFT JOIN partner AS main_partner " +
                    "ON category.id = main_partner.main_category_id " +
                    "LEFT JOIN partner_metadata AS main_partner_metadata " +
                    "ON main_partner.id = main_partner_metadata.partner_id " +
                    "LEFT JOIN partner_side_category " +
                    "ON category.id = partner_side_category.category_id " +
                    "LEFT JOIN partner AS side_partner " +
                    "ON partner_side_category.partner_id = side_partner.id " +
                    "LEFT JOIN partner_metadata AS side_partner_metadata " +
                    "ON side_partner.id = side_partner_metadata.partner_id " +
                    "WHERE main_partner.name LIKE :search " +
                    "OR side_partner.name LIKE :search " +
                    "GROUP BY category.id " +

                    "UNION " +

                    "SELECT " +
                    "3 AS row_type, " +
                    "category.id, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "NULL AS is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "NULL AS id, " +
                    "NULL AS name, " +
                    "NULL AS street, " +
                    "NULL AS zip_code, " +
                    "NULL AS city, " +
                    "NULL AS is_exchange_office, " +
                    "NULL AS is_visible " +
                    "FROM category " +
                    "LEFT JOIN partner AS main_partner " +
                    "ON category.id = main_partner.main_category_id " +
                    "LEFT JOIN partner_side_category " +
                    "ON category.id = partner_side_category.category_id " +
                    "LEFT JOIN partner AS side_partner " +
                    "ON partner_side_category.partner_id = side_partner.id " +
                    "WHERE main_partner.main_category_id IS NOT NULL " +
                    "AND main_partner.name LIKE :search " +
                    "OR category.id IS NOT NULL " +
                    "AND side_partner.name LIKE :search " +
                    "GROUP BY category.id " +

                    "UNION " +

                    "SELECT 1 AS row_type, " +
                    "category.id, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "category_metadata.is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "partner.id, " +
                    "partner.name, " +
                    "partner.street, " +
                    "partner.zip_code, " +
                    "partner.city, " +
                    "partner.is_exchange_office, " +
                    "partner_metadata.is_visible " +
                    "FROM category " +
                    "JOIN category_metadata " +
                    "ON category.id = category_metadata.category_id " +
                    "JOIN partner " +
                    "ON category.id = partner.main_category_id " +
                    "JOIN partner_metadata " +
                    "ON partner.id = partner_metadata.partner_id " +
                    "WHERE partner.name LIKE :search AND category_metadata.is_collapsed = 0 " +

                    "UNION " +

                    "SELECT 2 AS row_type, " +
                    "category.id, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "category_metadata.is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "partner.id, " +
                    "partner.name, " +
                    "partner.street, " +
                    "partner.zip_code, " +
                    "partner.city, " +
                    "partner.is_exchange_office, " +
                    "partner_metadata.is_visible " +
                    "FROM category " +
                    "JOIN category_metadata " +
                    "ON category.id = category_metadata.category_id " +
                    "JOIN partner_side_category " +
                    "ON category.id = partner_side_category.category_id " +
                    "JOIN partner " +
                    "ON partner.id = partner_side_category.partner_id " +
                    "JOIN partner_metadata " +
                    "ON partner.id = partner_metadata.partner_id " +
                    "WHERE partner.name LIKE :search " +
                    "AND category_metadata.is_collapsed = 0 " +

                    "ORDER BY category.id ASC, row_type ASC";


    public static final int ROW_TYPE;

    public static final int CATEGORY_ID;

    public static final int CATEGORY_LABEL;

    public static final int CATEGORY_ICON;

    public static final int CATEGORY_METADATA_IS_VISIBLE;

    public static final int CATEGORY_METADATA_IS_COLLAPSED;

    public static final int CATEGORY_IS_PARTNERS_VISIBLE;

    public static final int PARTNER_ID;

    public static final int PARTNER_NAME;

    public static final int PARTNER_STREET;

    public static final int PARTNER_ZIP_CODE;

    public static final int PARTNER_CITY;

    public static final int PARTNER_IS_EXCHANGE_OFFICE;

    public static final int PARTNER_METADATA_IS_VISIBLE;

    public static final int VALUE_ROW_CATEGORY = 0;

    public static final int VALUE_ROW_MAIN_PARTNER = 1;

    public static final int VALUE_ROW_SIDE_PARTNER = 2;

    public static final int VALUE_ROW_FOOTER = 3;

    public static final int ROW_TYPE_COUNT = 4;

    @Retention(SOURCE)
    @IntDef({
            VALUE_ROW_CATEGORY,
            VALUE_ROW_MAIN_PARTNER,
            VALUE_ROW_SIDE_PARTNER,
            VALUE_ROW_FOOTER
    })
    public @interface RowType {
    }

    static {
        int i = 0;
        ROW_TYPE = i++;
        CATEGORY_ID = i++;
        CATEGORY_LABEL = i++;
        CATEGORY_ICON = i++;
        CATEGORY_METADATA_IS_VISIBLE = i++;
        CATEGORY_METADATA_IS_COLLAPSED = i++;
        CATEGORY_IS_PARTNERS_VISIBLE = i++;
        PARTNER_ID = i++;
        PARTNER_NAME = i++;
        PARTNER_STREET = i++;
        PARTNER_ZIP_CODE = i++;
        PARTNER_CITY = i++;
        PARTNER_IS_EXCHANGE_OFFICE = i++;
        PARTNER_METADATA_IS_VISIBLE = i++;
    }

    public interface Contract {

        int getRowType();

        long getCategoryId();

        String getCategoryLabel();

        String getCategoryIcon();

        boolean isCategoryVisible();

        boolean isCategoryCollapsed();

        boolean isCategoryPartnersVisible();

        long getPartnerId();

        String getPartnerName();

        String getPartnerAddress();

        boolean isPartnerExchangeOffice();

        boolean isPartnerVisible();

    }

}
