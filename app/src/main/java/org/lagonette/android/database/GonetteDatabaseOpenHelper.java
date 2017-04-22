package org.lagonette.android.database;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.stream.JsonReader;

import org.lagonette.android.R;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.parser.FeatureCollectionJsonParser;
import org.lagonette.android.parser.PartnerGeometryJsonParser;
import org.lagonette.android.parser.PartnerJsonParser;
import org.lagonette.android.parser.PartnerPropertiesJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GonetteDatabaseOpenHelper
        extends SQLiteOpenHelper {

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
                + PartnerMetadataColumns.PARTNER_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + PartnerMetadataColumns.IS_VISIBLE + " INTEGER NOT NULL "
                + ")");
    }

    private void dropDatabase(SQLiteDatabase db) {
        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER_METADATA);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER_METADATA);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.PARTNER_SIDE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PARTNER_SIDE_CATEGORIES);

        Log.d(TAG, "dropDatabase: Drop table " + Tables.CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.CATEGORY);
    }

    public static void parseData(Context context) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentValues contentValues = new ContentValues();

        PartnerPropertiesJsonParser propertiesJsonParser = new PartnerPropertiesJsonParser(
                contentValues
        );
        PartnerGeometryJsonParser geometryJsonParser = new PartnerGeometryJsonParser(contentValues);
        PartnerJsonParser partnerJsonParser = new PartnerJsonParser(
                operations,
                contentValues,
                propertiesJsonParser,
                geometryJsonParser
        );
        FeatureCollectionJsonParser partnersJsonParser = new FeatureCollectionJsonParser(
                partnerJsonParser
        );

        try {
            operations.add(
                    ContentProviderOperation.newDelete(GonetteContract.Partner.CONTENT_URI)
                            .withYieldAllowed(true)
                            .build()
            );
            operations.add(
                    ContentProviderOperation.newDelete(GonetteContract.PartnerMetadata.CONTENT_URI)
                            .withYieldAllowed(true)
                            .build()
            );

            InputStream inputStream = context.getResources().openRawResource(R.raw.gonette);
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
            partnersJsonParser.parse(jsonReader);

            context.getContentResolver().applyBatch(
                    GonetteContract.AUTHORITY,
                    operations
            );
        } catch (IOException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "parseData: " + e.getMessage(), e);
            FirebaseCrash.report(e);
        }
    }
}
