package org.lagonette.android.content.loader;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.BuildConfig;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.base.BundleLoader;
import org.lagonette.android.database.statement.CategoryMetadataStatement;

import java.util.ArrayList;

public class UpdateCategoryMetadataLoader extends BundleLoader {

    private static final String TAG = "UpdateCategoryMetadataL";

    private static final String ARG_CATEGORY_ID = "arg:category_id";

    private static final String ARG_IS_VISIBLE = "arg:is_visible";

    private static final String ARG_IS_COLLAPSED = "arg:is_collapsed";

    private boolean mIsVisible;

    private boolean mIsCollapsed;

    private boolean mUpdateCollapsedState;

    private boolean mUpdateVisibility;

    private long mCategoryId;

    @NonNull
    public static Bundle getUpdateVisibilityArgs(long categoryId, boolean isVisible) {
        Bundle args = new Bundle(2);
        args.putLong(ARG_CATEGORY_ID, categoryId);
        args.putBoolean(ARG_IS_VISIBLE, isVisible);
        return args;
    }

    @NonNull
    public static Bundle getUpdateCollapsedStateArgs(long categoryId, boolean isColapsed) {
        Bundle args = new Bundle(2);
        args.putLong(ARG_CATEGORY_ID, categoryId);
        args.putBoolean(ARG_IS_COLLAPSED, isColapsed);
        return args;
    }

    public UpdateCategoryMetadataLoader(Context context, Bundle args) {
        super(context, args);
    }

    @Override
    protected void readArguments(@NonNull Bundle args) {
        mCategoryId = args.getLong(ARG_CATEGORY_ID, LaGonetteContract.NO_ID);

        mUpdateVisibility = args.containsKey(ARG_IS_VISIBLE);
        if (mUpdateVisibility) {
            mIsVisible = args.getBoolean(ARG_IS_VISIBLE);
        }

        mUpdateCollapsedState = args.containsKey(ARG_IS_COLLAPSED);
        if (mUpdateCollapsedState) {
            mIsCollapsed = args.getBoolean(ARG_IS_COLLAPSED);
        }

        if (BuildConfig.DEBUG) {
            if (mCategoryId == LaGonetteContract.NO_ID) {
                throw new IllegalArgumentException("You must provide a category id");
            }
        }
    }

    @Override
    public Bundle loadInBackground() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newUpdate(LaGonetteContract.CategoryMetadata.CONTENT_URI)
                .withSelection(
                        CategoryMetadataStatement.getSelections(),
                        CategoryMetadataStatement.getSelectionsArgs(mCategoryId)
                );

        if (mUpdateVisibility) {
            builder.withValue(LaGonetteContract.CategoryMetadata.IS_VISIBLE, mIsVisible);
        }

        if (mUpdateCollapsedState) {
            builder.withValue(LaGonetteContract.CategoryMetadata.IS_COLLAPSED, mIsCollapsed);
        }

        operations.add(builder.build());

        try {
            mContext.getContentResolver().applyBatch(
                    LaGonetteContract.AUTHORITY,
                    operations
            );
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "loadInBackground: ", e);
            FirebaseCrash.report(e);
        }

        mBundle.putInt(ARG_STATUS, STATUS_OK);
        return mBundle;
    }
}
