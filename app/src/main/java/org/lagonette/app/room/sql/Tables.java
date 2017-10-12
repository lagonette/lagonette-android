package org.lagonette.app.room.sql;

public interface Tables {

    String PARTNER = "partner";

    String CATEGORY = "category";

    String CATEGORY_METADATA = "category_metadata";

    String PARTNER_SIDE_CATEGORY = "partner_side_category";

    String LOCATION = "location";

    String LOCATION_METADATA = "location_metadata";

    String[] TABLES = new String[]{
            Tables.PARTNER,
            Tables.LOCATION_METADATA,
            Tables.LOCATION,
            Tables.CATEGORY,
            Tables.CATEGORY_METADATA,
            Tables.PARTNER_SIDE_CATEGORY
    };
}
