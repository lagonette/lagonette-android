package org.lagonette.app.room.statement;

public abstract class MapPartnerStatement
        extends Statement {

    // TODO Why only 87 partners returned ??
    public static final String SQL =
            "SELECT partner.id, " +
                "partner.latitude, " +
                "partner.longitude, " +
                "partner.is_exchange_office, " +
                "partner.main_category_id, " +
                "main_category.icon " +
            "FROM partner " +
            "JOIN partner_metadata " +
                "ON partner.id = partner_metadata.partner_id " +
            "JOIN category AS main_category " +
                "ON partner.main_category_id = main_category.id " +
            "JOIN category_metadata AS main_category_metadata " +
                "ON main_category.id = main_category_metadata.category_id " +
            "LEFT JOIN partner_side_category " +
                "ON partner.id = partner_side_category.partner_id " +
            "LEFT JOIN category AS side_category " +
                "ON partner_side_category.category_id = side_category.id " +
            "LEFT JOIN category_metadata AS side_category_metadata " +
                "ON side_category.id = side_category_metadata.category_id " +
            "WHERE partner.name LIKE :search " +
                "AND partner.is_localizable <> 0 " +
            "GROUP BY partner.id " +
            "HAVING partner_metadata.is_visible > 0 " +
                "AND SUM (side_category_metadata.is_visible) > 0";

}
