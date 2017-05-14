package org.lagonette.android.debug;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import org.lagonette.android.BuildConfig;

public final class CursorDebug {

    private static final String TAG = "CursorDebug";

    private CursorDebug() {
    }

    public static void logCursor(@NonNull Cursor cursor) {
        if (BuildConfig.DEBUG) {
            String columnNames = "";
            for (String columnName : cursor.getColumnNames()) {
                columnNames += columnName + " \t";
            }
            Log.d(TAG, "logCursor: " + columnNames);

            if (cursor.moveToFirst()) {
                do {
                    String values = "";
                    for (String columnName : cursor.getColumnNames()) {
                        values += cursor.getString(cursor.getColumnIndex(columnName)) + " \t";
                    }
                    Log.d(TAG, "logCursor: " + values);
                } while (cursor.moveToNext());
            }
        }
    }
}
