package com.zxcv.gonette.app.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.GoogleMap;
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.contract.MapsContract;
import com.zxcv.gonette.app.presenter.base.LoaderPresenter;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.loader.PartnerCursorLoaderHelper;
import com.zxcv.gonette.content.reader.PartnerReader;

public class MapsPresenter extends LoaderPresenter implements MapsContract.Presenter {

    @NonNull
    private final MapsContract.View mView;

    public MapsPresenter(@NonNull MapsContract.View view) {
        mView = view;
    }

    @NonNull
    @Override
    protected LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        loadPartners();
    }

    @Override
    protected void onReattachBundleLoader() {
        // Do nothing here
    }

    @Override
    public Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_map_partners:
                String search = PartnerCursorLoaderHelper.getSearch(args);
                return new CursorLoader(
                        mView.getContext(),
                        GonetteContract.Partner.METADATA_CONTENT_URI,
                        new String[]{
                                GonetteContract.Partner.ID,
                                GonetteContract.Partner.NAME,
                                GonetteContract.Partner.DESCRIPTION,
                                GonetteContract.Partner.LATITUDE,
                                GonetteContract.Partner.LONGITUDE
                        },
                        GonetteContract.PartnerMetadata.IS_VISIBLE + " = 1 AND " + GonetteContract.Partner.NAME + " LIKE ?",

                        new String[]{
                                "%" + search + "%"
                        },
                        null
                );
            default:
                return super.onCreateCursorLoader(id, args);
        }
    }

    @Override
    public void onCursorLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                onQueryPartnerLoadFinished(cursor);
                break;
            default:
                super.onCursorLoadFinished(loader, cursor);
        }
    }

    private void onQueryPartnerLoadFinished(Cursor cursor) {
        mView.showPartners(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    @Override
    public void onCursorLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                // Do nothing.
                break;
            default:
                super.onCursorLoaderReset(loader);
        }
    }

    @Override
    public void loadPartners() {
        initCursorLoader(
                R.id.loader_query_map_partners,
                null
        );
    }

    @Override
    public void loadPartners(@NonNull String search) {
        restartCursorLoader(
                R.id.loader_query_map_partners,
                PartnerCursorLoaderHelper.getArgs(search)
        );
    }

    public void startDirection(double latitude, double longitude) {
        Intent intent = new Intent(
                android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + latitude + "," + longitude)
        );
        PackageManager packageManager = mView.getContext().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            mView.getContext().startActivity(intent);
        } else {
            mView.errorNoDirectionAppFound();
        }
    }
}
