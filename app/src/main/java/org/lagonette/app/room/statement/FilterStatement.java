package org.lagonette.app.room.statement;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.statement.base.GonetteStatement;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public abstract class FilterStatement
        implements GonetteStatement {

    public static final int VALUE_ROW_CATEGORY = 1;

    public static final int VALUE_ROW_MAIN_PARTNER = 2;

    public static final int VALUE_ROW_SIDE_PARTNER = 3;

    public static final int VALUE_ROW_FOOTER = 4;

    public static final int ROW_TYPE_COUNT = 5;

    @Retention(SOURCE)
    @IntDef({
            VALUE_ROW_CATEGORY,
            VALUE_ROW_MAIN_PARTNER,
            VALUE_ROW_SIDE_PARTNER,
            VALUE_ROW_FOOTER
    })
    public @interface RowType {
    }

    private static final String SQL_CATEGORIES =
            "SELECT " +
                    VALUE_ROW_CATEGORY + " AS row_type, " +
                    "category.id, " +
                    "category.type, " +
                    "category.label, " +
                    "category.icon, " +
                    "category.display_order, " +
                    "category_metadata.is_visible, " +
                    "category_metadata.is_collapsed, " +
                    "TOTAL(main_location_metadata.is_visible) + TOTAL(side_location_metadata.is_visible) AS category_is_partners_visible, " +
                    "NULL AS id, " +
                    "NULL AS street, " +
                    "NULL AS zip_code, " +
                    "NULL AS city, " +
                    "NULL AS is_exchange_office, " +
                    "NULL AS is_visible, " +
                    "NULL AS name " +
                    FROM_CATEGORY_AND_METADATA +
                    LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_MAIN_LOCATION_AND_METADATA_ON_MAIN_PARTNER +
                    LEFT_JOIN_SIDE_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_SIDE_LOCATION_AND_METADATA_ON_SIDE_PARTNER +
                    " WHERE category.hidden = 0 " +
                    " AND (main_partner.name LIKE :search " +
                    " OR side_partner.name LIKE :search) " +
                    " GROUP BY category.type, category.id ";

    private static final String SQL_FOOTERS =
            "SELECT " +
                    VALUE_ROW_FOOTER + " AS row_type, " +
                    "category.id, " +
                    "category.type, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "category.display_order, " +
                    "NULL AS is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "NULL AS id, " +
                    "NULL AS street, " +
                    "NULL AS zip_code, " +
                    "NULL AS city, " +
                    "NULL AS is_exchange_office, " +
                    "NULL AS is_visible, " +
                    "NULL AS name " +
                    FROM_CATEGORY +
                    LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_MAIN_LOCATION_ON_MAIN_PARTNER +
                    LEFT_JOIN_SIDE_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_SIDE_LOCATION_ON_SIDE_PARTNER +
                    " WHERE main_partner.main_category_id IS NOT NULL " +
                    " AND main_partner.name LIKE :search " +
                    " OR category.id IS NOT NULL " +
                    " AND side_partner.name LIKE :search " +
                    " GROUP BY category.type, category.id ";


    private static final String SQL_MAIN_PARTNERS =
            "SELECT " +
                    VALUE_ROW_MAIN_PARTNER + " AS row_type, " +
                    "category.id, " +
                    "category.type, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "category.display_order, " +
                    "category_metadata.is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "main_location.id, " +
                    "main_location.street, " +
                    "main_location.zip_code, " +
                    "main_location.city, " +
                    "main_location.is_exchange_office, " +
                    "main_location_metadata.is_visible, " +
                    "main_partner.name " +
                    FROM_CATEGORY_AND_METADATA +
                    JOIN_MAIN_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_MAIN_LOCATION_AND_METADATA_ON_MAIN_PARTNER +
                    " WHERE main_partner.name LIKE :search AND category_metadata.is_collapsed = 0 ";

    private static final String SQL_SIDE_PARTNERS =
            "SELECT " +
                    VALUE_ROW_SIDE_PARTNER + " AS row_type, " +
                    "category.id, " +
                    "category.type, " +
                    "NULL AS label, " +
                    "NULL AS icon, " +
                    "category.display_order, " +
                    "category_metadata.is_visible, " +
                    "NULL AS is_collapsed, " +
                    "NULL AS category_is_partners_visible, " +
                    "side_location.id, " +
                    "side_location.street, " +
                    "side_location.zip_code, " +
                    "side_location.city, " +
                    "side_location.is_exchange_office, " +
                    "side_location_metadata.is_visible, " +
                    "side_partner.name " +
                    FROM_CATEGORY_AND_METADATA +
                    JOIN_SIDE_PARTNER_ON_CATEGORY +
                    JOIN_SIDE_LOCATION_AND_METADATA_ON_SIDE_PARTNER +
                    " WHERE side_partner.name LIKE :search " +
                    " AND category_metadata.is_collapsed = 0 ";

    public static final String SQL =
            SQL_CATEGORIES +
                    " UNION " +
                    SQL_FOOTERS +
                    " UNION " +
                    SQL_MAIN_PARTNERS +
                    " UNION " +
                    SQL_SIDE_PARTNERS +
                    " ORDER BY category.display_order ASC, category.id ASC, row_type ASC";


    public static final int ROW_TYPE;

    public static final int CATEGORY_ID;

    public static final int CATEGORY_TYPE;

    public static final int CATEGORY_LABEL;

    public static final int CATEGORY_ICON;

    public static final int CATEGORY_DISPLAY_ORDER;

    public static final int CATEGORY_METADATA_IS_VISIBLE;

    public static final int CATEGORY_METADATA_IS_COLLAPSED;

    public static final int CATEGORY_IS_PARTNERS_VISIBLE;

    public static final int LOCATION_ID;

    public static final int PARTNER_NAME;

    public static final int LOCATION_STREET;

    public static final int LOCATION_ZIP_CODE;

    public static final int LOCATION_CITY;

    public static final int LOCATION_IS_EXCHANGE_OFFICE;

    public static final int LOCATION_METADATA_IS_VISIBLE;

    static {
        int i = 0;
        ROW_TYPE = i++;
        CATEGORY_ID = i++;
        CATEGORY_TYPE = i++;
        CATEGORY_LABEL = i++;
        CATEGORY_ICON = i++;
        CATEGORY_DISPLAY_ORDER = i++;
        CATEGORY_METADATA_IS_VISIBLE = i++;
        CATEGORY_METADATA_IS_COLLAPSED = i++;
        CATEGORY_IS_PARTNERS_VISIBLE = i++;
        LOCATION_ID = i++;
        LOCATION_STREET = i++;
        LOCATION_ZIP_CODE = i++;
        LOCATION_CITY = i++;
        LOCATION_IS_EXCHANGE_OFFICE = i++;
        LOCATION_METADATA_IS_VISIBLE = i++;
        PARTNER_NAME = i++;
    }

    public interface Contract {

        int getRowType();

        @NonNull
        CategoryKey getCategoryKey();

        @NonNull
        CategoryKey getCategoryKey(@NonNull CategoryKey recycle);

        @NonNull
        String getCategoryLabel();

        @NonNull
        String getCategoryIcon();

        boolean isCategoryVisible();

        boolean isCategoryCollapsed();

        boolean isCategoryPartnersVisible();

        long getLocationId();

        @NonNull
        String getLocationAddress();

        boolean isLocationExchangeOffice();

        boolean isLocationVisible();

        @NonNull
        String getPartnerName();

    }

}