package com.zxcv.gonette.database;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.zxcv.gonette.R;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.parser.FeatureCollectionJsonParser;
import com.zxcv.gonette.parser.PartnerGeometryJsonParser;
import com.zxcv.gonette.parser.PartnerJsonParser;
import com.zxcv.gonette.parser.PartnerPropertiesJsonParser;

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

    public static void parseData(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.gonette);
        JsonReader  jsonReader  = new JsonReader(new InputStreamReader(inputStream));

        ArrayList<ContentProviderOperation> operations           = new ArrayList<>();
        ContentValues                       partnerContentValues = new ContentValues();

        PartnerPropertiesJsonParser propertiesJsonParser = new PartnerPropertiesJsonParser(
                partnerContentValues);
        PartnerGeometryJsonParser geometryJsonParser = new PartnerGeometryJsonParser(
                partnerContentValues);
        PartnerJsonParser partnerJsonParser = new PartnerJsonParser(
                operations,
                partnerContentValues,
                propertiesJsonParser,
                geometryJsonParser
        );
        FeatureCollectionJsonParser featureCollectionJsonParser = new FeatureCollectionJsonParser(
                partnerJsonParser);

        try {
            featureCollectionJsonParser.parse(jsonReader);

            context.getContentResolver().applyBatch(
                    GonetteContract.AUTHORITY,
                    operations
            );
        } catch (IOException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "parseData: " + e.getMessage(), e);
        }
    }
}
