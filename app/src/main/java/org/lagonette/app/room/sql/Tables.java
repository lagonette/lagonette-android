package org.lagonette.app.room.sql;

public class Tables {

    public static final String PARTNER = "partner";

    public static final String PARTNER_METADATA = "partner_metadata";

    public static final String CATEGORY = "category";

    public static final String CATEGORY_METADATA = "category_metadata";

    public static final String PARTNER_SIDE_CATEGORY = "partner_side_category";

    public static final String[] TABLES = new String[]{
            Tables.PARTNER,
            Tables.PARTNER_METADATA,
            Tables.CATEGORY,
            Tables.CATEGORY_METADATA,
            Tables.PARTNER_SIDE_CATEGORY
    };
}
