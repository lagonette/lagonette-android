package org.lagonette.app.room.statement;

import org.lagonette.app.room.statement.base.GonetteStatement;
import org.lagonette.app.room.sql.Sql;

public abstract class MapPartnerStatement
        implements GonetteStatement, Sql {

    public static final String SQL =
            "SELECT partner.id, " +
                "partner.latitude, " +
                "partner.longitude, " +
                "partner.is_exchange_office, " +
                "partner.main_category_id, " +
                "main_category.icon " +
            FROM_PARTNER_AND_METADATA +
            JOIN_LOCATION_ON_PARTNER +
            JOIN_MAIN_CATEGORY_AND_METADATA_ON_PARTNER +
            LEFT_JOIN_SIDE_CATEGORY_AND_METADATA_ON_PARTNER +
            "WHERE partner.name LIKE :search " +
                "AND location.display_location <> 0 " +
            "GROUP BY location.id " +
            "HAVING partner_metadata.is_visible + TOTAL (side_category_metadata.is_visible) > 0";

}