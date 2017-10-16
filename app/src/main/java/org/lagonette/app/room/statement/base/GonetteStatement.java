package org.lagonette.app.room.statement.base;

public interface GonetteStatement {

    String FROM_PARTNER = " FROM partner ";

    String FROM_CATEGORY = " FROM category ";

    String JOIN_METADATA_ON_CATEGORY =
            " JOIN category_metadata " +
                    " ON category.id = category_metadata.category_id " +
                    " AND category.type = category_metadata.category_type ";

    String LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY =
            " LEFT JOIN partner AS main_partner " +
                    " ON category.id = main_partner.main_category_id " +
                    " AND category.type = main_partner.main_category_type ";

    String JOIN_MAIN_PARTNER_ON_CATEGORY =
            " JOIN partner AS main_partner " +
                    " ON category.id = main_partner.main_category_id " +
                    " AND category.type = main_partner.main_category_type ";

    String JOIN_PARTNER_ON_CATEGORY =
            " JOIN partner " +
                    " ON category.id = partner.main_category_id " +
                    " AND category.type = partner.category_type ";

    String LEFT_JOIN_SIDE_PARTNER_ON_CATEGORY =
            " LEFT JOIN partner_side_category " +
                    " ON category.id = partner_side_category.category_id " +
                    " AND category.type = partner_side_category.category_type " +
                    " LEFT JOIN partner AS side_partner " +
                    " ON partner_side_category.partner_id = side_partner.id ";

    String JOIN_SIDE_PARTNER_ON_CATEGORY =
            " JOIN partner_side_category " +
                    " ON category.id = partner_side_category.category_id " +
                    " AND category.type = partner_side_category.category_type " +
                    " JOIN partner AS side_partner " +
                    " ON partner_side_category.partner_id = side_partner.id ";

    String JOIN_METADATA_ON_LOCATION =
            " JOIN location_metadata " +
                    " ON location.id = location_metadata.location_id ";

    String LEFT_JOIN_METADATA_ON_LOCATION =
            " LEFT JOIN location_metadata " +
                    " ON location.id = location_metadata.location_id ";

    String LEFT_JOIN_METADATA_ON_MAIN_LOCATION =
            " LEFT JOIN location_metadata AS main_location_metadata" +
                    " ON main_location.id = main_location_metadata.location_id ";

    String LEFT_JOIN_METADATA_ON_SIDE_LOCATION =
            " LEFT JOIN location_metadata AS side_location_metadata" +
                    " ON side_location.id = side_location_metadata.location_id ";

    String JOIN_METADATA_ON_SIDE_LOCATION =
            " JOIN location_metadata AS side_location_metadata" +
                    " ON side_location.id = side_location_metadata.location_id ";

    String JOIN_LOCATION_ON_PARTNER =
            " JOIN location" +
                    " ON location.partner_id = partner.id";

    String LEFT_JOIN_MAIN_LOCATION_ON_MAIN_PARTNER =
            " LEFT JOIN location AS main_location" +
                    " ON main_location.partner_id = main_partner.id";

    String LEFT_JOIN_SIDE_LOCATION_ON_SIDE_PARTNER =
            " LEFT JOIN location AS side_location" +
                    " ON side_location.partner_id = side_partner.id";

    String JOIN_SIDE_LOCATION_ON_SIDE_PARTNER =
            " JOIN location AS side_location" +
                    " ON side_location.partner_id = side_partner.id";

    String JOIN_MAIN_CATEGORY_ON_PARTNER =
            " JOIN category AS main_category " +
                    " ON partner.main_category_id = main_category.id " +
                    " AND partner.main_category_type = main_category.type ";

    String JOIN_METADATA_ON_MAIN_CATEGORY =
            " JOIN category_metadata AS main_category_metadata " +
                    " ON main_category.id = main_category_metadata.category_id " +
                    " AND main_category.type = main_category_metadata.category_type ";

    String LEFT_JOIN_SIDE_CATEGORY_ON_PARTNER =
            " LEFT JOIN partner_side_category " +
                    " ON partner.id = partner_side_category.partner_id " +
                    " LEFT JOIN category AS side_category " +
                    " ON partner_side_category.category_id = side_category.id " +
                    " AND partner_side_category.category_type = side_category.type ";

    String LEFT_JOIN_METADATA_ON_SIDE_CATEGORY =
            " LEFT JOIN category_metadata AS side_category_metadata " +
                    " ON side_category.id = side_category_metadata.category_id " +
                    " AND side_category.type = side_category_metadata.category_type ";

    String FROM_CATEGORY_AND_METADATA = FROM_CATEGORY + JOIN_METADATA_ON_CATEGORY;

    String JOIN_LOCATION_AND_METADATA_ON_PARTNER =
            JOIN_LOCATION_ON_PARTNER
                    + JOIN_METADATA_ON_LOCATION;

    String JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER =
            JOIN_MAIN_CATEGORY_ON_PARTNER +
                    JOIN_METADATA_ON_MAIN_CATEGORY;

    String LEFT_JOIN_SIDE_CATEGORY_AND_METADATA_ON_PARTNER =
            LEFT_JOIN_SIDE_CATEGORY_ON_PARTNER +
                    LEFT_JOIN_METADATA_ON_SIDE_CATEGORY;

    String LEFT_JOIN_MAIN_LOCATION_AND_METADATA_ON_MAIN_PARTNER =
            LEFT_JOIN_MAIN_LOCATION_ON_MAIN_PARTNER +
                    LEFT_JOIN_METADATA_ON_MAIN_LOCATION;

    String LEFT_JOIN_SIDE_LOCATION_AND_METADATA_ON_SIDE_PARTNER =
            LEFT_JOIN_SIDE_LOCATION_ON_SIDE_PARTNER +
                    LEFT_JOIN_METADATA_ON_SIDE_LOCATION;

    String JOIN_SIDE_LOCATION_AND_METADATA_ON_SIDE_PARTNER =
            JOIN_SIDE_LOCATION_ON_SIDE_PARTNER +
                    JOIN_METADATA_ON_SIDE_LOCATION;
}
