package org.lagonette.android.database.columns;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface FilterColumns {

    String VALUE_NULL = "NULL";

    String ROW_TYPE = "row_type";

    int VALUE_ROW_CATEGORY = 0;

    int VALUE_ROW_MAIN_PARTNER = 1;

    int VALUE_ROW_SIDE_PARTNER = 2;

    int VALUE_ROW_FOOTER = 3;

    int ROW_TYPE_COUNT = 4;

    @Retention(SOURCE)
    @IntDef({
            VALUE_ROW_CATEGORY,
            VALUE_ROW_MAIN_PARTNER,
            VALUE_ROW_SIDE_PARTNER,
            VALUE_ROW_FOOTER
    })
    public @interface RowType {
    }

}
