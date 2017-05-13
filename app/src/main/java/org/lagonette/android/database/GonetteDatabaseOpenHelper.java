package org.lagonette.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GonetteDatabaseOpenHelper
        extends SQLiteOpenHelper {

    private static final String TAG = "GonetteDatabaseOpenHelp";

    private static final int DATABASE_VERSION = 3;

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
        Log.d(TAG, "onCreate: Create table " + Tables.CATEGORY);
        db.execSQL("CREATE TABLE " + Tables.CATEGORY + " ("
                + CategoryColumns.ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + CategoryColumns.LABEL + " TEXT NOT NULL, "
                + CategoryColumns.ICON + " TEXT NOT NULL "
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.PARTNER_SIDE_CATEGORIES);
        db.execSQL("CREATE TABLE " + Tables.PARTNER_SIDE_CATEGORIES + " ("
                + PartnerSideCategoriesColumns.CATEGORY_ID + " INTEGER, "
                + PartnerSideCategoriesColumns.PARTNER_ID + " INTEGER, "
                + "PRIMARY KEY (" + PartnerSideCategoriesColumns.CATEGORY_ID + ", " + PartnerSideCategoriesColumns.PARTNER_ID + ") ON CONFLICT REPLACE "
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.PARTNER);
        db.execSQL("CREATE TABLE " + Tables.PARTNER + " ("
                + PartnerColumns.ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + PartnerColumns.CLIENT_CODE + " TEXT NOT NULL, "
                + PartnerColumns.NAME + " TEXT, "
                + PartnerColumns.LATITUDE + " NUMERIC NOT NULL, "
                + PartnerColumns.LONGITUDE + " NUMERIC NOT NULL, "
                + PartnerColumns.DESCRIPTION + " TEXT, "
                + PartnerColumns.ADDRESS + " TEXT, "
                + PartnerColumns.CITY + " TEXT, "
                + PartnerColumns.LOGO + " TEXT, "
                + PartnerColumns.ZIP_CODE + " TEXT, "
                + PartnerColumns.PHONE + " TEXT, "
                + PartnerColumns.WEBSITE + " TEXT, "
                + PartnerColumns.EMAIL + " TEXT, "
                + PartnerColumns.OPENING_HOURS + " TEXT, "
                + PartnerColumns.IS_EXCHANGE_OFFICE + " INTEGER, "
                + PartnerColumns.SHORT_DESCRIPTION + " TEXT, "
                + PartnerColumns.MAIN_CATEGORY + " INTEGER, "
                + PartnerColumns.PARTNER_SIDE_CATEGORIES_ID + " INTEGER "
                + ")");

        Log.d(TAG, "onCreate: Create table " + Tables.PARTNER_METADATA);
        db.execSQL("CREATE TABLE " + Tables.PARTNER_METADATA + " ("
                + PartnerMetadataColumns.PARTNER_ID + " INTEGER PRIMARY KEY ON CONFLICT IGNORE, "
                + PartnerMetadataColumns.IS_VISIBLE + " INTEGER NOT NULL "
                + ")");

        Log.d(TAG, "onCreate: Create view " + Views.FILTERS);
        db.execSQL(FilterColumns.SQL);
    }

    private void dropDatabase(SQLiteDatabase db) {
        Log.d(TAG, "dropDatabase: Drop view " + Views.FILTERS);
        db.execSQL("DROP VIEW IF EXISTS " + Views.FILTERS);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER_METADATA);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER_METADATA);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER_SIDE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER_SIDE_CATEGORIES);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.CATEGORY);
    }

}
