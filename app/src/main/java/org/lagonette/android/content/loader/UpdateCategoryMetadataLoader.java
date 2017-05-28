package org.lagonette.android.content.loader;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.loader.base.BundleLoader;

import java.util.ArrayList;

public class UpdateCategoryMetadataLoader extends BundleLoader {

    private static final String TAG = "UpdateCategoryMetadataL";

    private static final String ARG_CATEGORY_ID = "arg:category_id";

    private static final String ARG_IS_VISIBLE = "arg:is_visible";

    private boolean mIsVisible;

    private long mCategoryId;

    @NonNull
    public static Bundle getUpdateVisibilityArgs(long categoryId, boolean isVisible) {
        Bundle args = new Bundle(2);
        args.putLong(ARG_CATEGORY_ID, categoryId);
        args.putBoolean(ARG_IS_VISIBLE, isVisible);
        return args;
    }

    public UpdateCategoryMetadataLoader(Context context, Bundle args) {
        super(context, args);
    }

    @Override
    protected void readArguments(@NonNull Bundle args) {
        mCategoryId = args.getLong(ARG_CATEGORY_ID, GonetteContract.NO_ID);
        mIsVisible = args.getBoolean(ARG_IS_VISIBLE, false);
    }

    @Override
    public Bundle loadInBackground() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(
                ContentProviderOperation.newUpdate(GonetteContract.CategoryMetadata.CONTENT_URI)
                        .withValue(GonetteContract.CategoryMetadata.IS_VISIBLE, mIsVisible)
                        .withSelection(
                                GonetteContract.CategoryMetadata.CATEGORY_ID + " = ?",
                                new String[]{
                                        String.valueOf(mCategoryId)
                                }
                        )
                        .build()
        );

        try {
            mContext.getContentResolver().applyBatch(
                    GonetteContract.AUTHORITY,
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
