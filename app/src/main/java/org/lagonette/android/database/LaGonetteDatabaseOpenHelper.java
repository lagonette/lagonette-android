package org.lagonette.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.lagonette.android.database.columns.CategoryColumns;
import org.lagonette.android.database.columns.CategoryMetadataColumns;
import org.lagonette.android.database.columns.PartnerColumns;
import org.lagonette.android.database.columns.PartnerMetadataColumns;
import org.lagonette.android.database.columns.PartnerSideCategoriesColumns;
import org.lagonette.android.util.SqlUtil;

public class LaGonetteDatabaseOpenHelper
        extends SQLiteOpenHelper {

    private static final String TAG = "GonetteDatabaseOpenHelp";

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "gonette.db";

    public LaGonetteDatabaseOpenHelper(Context context) {
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
        db.execSQL(
                SqlUtil.build()
                        .createTable(Tables.CATEGORY)
                            .field(CategoryColumns.ID).integer().primaryKey().onConflictReplace()
                            .field(CategoryColumns.LABEL).text().notNull()
                            .field(CategoryColumns.ICON).text().notNull()
                        .endTable()
                        .toString()
        );

        db.execSQL(
                SqlUtil.build()
                        .createTable(Tables.PARTNER)
                            .field(PartnerColumns.ID).integer().primaryKey().onConflictReplace()
                            .field(PartnerColumns.CLIENT_CODE).text()
                            .field(PartnerColumns.NAME).text()
                            .field(PartnerColumns.LATITUDE).numeric()
                            .field(PartnerColumns.LONGITUDE).numeric()
                            .field(PartnerColumns.DESCRIPTION).text()
                            .field(PartnerColumns.ADDRESS).text()
                            .field(PartnerColumns.CITY).text()
                            .field(PartnerColumns.LOGO).text()
                            .field(PartnerColumns.ZIP_CODE).text()
                            .field(PartnerColumns.PHONE).text()
                            .field(PartnerColumns.WEBSITE).text()
                            .field(PartnerColumns.EMAIL).text()
                            .field(PartnerColumns.OPENING_HOURS ).text()
                            .field(PartnerColumns.IS_EXCHANGE_OFFICE).integer()
                            .field(PartnerColumns.SHORT_DESCRIPTION).text()
                            .field(PartnerColumns.MAIN_CATEGORY).integer()
                            .field(PartnerColumns.PARTNER_SIDE_CATEGORIES_ID).integer()
                        .endTable()
                        .toString()
        );

        db.execSQL(
                SqlUtil.build()
                        .createTable(Tables.PARTNER_SIDE_CATEGORIES)
                            .field(PartnerSideCategoriesColumns.CATEGORY_ID).integer()
                            .field(PartnerSideCategoriesColumns.PARTNER_ID).integer()
                            .setPrimaryKey()
                                .key(PartnerSideCategoriesColumns.CATEGORY_ID)
                                .key(PartnerSideCategoriesColumns.PARTNER_ID)
                            .endPrimaryKey()
                        .endTable()
                        .toString()
        );

        db.execSQL(
                SqlUtil.build()
                        .createTable(Tables.PARTNER_METADATA)
                            .field(PartnerMetadataColumns.PARTNER_ID).integer().primaryKey().onConflictIgnore()
                            .field(PartnerMetadataColumns.IS_VISIBLE).integer().notNull()
                        .endTable()
                        .toString()
        );

        db.execSQL(
                SqlUtil.build()
                        .createTable(Tables.CATEGORY_METADATA)
                            .field(CategoryMetadataColumns.CATEGORY_ID).integer().primaryKey().onConflictIgnore()
                            .field(CategoryMetadataColumns.IS_VISIBLE).integer().notNull()
                            .field(CategoryMetadataColumns.IS_COLLAPSED).integer().notNull()
                        .endTable()
                        .toString()
        );
    }

    private void dropDatabase(SQLiteDatabase db) {
        db.execSQL(SqlUtil.build().dropTableIfExists(Tables.PARTNER_METADATA).toString());
        db.execSQL(SqlUtil.build().dropTableIfExists(Tables.PARTNER_SIDE_CATEGORIES).toString());
        db.execSQL(SqlUtil.build().dropTableIfExists(Tables.PARTNER).toString());
        db.execSQL(SqlUtil.build().dropTableIfExists(Tables.CATEGORY_METADATA).toString());
        db.execSQL(SqlUtil.build().dropTableIfExists(Tables.CATEGORY).toString());
    }

}
