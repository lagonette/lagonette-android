package org.lagonette.android.app.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.text.TextUtils;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.FiltersContract;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.InsertPartnerVisibilityCallbacks;
import org.lagonette.android.content.loader.callbacks.LoadFilterCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.FilterReader;
import org.lagonette.android.util.SearchUtil;

public class FiltersPresenter
        extends BundleLoaderPresenter
        implements FiltersContract.Presenter,
        CursorLoaderCallbacks.Callbacks,
        InsertPartnerVisibilityCallbacks.Callbacks,
        LoadFilterCallbacks.Callbacks {

    private static final String ARG_SEARCH = "arg:search";

    private LoadFilterCallbacks mLoadFilterCallbacks;

    private InsertPartnerVisibilityCallbacks mInsertPartnerVisibilityCallbacks;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mView.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
        mLoadFilterCallbacks = new LoadFilterCallbacks(
                FiltersPresenter.this,
                R.id.loader_query_filters_partners
        );
        mInsertPartnerVisibilityCallbacks = new InsertPartnerVisibilityCallbacks(FiltersPresenter.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void reattachLoaders() {
        mInsertPartnerVisibilityCallbacks.reattachLoader();
    }

    @Override
    public Context getContext() {
        return mView.getContext();
    }

    @NonNull
    @Override
    public LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
    }

    @Override
    public void setPartnerVisibility(long partnerId, boolean visibility) {
        mInsertPartnerVisibilityCallbacks.insertPartnerVisibility(partnerId, visibility);
    }

    @Override
    public void filterPartners(@NonNull String search) {
        if (!mCurrentSearch.equals(search)) {
            mCurrentSearch = search;
            loadFilters(mCurrentSearch);
        }
    }

    @Override
    public void loadFilters() {
        mLoadFilterCallbacks.loadFilters();
    }

    private void loadFilters(@NonNull String search) {
        mLoadFilterCallbacks.loadFilters(
                PartnerCursorLoaderHelper.getArgs(search)
        );
    }

    @Override
    public void setFilterReaders(@Nullable FilterReader filterReader) {
        mView.displayFilters(filterReader);
    }

    @Override
    public CursorLoaderParams getFilterLoaderParams(@Nullable Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoaderParams(
                GonetteContract.Filter.CONTENT_URI,
                new String[]{
                        GonetteContract.Filter.ROW_TYPE,
                        GonetteContract.Filter.Category.ID,
                        GonetteContract.Filter.Category.LABEL,
                        GonetteContract.Filter.Partner.ID,
                        GonetteContract.Filter.Partner.NAME,
                        GonetteContract.Filter.PartnerMetadata.IS_VISIBLE
                }
        )
                .setSelection(!TextUtils.isEmpty(search)
                        ? "(" + GonetteContract.Filter.ROW_TYPE + " <> " + GonetteContract.Filter.VALUE_ROW_MAIN_PARTNER
                        + " OR " + GonetteContract.Partner.NAME + " LIKE ?)"
                        + " AND "
                        + "(" + GonetteContract.Filter.ROW_TYPE + " <> " + GonetteContract.Filter.VALUE_ROW_SIDE_PARTNER
                        + " OR " + GonetteContract.Partner.NAME + " LIKE ?)"
                        : null
                )
                .setSelectionArgs(!TextUtils.isEmpty(search)
                        ? new String[]{"%" + search + "%", "%" + search + "%"}
                        : null
                )
                .setSortOrder(null);
    }

}
