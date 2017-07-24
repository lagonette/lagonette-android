package org.lagonette.android.util;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import org.lagonette.android.BuildConfig;

import java.util.Set;

public class DebugContentProviderUtil {

    private static final String TAG = "DebugContentProviderUti";

    private static final String PARAM_DEBUG_FORCE_LOG = "debug_force_log";

    private static final String PARAM_DEBUG_SLOW_DOWN = "debug_slow_down";

    public static void logSqlQuery(
            @NonNull Uri uri,
            @NonNull SQLiteQueryBuilder qb,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String groupBy,
            @Nullable String having,
            @Nullable String sortOrder,
            @Nullable String limit) {
        String sql = qb.buildQuery(projection, selection, groupBy, having, sortOrder, limit);
        if (selectionArgs != null) {
            for (String arg : selectionArgs) {
                sql = sql.replaceFirst("\\?", arg);
            }
        }
        Log.d(TAG, uri.toString());
        Log.d(TAG, sql);
    }

    @NonNull
    public static Uri slowDown(@NonNull Uri uri) {
        return slowDown(uri, 3000);
    }

    @NonNull
    public static Uri slowDown(@NonNull Uri uri, long ms) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(uri.getQueryParameter(PARAM_DEBUG_SLOW_DOWN))) {
                return uri.buildUpon()
                        .appendQueryParameter(PARAM_DEBUG_SLOW_DOWN, String.valueOf(ms))
                        .build();
            }
        }
        return uri;
    }

    @NonNull
    public static Uri forceLog(@NonNull Uri uri) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(uri.getQueryParameter(PARAM_DEBUG_FORCE_LOG))) {
                return uri.buildUpon()
                        .appendQueryParameter(PARAM_DEBUG_FORCE_LOG, "true")
                        .build();
            }
        }
        return uri;
    }

    @NonNull
    public static Uri removeSlowDown(@NonNull Uri uri) {
        if (BuildConfig.DEBUG) {
            Uri builder = removeQueryParameter(uri, PARAM_DEBUG_SLOW_DOWN);
            if (builder != null) {
                return builder;
            }
        }
        return uri;
    }

    @NonNull
    public static Uri removeForceLog(@NonNull Uri uri) {
        if (BuildConfig.DEBUG) {
            Uri builder = removeQueryParameter(uri, PARAM_DEBUG_FORCE_LOG);
            if (builder != null) {
                return builder;
            }
        }
        return uri;
    }

    @Nullable
    private static Uri removeQueryParameter(@NonNull Uri uri, @NonNull String parameter) {
        if (!TextUtils.isEmpty(uri.getQueryParameter(parameter))) {
            Set<String> names = uri.getQueryParameterNames();
            int size = names.size();
            if (size > 0) {
                Uri debugUri = Uri.parse(uri.toString());
                Uri.Builder builder = debugUri.buildUpon();
                builder.clearQuery();
                for (String param : names) {
                    if (parameter.equals(param)) {
                        continue;
                    }
                    builder.appendQueryParameter(param, uri.getQueryParameter(param));
                }
                return builder.build();
            }
        }
        return null;
    }

    @WorkerThread
    public static void executeSlowDown(@NonNull Uri uri) {
        if (BuildConfig.DEBUG) {
            Set<String> params = uri.getQueryParameterNames();
            if (params.contains(PARAM_DEBUG_SLOW_DOWN)) {
                long ms = Long.parseLong(uri.getQueryParameter(PARAM_DEBUG_SLOW_DOWN));
                Log.d(TAG, "executeSlowDown() sleep for " + ms);
                SystemClock.sleep(ms);
            }
        }
    }

}
