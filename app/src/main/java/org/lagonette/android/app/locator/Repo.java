package org.lagonette.android.app.locator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.repo.MainRepo;

public final class Repo {

    @Nullable
    private static MainRepo REPO;

    @NonNull
    public static MainRepo get() {
        if (REPO == null) throw new IllegalStateException("Repo was not set!");
        return REPO;
    }

    public static void set(@NonNull MainRepo repo) {
        REPO = repo;
    }

    private Repo() {
    }

}