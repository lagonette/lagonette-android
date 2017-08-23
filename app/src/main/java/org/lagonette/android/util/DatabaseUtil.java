package org.lagonette.android.util;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;

public final class DatabaseUtil {

    private DatabaseUtil() {}

    public static final String DATABASE_NAME = "la_gonette.db";

    public static final int DATABASE_NUMBER = 2;

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

}
