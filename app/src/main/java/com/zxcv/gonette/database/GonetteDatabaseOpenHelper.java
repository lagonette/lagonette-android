package com.zxcv.gonette.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GonetteDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "GonetteDatabaseOpenHelp";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "gonette.db";

    public GonetteDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            dropDatabase(db);
            createDatabase(db);
        }
    }

    private void createDatabase(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Create table " + Tables.PARTNER);
        db.execSQL("CREATE TABLE " + Tables.PARTNER + " ("
                + PartnerColumns.ID + " INTEGER PRIMARY KEY, "
                + PartnerColumns.NAME + " TEXT NOT NULL, "
                + PartnerColumns.DESCRIPTION + " TEXT NOT NULL, "
                + PartnerColumns.LATITUDE + " INTEGER NOT NULL, "
                + PartnerColumns.LONGITUDE + " INTEGER NOT NULL "
                + ")");
    }

    private void dropDatabase(SQLiteDatabase db) {
        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER);
    }
}
