package org.lagonette.app.util;

import android.support.annotation.Nullable;

public final class IntegerUtil {

    private IntegerUtil() {
    }

    public static int intValue(@Nullable Integer integer) {
        return integer == null ? 0 : integer;
    }
}
