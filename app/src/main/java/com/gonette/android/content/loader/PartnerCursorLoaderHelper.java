package com.gonette.android.content.loader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PartnerCursorLoaderHelper {

    private static String ARG_SEARCH = "arg:search";

    public static Bundle getArgs(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search);
        return args;
    }

    @NonNull
    public static String getSearch(@Nullable Bundle bundle) {
        return bundle != null ? bundle.getString(ARG_SEARCH, "") : "";
    }
}
