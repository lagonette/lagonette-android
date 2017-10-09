package org.lagonette.app.room.sql;

public interface Tables {

    String PARTNER = "partner";

    String PARTNER_METADATA = "partner_metadata";

    String CATEGORY = "category";

    String CATEGORY_METADATA = "category_metadata";

    String PARTNER_SIDE_CATEGORY = "partner_side_category";

    String LOCATION = "location";

    String[] TABLES = new String[]{
            Tables.PARTNER,
            Tables.PARTNER_METADATA,
            Tables.LOCATION,
            Tables.CATEGORY,
            Tables.CATEGORY_METADATA,
            Tables.PARTNER_SIDE_CATEGORY
    };
}
