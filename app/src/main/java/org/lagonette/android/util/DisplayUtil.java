package org.lagonette.android.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.lagonette.android.R;

public final class DisplayUtil {

    private DisplayUtil() {
    }

    public static final String formatAddress(@NonNull Resources resources, @NonNull String address, @NonNull String zipCode, @NonNull String city) {
        return resources.getString(R.string.format_address_full, address, zipCode, city);
    }
}
