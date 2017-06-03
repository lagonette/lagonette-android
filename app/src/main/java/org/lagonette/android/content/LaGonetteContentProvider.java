package org.lagonette.android.content;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.R;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.database.LaGonetteDatabaseOpenHelper;
import org.lagonette.android.database.Tables;
import org.lagonette.android.database.statement.FilterStatement;
import org.lagonette.android.database.statement.MapStatement;
import org.lagonette.android.util.DebugContentProviderUtil;

import java.util.ArrayList;
import java.util.List;

public class LaGonetteContentProvider
        extends ContentProvider {

    private static final String TAG = "LaGonetteContentProvide";

    private UriMatcher mUriMatcher;

    private LaGonetteDatabaseOpenHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new LaGonetteDatabaseOpenHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        setupUriMatcher();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        Log.d(TAG, "query: " + uri.toString());
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                qb.setTables(Tables.PARTNER);
                break;
            case R.id.content_uri_partners_extended:
                qb.setTables(MapStatement.getMapsPartnerStatement());
                break;
            case R.id.content_uri_map:
                qb.setTables(MapStatement.getMapsPartnerStatement());
                break;
            case R.id.content_uri_filters:
                qb.setTables(FilterStatement.getFilterStatement());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown uri: %s", uri));
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        if (BuildConfig.DEBUG && BuildConfig.LOG_SQL) {
            DebugContentProviderUtil.logSqlQuery(
                    uri,
                    qb,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
        }

        Cursor cursor = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                null
        );

        assert null != getContext();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partner:
                return GonetteContract.Partner.CONTENT_TYPE_ITEM;
            case R.id.content_uri_partners:
                return GonetteContract.Partner.CONTENT_TYPE_DIR;
            case R.id.content_uri_partners_extended:
                return GonetteContract.Partner.CONTENT_TYPE_DIR;
            case R.id.content_uri_partners_metadata:
                return GonetteContract.PartnerMetadata.CONTENT_TYPE_DIR;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unknown content uri code: %s",
                        match
                ));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        List<Uri> uris = new ArrayList<>();
        String table;
        Uri baseUri;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                table = Tables.PARTNER;
                baseUri = GonetteContract.Partner.CONTENT_URI;
                uris.add(GonetteContract.Partner.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = GonetteContract.PartnerMetadata.CONTENT_URI;
                uris.add(GonetteContract.Partner.EXTENDED_CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = GonetteContract.PartnerSideCategories.CONTENT_URI;
                uris.add(GonetteContract.PartnerSideCategories.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = GonetteContract.Category.CONTENT_URI;
                uris.add(GonetteContract.Category.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_categories_metadata:
                table = Tables.CATEGORY_METADATA;
                baseUri = GonetteContract.CategoryMetadata.CONTENT_URI;
                uris.add(GonetteContract.Category.EXTENDED_CONTENT_URI);
                uris.add(GonetteContract.CategoryMetadata.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unknown content uri code: %s",
                        match
                ));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowID = db.insert(table, null, values);
        Uri newUri = ContentUris.withAppendedId(baseUri, rowID);
        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        ContentResolver contentResolver = getContext().getContentResolver();
        Log.d(TAG, "insert: Notify " + newUri);
        contentResolver.notifyChange(newUri, null);
        for (Uri u : uris) {
            Log.d(TAG, "insert: Notify " + u);
            contentResolver.notifyChange(u, null);
        }

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        List<Uri> uris = new ArrayList<>();
        String table;
        Uri baseUri;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                table = Tables.PARTNER;
                baseUri = GonetteContract.Partner.CONTENT_URI;
                uris.add(GonetteContract.Partner.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = GonetteContract.PartnerMetadata.CONTENT_URI;
                uris.add(GonetteContract.Partner.EXTENDED_CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = GonetteContract.PartnerSideCategories.CONTENT_URI;
                uris.add(GonetteContract.PartnerSideCategories.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = GonetteContract.Category.CONTENT_URI;
                uris.add(GonetteContract.Category.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: %s", match));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int n = db.delete(table, selection, selectionArgs);
        Log.d(TAG, "delete: Delete " + n + " rows in " + table);

        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        ContentResolver contentResolver = getContext().getContentResolver();
        Log.d(TAG, "delete: Notify " + baseUri);
        contentResolver.notifyChange(baseUri, null);
        for (Uri u : uris) {
            Log.d(TAG, "delete: Notify " + u);
            contentResolver.notifyChange(u, null);
        }

        return n;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        List<Uri> uris = new ArrayList<>();
        String table;
        Uri baseUri;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                table = Tables.PARTNER;
                baseUri = GonetteContract.Partner.CONTENT_URI;
                uris.add(GonetteContract.Partner.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = GonetteContract.PartnerMetadata.CONTENT_URI;
                uris.add(GonetteContract.Partner.EXTENDED_CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = GonetteContract.PartnerSideCategories.CONTENT_URI;
                uris.add(GonetteContract.PartnerSideCategories.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = GonetteContract.Category.CONTENT_URI;
                uris.add(GonetteContract.Category.CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            case R.id.content_uri_categories_metadata:
                table = Tables.CATEGORY_METADATA;
                baseUri = GonetteContract.Category.CONTENT_URI;
                uris.add(GonetteContract.Category.EXTENDED_CONTENT_URI);
                uris.add(GonetteContract.Filter.CONTENT_URI);
                uris.add(GonetteContract.Map.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: %s", match));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int n = db.update(table, values, selection, selectionArgs);
        Log.d(TAG, "update: Update " + n + " rows in " + table);

        // getContext() not null because called after onCreate()
        //noinspection ConstantConditions
        ContentResolver contentResolver = getContext().getContentResolver();
        Log.d(TAG, "update: Notify " + baseUri);
        contentResolver.notifyChange(baseUri, null);
        for (Uri u : uris) {
            Log.d(TAG, "update: Notify " + u);
            contentResolver.notifyChange(u, null);
        }

        return n;
    }

    private void setupUriMatcher() {
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Partner.CONTENT_URI.getPath(),
                R.id.content_uri_partners
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Partner.CONTENT_URI.getPath() + "/#",
                R.id.content_uri_partner
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Partner.EXTENDED_CONTENT_URI.getPath(),
                R.id.content_uri_partners_extended
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.PartnerMetadata.CONTENT_URI.getPath(),
                R.id.content_uri_partners_metadata
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.PartnerSideCategories.CONTENT_URI.getPath(),
                R.id.content_uri_partners_side_categories
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Filter.CONTENT_URI.getPath(),
                R.id.content_uri_filters
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Map.CONTENT_URI.getPath(),
                R.id.content_uri_map
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Category.CONTENT_URI.getPath(),
                R.id.content_uri_categories
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.Category.EXTENDED_CONTENT_URI.getPath(),
                R.id.content_uri_categories_extended
        );
        addUriToUriMatcher(
                mUriMatcher,
                GonetteContract.AUTHORITY,
                GonetteContract.CategoryMetadata.CONTENT_URI.getPath(),
                R.id.content_uri_categories_metadata
        );
    }

    private static void addUriToUriMatcher(
            @NonNull UriMatcher uriMatcher,
            @NonNull String authority,
            @NonNull String path,
            int code) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            path = path.substring(1);
        }
        uriMatcher.addURI(authority, path, code);
    }
}
