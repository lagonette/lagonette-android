package org.lagonette.app.room.statement.base;

public interface GonetteStatement {

    String FROM_PARTNER = "FROM partner ";

    String FROM_CATEGORY = "FROM category ";

    String JOIN_METADATA_ON_CATEGORY =
            "JOIN category_metadata " +
                    "ON category.id = category_metadata.category_id ";

    String LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY =
            "LEFT JOIN partner AS main_partner " +
                    "ON category.id = main_partner.main_category_id ";

    String JOIN_PARTNER_ON_CATEGORY =
            "JOIN partner " +
                    "ON category.id = partner.main_category_id ";

    String LEFT_JOIN_METADATA_ON_MAIN_PARTNER =
            "LEFT JOIN partner_metadata AS main_partner_metadata " +
                    "ON main_partner.id = main_partner_metadata.partner_id ";

    String LEFT_JOIN_METADATA_ON_SIDE_PARTNER =
            "LEFT JOIN partner_metadata AS side_partner_metadata " +
                    "ON side_partner.id = side_partner_metadata.partner_id ";

    String LEFT_JOIN_SIDE_PARTNER_ON_CATEGORY =
            "LEFT JOIN partner_side_category " +
                    "ON category.id = partner_side_category.category_id " +
                    "LEFT JOIN partner AS side_partner " +
                    "ON partner_side_category.partner_id = side_partner.id ";

    String JOIN_METADATA_ON_PARTNER =
            "JOIN partner_metadata " +
                    "ON partner.id = partner_metadata.partner_id ";

    String JOIN_LOCATION_ON_PARTNER =
            "JOIN location" +
                    "ON location.partner_id = partner.id";

    String JOIN_MAIN_CATEGORY_ON_PARTNER =
            "JOIN category AS main_category " +
                    "ON partner.main_category_id = main_category.id ";

    String JOIN_METADATA_ON_MAIN_CATEGORY =
            "JOIN category_metadata AS main_category_metadata " +
                    "ON main_category.id = main_category_metadata.category_id ";

    String LEFT_JOIN_SIDE_CATEGORY_ON_PARTNER =
            "LEFT JOIN partner_side_category " +
                    "ON partner.id = partner_side_category.partner_id " +
                    "LEFT JOIN category AS side_category " +
                    "ON partner_side_category.category_id = side_category.id ";

    String LEFT_JOIN_METADATA_ON_SIDE_CATEGORY =
            "LEFT JOIN category_metadata AS side_category_metadata " +
                    "ON side_category.id = side_category_metadata.category_id ";

    String FROM_CATEGORY_AND_METADATA = FROM_CATEGORY + JOIN_METADATA_ON_CATEGORY;

    String FROM_PARTNER_AND_METADATA = FROM_PARTNER + JOIN_METADATA_ON_PARTNER;

    String JOIN_PARTNER_AND_METADATA_ON_CATEGORY =
            JOIN_PARTNER_ON_CATEGORY +
                    JOIN_METADATA_ON_PARTNER;

    String LEFT_JOIN_MAIN_PARTNER_AND_METADATA_ON_CATEGORY =
            LEFT_JOIN_MAIN_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_METADATA_ON_MAIN_PARTNER;

    String JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER =
            JOIN_MAIN_CATEGORY_ON_PARTNER +
                    JOIN_METADATA_ON_MAIN_CATEGORY;

    String LEFT_JOIN_SIDE_CATEGORY_AND_METADATA_ON_PARTNER =
            LEFT_JOIN_SIDE_CATEGORY_ON_PARTNER +
                    LEFT_JOIN_METADATA_ON_SIDE_CATEGORY;

    String LEFT_JOIN_SIDE_PARTNER_AND_METADATA_ON_CATEGORY =
            LEFT_JOIN_SIDE_PARTNER_ON_CATEGORY +
                    LEFT_JOIN_METADATA_ON_SIDE_PARTNER;
}
