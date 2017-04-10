package org.lagonette.android.app.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.FiltersContract;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.loader.InsertPartnerVisibilityLoader;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.content.reader.PartnersVisibilityReader;
import org.lagonette.android.util.SearchUtil;

public class FiltersPresenter
        extends BundleLoaderPresenter
        implements FiltersContract.Presenter,
        CursorLoaderCallbacks.Callbacks {

    private static final String ARG_SEARCH = "arg:search";

    private CursorLoaderCallbacks mCursorLoaderCallbacks;

    private boolean mStartUp = true;

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    private FiltersContract.View mView;

    @NonNull
    private String mCurrentSearch = SearchUtil.DEFAULT_SEARCH;

    public FiltersPresenter(@NonNull FiltersContract.View view) {
        mView = view;
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mView.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
        mCursorLoaderCallbacks = new CursorLoaderCallbacks(FiltersPresenter.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        loadPartnersVisibility();
    }

    @CallSuper
    @Override
    public Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_filters_partners:
                return onCreateQueryPartnersLoader(args);
            case R.id.loader_query_filters_partners_visibility:
                return onCreateQueryPartnersVisibilityLoader(args);
            default:
                return null;
        }
    }

    @CallSuper
    @Override
    public boolean onCursorLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                onQueryPartnersLoadFinished(cursor);
                return true;
            case R.id.loader_query_filters_partners_visibility:
                onQueryPartnersVisibilityLoadFinished(cursor);
                return true;
            default:
                return false;
        }
    }

    @CallSuper
    @Override
    public boolean onCursorLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                onPartnerReset();
                return true;
            case R.id.loader_query_filters_partners_visibility:
                onPartnersVisibilityReset();
                return true;
            default:
                return false;
        }
    }

    @CallSuper
    @Override
    public Loader<Bundle> onCreateBundleLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                return new InsertPartnerVisibilityLoader(mView.getContext(), args);
            default:
                return null;
        }
    }

    @CallSuper
    @Override
    public boolean onBundleLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                // Do nothing
                return true;
            default:
                return false;
        }
    }

    @CallSuper
    @Override
    public boolean onBundleLoaderReset(@NonNull Loader<Bundle> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_insert_partner_visibility:
                // Do nothing
                return true;
            default:
                return false;
        }
    }

    @Override
    public int[] getBundleLoaderIds() {
        return new int[]{
                R.id.loader_insert_partner_visibility
        };
    }

    private void onQueryPartnersVisibilityLoadFinished(Cursor cursor) {
        mView.displayPartnersVisibility(
                cursor != null
                        ? new PartnersVisibilityReader(cursor)
                        : null
        );

        if (mStartUp) {
            loadPartners();
        }
    }

    public void onPartnerReset() {
        mView.resetPartners();
    }

    public void onPartnersVisibilityReset() {
        mView.resetPartnersVisibility();
    }

    @NonNull
    @Override
    public LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
    }

    @Override
    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(
                partnerId,
                isVisible
        );
        mBundleLoaderCallbacks.initLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

    @Override
    public void setPartnersVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        mBundleLoaderCallbacks.initLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

    @Override
    public void filterPartners(@NonNull String search) {
        if (!mCurrentSearch.equals(search)) {
            mCurrentSearch = search;
            loadPartners(mCurrentSearch);
        }
    }

    @Override
    public void LoadFilters() {
        loadPartnersVisibility();
    }

    private Loader<Cursor> onCreateQueryPartnersLoader(Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoader(
                mView.getContext(),
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
                mView.getContext(),
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
        mView.displayPartners(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );

        mStartUp = false;
    }

    private void loadPartners() {
        mCursorLoaderCallbacks.initLoader(
                R.id.loader_query_filters_partners,
                null
        );
    }

    private void loadPartners(@NonNull String search) {
        mCursorLoaderCallbacks.restartLoader(
                R.id.loader_query_filters_partners,
                PartnerCursorLoaderHelper.getArgs(search)
        );
    }

    private void loadPartnersVisibility() {
        mCursorLoaderCallbacks.initLoader(
                R.id.loader_query_filters_partners_visibility,
                null
        );
    }

}
