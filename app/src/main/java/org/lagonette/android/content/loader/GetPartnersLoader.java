package org.lagonette.android.content.loader;


import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.api.CategoriesResponse;
import org.lagonette.android.api.LaGonetteService;
import org.lagonette.android.api.PartnersResponse;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.loader.base.BundleLoader;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class GetPartnersLoader extends BundleLoader {

    private static final String TAG = "GetPartnersLoader";

    private static final String ARG_ERROR_CODE = "arg:error_code";

    public GetPartnersLoader(@NonNull Context context, @Nullable Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public Bundle loadInBackground() {
        try {
            LaGonetteService service = LaGonetteService.retrofit.create(LaGonetteService.class);
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            ContentValues contentValues = new ContentValues();

            if (getCategories(service, operations, contentValues)) {
                if (getPartners(service, operations, contentValues)) {
                    mContext.getContentResolver().applyBatch(
                            GonetteContract.AUTHORITY,
                            operations
                    );
                    mBundle.putInt(ARG_STATUS, STATUS_OK);
                }
            }

            return mBundle;

        } catch (IOException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "loadInBackground: ", e);
            FirebaseCrash.report(e);
            mBundle.putInt(ARG_STATUS, STATUS_ERROR);
            return mBundle;
        }
    }

    private boolean getCategories(
            @NonNull LaGonetteService service,
            @NonNull ArrayList<ContentProviderOperation> operations,
            @NonNull ContentValues contentValues)
            throws IOException, RemoteException, OperationApplicationException {

        Call<CategoriesResponse> categoryCall = service.getCategories();
        Response<CategoriesResponse> response = categoryCall.execute();

        if (response.isSuccessful()) {
            CategoriesResponse result = response.body();
            result.prepareInsert(operations, contentValues);
            return true;
        } else {
            mBundle.putInt(ARG_STATUS, STATUS_ERROR);
            mBundle.putInt(ARG_ERROR_CODE, response.code());
            FirebaseCrash.logcat(Log.ERROR, TAG, response.code() + ": " + response.message());
            return false;
        }
    }

    private boolean getPartners(
            @NonNull LaGonetteService service,
            @NonNull ArrayList<ContentProviderOperation> operations,
            @NonNull ContentValues contentValues)
            throws IOException, RemoteException, OperationApplicationException {

        Call<PartnersResponse> partnersCall = service.getPartners();
        Response<PartnersResponse> response = partnersCall.execute();

        if (response.isSuccessful()) {
            PartnersResponse result = response.body();
            result.prepareInsert(operations, contentValues);
            return true;
        } else {
            mBundle.putInt(ARG_STATUS, STATUS_ERROR);
            mBundle.putInt(ARG_ERROR_CODE, response.code());
            FirebaseCrash.logcat(Log.ERROR, TAG, response.code() + ": " + response.message());
            return false;
        }
    }

}
