package org.lagonette.android.database.columns;

import org.lagonette.android.database.Tables;


public interface CategoryMetadataColumns {

    String CATEGORY_ID = Tables.CATEGORY_METADATA + "_category_id";

    String IS_VISIBLE = Tables.CATEGORY_METADATA + "_is_visible";

    String IS_COLLAPSED = Tables.CATEGORY_METADATA + "_is_collapsed";

    // TODO Create table SQL or put it in statement class
}
