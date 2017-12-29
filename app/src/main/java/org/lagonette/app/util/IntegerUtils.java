package org.lagonette.app.util;

import android.support.annotation.Nullable;

public final class IntegerUtils {

    private IntegerUtils() {
    }

    public static int intValue(@Nullable Integer integer) {
        return integer == null ? 0 : integer;
    }
}
