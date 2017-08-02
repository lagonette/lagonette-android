package org.lagonette.android.room.statement;

public abstract class MapPartnerStatement {

    public static final String sql =
            "SELECT partner.id, " +
                "partner.name, " +
                "partner.description, " +
                "partner.latitude, " +
                "partner.longitude, " +
                "partner.is_exchange_office, " +
                "partner.main_category, " +
                "main_category.icon, " +
                "SUM (side_category_metadata.is_visible) AS side_is_visible_sum " +
            "FROM partner " +
            "JOIN partner_metadata " +
                "ON partner.id = partner_metadata.partner_id " +
            "JOIN category AS main_category " +
                "ON partner.main_category = main_category.id " +
            "JOIN category_metadata AS main_category_metadata " +
                "ON main_category.id = main_category_metadata.category_id " +
            "LEFT JOIN partner_side_category " +
                "ON partner.id = partner_side_category.partner_id LEFT " +
            "JOIN category AS side_category " +
                "ON partner_side_category.category_id = side_category.id " +
            "LEFT JOIN category_metadata AS side_category_metadata " +
                "ON side_category.id = side_category_metadata.category_id " +
            "GROUP BY partner.id";

    public static final int ID;

    public static final int NAME;

    public static final int DESCRIPTION;

    public static final int LATITUDE;

    public static final int LONGITUDE;

    public static final int IS_EXCHANGE_OFFICE;

    public static final int MAIN_CATEGORY;

    public static final int ICON;

    public static final int SIDE_IS_VISIBLE_SUM;

    static {
        int i = 0;
        ID = i++;
        NAME = i++;
        DESCRIPTION = i++;
        LATITUDE = i++;
        LONGITUDE = i++;
        IS_EXCHANGE_OFFICE = i++;
        MAIN_CATEGORY = i++;
        ICON = i++;
        SIDE_IS_VISIBLE_SUM = i++;
    }

    public interface Contract {

        long getId();

        String getName();

        String getDescription();

        double getLatitude();

        double getLongitude();

        Boolean isExchangeOffice();

        long getMainCategory();

        String getIcon();

        // TODO ???
        int getSideIsVisibleSum();
    }

}
