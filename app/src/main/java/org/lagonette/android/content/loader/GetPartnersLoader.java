package org.lagonette.android.content.loader;


import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.api.response.CategoriesResponse;
import org.lagonette.android.api.response.PartnersResponse;
import org.lagonette.android.api.service.LaGonetteService;
import org.lagonette.android.content.loader.base.BundleLoader;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.entity.Category;
import org.lagonette.android.room.entity.CategoryMetadata;
import org.lagonette.android.room.entity.Partner;
import org.lagonette.android.room.entity.PartnerMetadata;
import org.lagonette.android.room.entity.PartnerSideCategory;
import org.lagonette.android.util.DB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            LaGonetteDatabase database = DB.get(mContext);
            List<Category> categories = new ArrayList<>();
            List<CategoryMetadata> categoryMetadataList = new ArrayList<>();
            List<Partner> partners = new ArrayList<>();
            List<PartnerMetadata> partnerMetadataList = new ArrayList<>();
            List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

            // TODO Exception ? Finally ?
            database.beginTransaction();

            if (getCategories(service, categories, categoryMetadataList)) {
                database.categoryDao().deleteCategories();
                database.categoryDao().insertCategories(categories);
                database.categoryDao().insertCategoriesMetadatas(categoryMetadataList);
                if (getPartners(service, partners, partnerMetadataList, partnerSideCategories)) {
                    database.partnerDao().deletePartners();
                    database.partnerDao().insertPartners(partners);
                    database.partnerDao().insertPartnersMetadatas(partnerMetadataList);
                    database.partnerDao().deletePartnerSideCategories();
                    database.partnerDao().insertPartnersSideCategories(partnerSideCategories);
                    database.setTransactionSuccessful();
                    mBundle.putInt(ARG_STATUS, STATUS_OK);
                }
            }

            database.endTransaction();

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
            @NonNull List<Category> categories,
            @NonNull List<CategoryMetadata> categoryMetadataList)
            throws IOException, RemoteException, OperationApplicationException {

        Call<CategoriesResponse> categoryCall = service.getCategories();
        Response<CategoriesResponse> response = categoryCall.execute();

        if (response.isSuccessful()) {
            CategoriesResponse result = response.body();
            result.prepareInsert(categories, categoryMetadataList);
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
            @NonNull List<Partner> partners,
            @NonNull List<PartnerMetadata> partnerMetadataList,
            @NonNull List<PartnerSideCategory> partnerSideCategories)
            throws IOException, RemoteException, OperationApplicationException {

        Call<PartnersResponse> partnersCall = service.getPartners();
        Response<PartnersResponse> response = partnersCall.execute();

        if (response.isSuccessful()) {
            PartnersResponse result = response.body();
            result.prepareInsert(partners, partnerMetadataList, partnerSideCategories);
            return true;
        } else {
            mBundle.putInt(ARG_STATUS, STATUS_ERROR);
            mBundle.putInt(ARG_ERROR_CODE, response.code());
            FirebaseCrash.logcat(Log.ERROR, TAG, response.code() + ": " + response.message());
            return false;
        }
    }

}
