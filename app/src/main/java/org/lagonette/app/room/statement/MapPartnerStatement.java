package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;
import org.lagonette.app.room.sql.Sql;

public abstract class MapPartnerStatement
        implements GonetteStatement, Sql {

    public static final String SQL =
            "SELECT partner.id, " +
                "partner.main_category_id, " +
                "location.latitude, " +
                "location.longitude, " +
                "location.is_exchange_office, " +
                "main_category.icon " +
            FROM_PARTNER +
            JOIN_LOCATION_AND_METADATA_ON_PARTNER +
            JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER +
            LEFT_JOIN_SIDE_CATEGORY_AND_METADATA_ON_PARTNER +
            "WHERE partner.name LIKE :search " +
                "AND location.display_location <> 0 " +
                "AND location_metadata.is_visible <> 0 " +
            "GROUP BY location.id " +
            "HAVING main_category_metadata.is_visible + TOTAL (side_category_metadata.is_visible) > 0";

}