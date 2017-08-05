package org.lagonette.android.util;

import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.android.app.LaGonetteApplication;
import org.lagonette.android.room.database.LaGonetteDatabase;

public abstract class DB {

    public static LaGonetteDatabase get(@NonNull Context context) {
        return ((LaGonetteApplication) context.getApplicationContext()).database;
    }
}
