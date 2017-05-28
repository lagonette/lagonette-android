package org.lagonette.android.database.columns;

import org.lagonette.android.database.Tables;

public interface PartnerColumns {

    String ID = Tables.PARTNER + "_id";

    String NAME = Tables.PARTNER + "_name";

    String DESCRIPTION = Tables.PARTNER + "_description";

    String LATITUDE = Tables.PARTNER + "_latitude";

    String LONGITUDE = Tables.PARTNER + "_longitude";

    String CLIENT_CODE = Tables.PARTNER + "_client_code";

    String ADDRESS = Tables.PARTNER + "_address";

    String CITY = Tables.PARTNER + "_city";

    String LOGO = Tables.PARTNER + "_logo";

    String ZIP_CODE = Tables.PARTNER + "_zip_code";

    String PHONE = Tables.PARTNER + "_phone";

    String WEBSITE = Tables.PARTNER + "_website";

    String EMAIL = Tables.PARTNER + "_email";

    String OPENING_HOURS = Tables.PARTNER + "_opening_hours";

    String IS_EXCHANGE_OFFICE = Tables.PARTNER + "_is_exchange_office";

    String SHORT_DESCRIPTION = Tables.PARTNER + "_short_description";

    String MAIN_CATEGORY = Tables.PARTNER + "_main_category";

    String PARTNER_SIDE_CATEGORIES_ID = Tables.PARTNER + "_partner_side_categories_id";

    // TODO Create table SQL

}
