package org.lagonette.app.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public class SearchUtils {

    public static final String DEFAULT_SEARCH = "";

    public static String formatSearch(@Nullable String search) {
        return TextUtils.isEmpty(search)
                ? "%"
                : "%" + search + "%";
    }
}
