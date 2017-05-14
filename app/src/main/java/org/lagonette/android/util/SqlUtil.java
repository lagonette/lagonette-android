package org.lagonette.android.util;


import android.support.annotation.NonNull;

import org.lagonette.android.helper.SqlBuilder;

public final class SqlUtil {

    private SqlUtil() {
    }

    private static SqlBuilder SQL_BUILDER;

    public static void provideSqlBuilder(@NonNull SqlBuilder sqlBuilder) {
        SQL_BUILDER = sqlBuilder;
    }

    public static void provideDefaultSqlBuilder() {
        provideSqlBuilder(new SqlBuilder());
    }

    public static SqlBuilder build() {
        if (SQL_BUILDER == null) {
            provideDefaultSqlBuilder();
        }
        return SQL_BUILDER.build();
    }
}
