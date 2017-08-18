package org.lagonette.android.locator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.api.service.LaGonetteService;

public class API {

    @Nullable
    private static LaGonetteService SERVICE;

    private API() {
    }

    @NonNull
    public static LaGonetteService get() {
        if (SERVICE == null) throw new IllegalStateException("SERVICE was not set!");
        return SERVICE;
    }

    public static void set(@NonNull LaGonetteService service) {
        SERVICE = service;
    }

}