package com.gonette.android.content.loader;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.gonette.android.content.contract.GonetteContract;
import com.gonette.android.content.loader.base.BundleLoader;

import java.util.ArrayList;

public class InsertPartnerVisibilityLoader extends BundleLoader {

    private static final String TAG = "InsertPartnerVisibility";

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    private static final String ARG_IS_VISIBLE = "arg:is_visible";

    private boolean mIsVisible;

    private long mPartnerId;

    private boolean mAllPartner;

    @NonNull
    public static Bundle getArgs(long partnerId, boolean isVisible) {
        Bundle args = new Bundle(2);
        args.putLong(ARG_PARTNER_ID, partnerId);
        args.putBoolean(ARG_IS_VISIBLE, isVisible);
        return args;
    }

    @NonNull
    public static Bundle getArgs(boolean isVisible) {
        Bundle args = new Bundle(1);
        args.putBoolean(ARG_IS_VISIBLE, isVisible);
        return args;
    }

    public InsertPartnerVisibilityLoader(Context context, Bundle args) {
        super(context, args);
    }

    @Override
    protected void readArguments(@NonNull Bundle args) {
        mPartnerId = args.getLong(ARG_PARTNER_ID, GonetteContract.NO_ID);
        mIsVisible = args.getBoolean(ARG_IS_VISIBLE, false);
        mAllPartner = mPartnerId == GonetteContract.NO_ID;
    }

    @Override
    public Bundle loadInBackground() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        if (mAllPartner) {
            operations.add(
                    ContentProviderOperation.newUpdate(GonetteContract.PartnerMetadata.CONTENT_URI)
                            .withValue(GonetteContract.PartnerMetadata.IS_VISIBLE, mIsVisible)
                            .build()
            );
        } else {
            operations.add(
                    ContentProviderOperation.newUpdate(GonetteContract.PartnerMetadata.CONTENT_URI)
                            .withValue(GonetteContract.PartnerMetadata.IS_VISIBLE, mIsVisible)
                            .withSelection(
                                    GonetteContract.PartnerMetadata.PARTNER_ID + " = ?",
                                    new String[]{
                                            String.valueOf(mPartnerId)
                                    }
                            )
                            .build()
            );
        }
        try {
            mContext.getContentResolver().applyBatch(
                    GonetteContract.AUTHORITY,
                    operations
            );
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace(); // TODO
        }
        mBundle.putInt(ARG_STATUS, STATUS_OK);
        return mBundle;
    }
}
