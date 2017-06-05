package org.lagonette.android.content.loader;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.base.BundleLoader;

import java.util.ArrayList;

public class UpdatePartnerMetadataLoader extends BundleLoader {

    private static final String TAG = "UpdatePartnerMetadataLo";

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    private static final String ARG_IS_VISIBLE = "arg:is_visible";

    private boolean mIsVisible;

    private long mPartnerId;

    @NonNull
    public static Bundle getUpdateVisibilityArgs(long partnerId, boolean isVisible) {
        Bundle args = new Bundle(2);
        args.putLong(ARG_PARTNER_ID, partnerId);
        args.putBoolean(ARG_IS_VISIBLE, isVisible);
        return args;
    }

    public UpdatePartnerMetadataLoader(Context context, Bundle args) {
        super(context, args);
    }

    @Override
    protected void readArguments(@NonNull Bundle args) {
        mPartnerId = args.getLong(ARG_PARTNER_ID, LaGonetteContract.NO_ID);
        mIsVisible = args.getBoolean(ARG_IS_VISIBLE, false);
    }

    @Override
    public Bundle loadInBackground() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(
                ContentProviderOperation.newUpdate(LaGonetteContract.PartnerMetadata.CONTENT_URI)
                        .withValue(LaGonetteContract.PartnerMetadata.IS_VISIBLE, mIsVisible)
                        .withSelection(
                                LaGonetteContract.PartnerMetadata.PARTNER_ID + " = ?",
                                new String[]{
                                        String.valueOf(mPartnerId)
                                }
                        )
                        .build()
        );

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
