package com.zxcv.gonette.content.repo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.zxcv.gonette.R;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.loader.InsertPartnerVisibilityLoader;
import com.zxcv.gonette.content.loader.PartnerCursorLoaderHelper;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public class PartnerRepo
        extends GonetteRepo<PartnerRepo.Callback> {

    public interface Callback extends GonetteRepo.Callback {

        void onPartnerLoaded(@Nullable PartnerReader reader);

        void onPartnersVisibilityLoaded(@Nullable PartnersVisibilityReader reader);

        void onPartnerReset();

        void onPartnersVisibilityReset();

    }

    public PartnerRepo(@NonNull Callback callback) {
        super(callback);
    }

    @Override
    protected Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_filters_partners:
                return onCreateQueryPartnersLoader(args);
            case R.id.loader_query_filters_partners_visibility:
                return onCreateQueryPartnersVisibilityLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    @Override
    protected void onCursorLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                onQueryPartnersLoadFinished(cursor);
                break;
            case R.id.loader_query_filters_partners_visibility:
                onQueryPartnersVisibilityLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    @Override
    protected void onCursorLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                mCallback.onPartnerReset();
                break;
            case R.id.loader_query_filters_partners_visibility:
                mCallback.onPartnersVisibilityReset();
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    @Override
    protected Loader<Bundle> onCreateBundleLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                return new InsertPartnerVisibilityLoader(mCallback.getContext(), args);
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    @Override
    protected void onBundleLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                // Do nothing
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    @Override
    protected void onBundleLoaderReset(@NonNull Loader<Bundle> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                // Do nothing
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    private Loader<Cursor> onCreateQueryPartnersLoader(Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoader(
                mCallback.getContext(),
                GonetteContract.Partner.METADATA_CONTENT_URI,
                new String[]{
                        GonetteContract.Partner.ID,
                        GonetteContract.Partner.NAME,
                        GonetteContract.PartnerMetadata.IS_VISIBLE
                },
                GonetteContract.Partner.NAME + " LIKE ?",
                new String[]{
                        "%" + search + "%"
                },
                GonetteContract.Partner.NAME + " ASC"
        );
    }

    private Loader<Cursor> onCreateQueryPartnersVisibilityLoader(Bundle args) {
        return new CursorLoader(
                mCallback.getContext(),
                GonetteContract.Partner.METADATA_CONTENT_URI,
                new String[]{
                        PartnersVisibilityReader.getPartnerVisibilityCountProjection()
                },
                GonetteContract.PartnerMetadata.IS_VISIBLE + " > ?",
                new String[]{
                        String.valueOf(0)
                },
                null
        );
    }

    private void onQueryPartnersLoadFinished(Cursor cursor) {
        mCallback.onPartnerLoaded(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    private void onQueryPartnersVisibilityLoadFinished(Cursor cursor) {
        mCallback.onPartnersVisibilityLoaded(
                cursor != null
                        ? new PartnersVisibilityReader(cursor)
                        : null
        );
    }

    public void loadPartners(@NonNull String search) {
        restartCursorLoader(
                R.id.loader_query_filters_partners,
                PartnerCursorLoaderHelper.getArgs(search)
        );
    }

    public void loadPartnersVisibility() {
        initCursorLoader(
                R.id.loader_query_filters_partners_visibility,
                null
        );
    }

    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(
                partnerId,
                isVisible
        );
        initBundleLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

    public void setPartnersVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        initBundleLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

}
