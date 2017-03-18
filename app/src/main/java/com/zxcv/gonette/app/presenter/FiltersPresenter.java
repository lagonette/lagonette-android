package com.zxcv.gonette.app.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.contract.FiltersContract;
import com.zxcv.gonette.app.fragment.FiltersFragment;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.loader.InsertPartnerVisibilityLoader;
import com.zxcv.gonette.content.loader.PartnerCursorLoaderHelper;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;
import com.zxcv.gonette.util.SearchUtil;

public class FiltersPresenter
        extends FragmentPresenter
        implements FiltersContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_SEARCH = "arg:search";

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        new FiltersPresenter(fragment);
        return fragment;
    }

    @NonNull
    private FiltersContract.Fragment mFragment;

    @NonNull
    private String mCurrentSearch = SearchUtil.DEFAULT_SEARCH;

    @NonNull
    private LoaderManager.LoaderCallbacks<Bundle> mBundleLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bundle>() {
        @Override
        public Loader<Bundle> onCreateLoader(int id, Bundle args) {
            return new InsertPartnerVisibilityLoader(mFragment.getContext(), args);
        }

        @Override
        public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
            mFragment.getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<Bundle> loader) {
            // Do nothing
        }
    };

    public FiltersPresenter(@NonNull FiltersContract.Fragment fragment) {
        mFragment = fragment;
        mFragment.setPresenter(FiltersPresenter.this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mFragment.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPartnersVisibility();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_filters_partners:
                return onCreateQueryPartnersLoader(args);
            case R.id.loader_query_filters_partners_visibility:
                return onCreateQueryPartnersVisibilityLoader(args);
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    private Loader<Cursor> onCreateQueryPartnersLoader(Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoader(
                mFragment.getContext(),
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
                mFragment.getContext(),
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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

    private void onQueryPartnersLoadFinished(Cursor cursor) {
        mFragment.displayPartners(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    private void onQueryPartnersVisibilityLoadFinished(Cursor cursor) {
        mFragment.displayPartnersVisibility(
                cursor != null
                        ? new PartnersVisibilityReader(cursor)
                        : null
        );
        loadPartners();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                mFragment.resetPartners();
                break;
            case R.id.loader_query_filters_partners_visibility:
                mFragment.resetPartnersVisibility();
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    private void loadPartners() {
        LoaderManager loaderManager = mFragment.getLoaderManager();
        loaderManager.restartLoader(
                R.id.loader_query_filters_partners,
                PartnerCursorLoaderHelper.getArgs(mCurrentSearch),
                FiltersPresenter.this
        );
    }

    private void loadPartnersVisibility() {
        LoaderManager loaderManager = mFragment.getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_filters_partners_visibility, null, FiltersPresenter.this);
    }

    @Override
    public void changePartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(
                partnerId,
                isVisible
        );
        mFragment.getLoaderManager().initLoader(R.id.loader_insert_partner_visibility, args, mBundleLoaderCallbacks);
    }

    @Override
    public void changePartnersVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        mFragment.getLoaderManager().initLoader(R.id.loader_insert_partner_visibility, args, mBundleLoaderCallbacks);
    }

    @Override
    public void filterPartner(@NonNull String search) {
        if (!mCurrentSearch.equals(search)) {
            mCurrentSearch = search;
            loadPartners();
        }
    }
}
