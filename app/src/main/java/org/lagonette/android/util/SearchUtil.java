package org.lagonette.android.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public class SearchUtil {

    public static final String DEFAULT_SEARCH = "";

    public static String formatSearch(@Nullable String search) {
        return TextUtils.isEmpty(search)
                ? "%"
                : "%" + search + "%";
    }
}
