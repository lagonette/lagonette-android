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
import com.zxcv.gonette.app.presenter.base.FragmentPresenter;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.loader.InsertPartnerVisibilityLoader;
import com.zxcv.gonette.content.loader.PartnerCursorLoaderHelper;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;
import com.zxcv.gonette.util.SearchUtil;

public class FiltersPresenter
        extends FragmentPresenter
        implements FiltersContract.Presenter {

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
    protected Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_filters_partners:
                return onCreateQueryPartnersLoader(args);
            case R.id.loader_query_filters_partners_visibility:
                return onCreateQueryPartnersVisibilityLoader(args);
            default:
                return super.onCreateCursorLoader(id, args);
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
                super.onCursorLoadFinished(loader, cursor);
        }
    }

    @Override
    protected void onCursorLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                onPartnerReset();
                break;
            case R.id.loader_query_filters_partners_visibility:
                onPartnersVisibilityReset();
                break;
            default:
                super.onCursorLoaderReset(loader);
        }
    }

    @Override
    protected Loader<Bundle> onCreateBundleLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                return new InsertPartnerVisibilityLoader(mFragment.getContext(), args);
            default:
                return super.onCreateBundleLoader(id, args);
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
                super.onBundleLoadFinished(loader, data);
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
                super.onBundleLoaderReset(loader);
        }
    }

    private void onQueryPartnersVisibilityLoadFinished(Cursor cursor) {
        mFragment.displayPartnersVisibility(
                cursor != null
                        ? new PartnersVisibilityReader(cursor)
                        : null
        );

        //TODO maybe load only at start?
        loadPartners(mCurrentSearch);
    }

    public void onPartnerReset() {
        mFragment.resetPartners();
    }

    public void onPartnersVisibilityReset() {
        mFragment.resetPartnersVisibility();
    }

    @NonNull
    @Override
    public LoaderManager getLoaderManager() {
        return mFragment.getLoaderManager();
    }

    @Override
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

    @Override
    public void setPartnersVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        initBundleLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

    @Override
    public void filterPartner(@NonNull String search) {
        if (!mCurrentSearch.equals(search)) {
            mCurrentSearch = search;
            loadPartners(mCurrentSearch);
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

    private void onQueryPartnersLoadFinished(Cursor cursor) {
        mFragment.displayPartners(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    public void loadPartners(@NonNull String search) {
        //TODO Do not restart loader on conf change
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
}
