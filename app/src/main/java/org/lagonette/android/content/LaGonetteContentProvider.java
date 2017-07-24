package org.lagonette.android.content;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
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
import org.lagonette.android.content.contract.LaGonetteContract;
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

    private boolean mNotifyUri = true;

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
                return LaGonetteContract.Partner.CONTENT_TYPE_ITEM;
            case R.id.content_uri_partners:
                return LaGonetteContract.Partner.CONTENT_TYPE_DIR;
            case R.id.content_uri_partners_extended:
                return LaGonetteContract.Partner.CONTENT_TYPE_DIR;
            case R.id.content_uri_partners_metadata:
                return LaGonetteContract.PartnerMetadata.CONTENT_TYPE_DIR;
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
        String table;
        Uri baseUri;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                table = Tables.PARTNER;
                baseUri = LaGonetteContract.Partner.CONTENT_URI;
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = LaGonetteContract.PartnerMetadata.CONTENT_URI;
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = LaGonetteContract.PartnerSideCategories.CONTENT_URI;
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = LaGonetteContract.Category.CONTENT_URI;
                break;
            case R.id.content_uri_categories_metadata:
                table = Tables.CATEGORY_METADATA;
                baseUri = LaGonetteContract.CategoryMetadata.CONTENT_URI;
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

        if (mNotifyUri) {
            Log.d(TAG, "insert: Notify " + newUri);
            // getContext() not null because called after onCreate()
            // noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(newUri, null);
        }

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String table;
        Uri baseUri;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case R.id.content_uri_partners:
                table = Tables.PARTNER;
                baseUri = LaGonetteContract.Partner.CONTENT_URI;
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = LaGonetteContract.PartnerMetadata.CONTENT_URI;
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = LaGonetteContract.PartnerSideCategories.CONTENT_URI;
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = LaGonetteContract.Category.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: %s", match));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int n = db.delete(table, selection, selectionArgs);
        Log.d(TAG, "delete: Delete " + n + " rows in " + table);

        if (mNotifyUri && n > 0) {
            Log.d(TAG, "delete: Notify " + baseUri);
            // getContext() not null because called after onCreate()
            // noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(baseUri, null);
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
                baseUri = LaGonetteContract.Partner.CONTENT_URI;
                break;
            case R.id.content_uri_partners_metadata:
                table = Tables.PARTNER_METADATA;
                baseUri = LaGonetteContract.PartnerMetadata.CONTENT_URI;
                break;
            case R.id.content_uri_partners_side_categories:
                table = Tables.PARTNER_SIDE_CATEGORIES;
                baseUri = LaGonetteContract.PartnerSideCategories.CONTENT_URI;
                break;
            case R.id.content_uri_categories:
                table = Tables.CATEGORY;
                baseUri = LaGonetteContract.Category.CONTENT_URI;
                break;
            case R.id.content_uri_categories_metadata:
                table = Tables.CATEGORY_METADATA;
                baseUri = LaGonetteContract.Category.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown content uri code: %s", match));
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int n = db.update(table, values, selection, selectionArgs);
        Log.d(TAG, "update: Update " + n + " rows in " + table);

        if (mNotifyUri && n > 0) {
            Log.d(TAG, "update: Notify " + baseUri);
            // getContext() not null because called after onCreate()
            // noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(baseUri, null);
        }

        return n;
    }

    private void setupUriMatcher() {
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Partner.CONTENT_URI.getPath(),
                R.id.content_uri_partners
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Partner.CONTENT_URI.getPath() + "/#",
                R.id.content_uri_partner
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Partner.EXTENDED_CONTENT_URI.getPath(),
                R.id.content_uri_partners_extended
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.PartnerMetadata.CONTENT_URI.getPath(),
                R.id.content_uri_partners_metadata
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.PartnerSideCategories.CONTENT_URI.getPath(),
                R.id.content_uri_partners_side_categories
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Filter.CONTENT_URI.getPath(),
                R.id.content_uri_filters
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Map.CONTENT_URI.getPath(),
                R.id.content_uri_map
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Category.CONTENT_URI.getPath(),
                R.id.content_uri_categories
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.Category.EXTENDED_CONTENT_URI.getPath(),
                R.id.content_uri_categories_extended
        );
        addUriToUriMatcher(
                mUriMatcher,
                LaGonetteContract.AUTHORITY,
                LaGonetteContract.CategoryMetadata.CONTENT_URI.getPath(),
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

    private static boolean addUri(ArrayList<Uri> uris, Uri uri) {
        if (!uris.contains(uri)) {
            uris.add(uri);

            if (BuildConfig.DEBUG) {
                Uri debugUri = DebugContentProviderUtil.removeForceLog(uri);
                debugUri = DebugContentProviderUtil.removeSlowDown(debugUri);
                if (!uris.contains(debugUri)) {
                    uris.add(debugUri);
                }
            }
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        ArrayList<Uri> uris = new ArrayList<>();
        mNotifyUri = false;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransactionNonExclusive();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                ContentProviderOperation operation = operations.get(i);
                if (operation.isYieldAllowed()) {
                    db.yieldIfContendedSafely();
                }
                results[i] = operation.apply(this, results, i);
                Uri uri = operation.getUri();
                if (addUri(uris, uri)) {
                    int match = mUriMatcher.match(uri);
                    switch (match) {
                        case R.id.content_uri_partners:
                            addUri(uris, LaGonetteContract.Partner.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Filter.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Map.CONTENT_URI);
                            break;
                        case R.id.content_uri_partners_metadata:
                            addUri(uris, LaGonetteContract.Partner.EXTENDED_CONTENT_URI);
                            addUri(uris, LaGonetteContract.Filter.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Map.CONTENT_URI);
                            break;
                        case R.id.content_uri_partners_side_categories:
                            addUri(uris, LaGonetteContract.PartnerSideCategories.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Filter.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Map.CONTENT_URI);
                            break;
                        case R.id.content_uri_categories:
                            addUri(uris, LaGonetteContract.Category.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Filter.CONTENT_URI);
                            addUri(uris, LaGonetteContract.Map.CONTENT_URI);
                            break;
                    }
                }
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
            mNotifyUri = true;
            if (!uris.isEmpty()) {
                // getContext() not null because called after onCreate()
                // noinspection ConstantConditions
                ContentResolver contentResolver = getContext().getContentResolver();

                for (Uri uri : uris) {
                    Log.d(TAG, "applyBatch() notifyChange uri = " + uri.toString());
                    contentResolver.notifyChange(uri, null);
                }

                uris.clear();
            }
        }
    }
}
