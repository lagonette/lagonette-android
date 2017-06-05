package org.lagonette.android.app.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.FiltersContract;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.UpdateCategoryMetadataCallbacks;
import org.lagonette.android.content.loader.callbacks.UpdatePartnerMetadataCallbacks;
import org.lagonette.android.content.loader.callbacks.LoadFilterCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.FilterReader;
import org.lagonette.android.database.statement.FilterStatement;
import org.lagonette.android.util.SearchUtil;

public class FiltersPresenter
        extends BundleLoaderPresenter
        implements FiltersContract.Presenter,
        CursorLoaderCallbacks.Callbacks,
        LoadFilterCallbacks.Callbacks,
        UpdatePartnerMetadataCallbacks.Callbacks,
        UpdateCategoryMetadataCallbacks.Callbacks {

    private static final String ARG_SEARCH = "arg:search";

    private LoadFilterCallbacks mLoadFilterCallbacks;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private UpdatePartnerMetadataCallbacks mUpdatePartnerMetadataCallbacks;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private UpdateCategoryMetadataCallbacks mUpdateCategoryMetadataCallbacks;

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
        mUpdatePartnerMetadataCallbacks = new UpdatePartnerMetadataCallbacks(
                FiltersPresenter.this
        );
        mUpdateCategoryMetadataCallbacks = new UpdateCategoryMetadataCallbacks(
                FiltersPresenter.this
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void reattachLoaders() {
        mUpdatePartnerMetadataCallbacks.reattachLoader();
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
    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        mUpdatePartnerMetadataCallbacks.updatePartnerVisibility(partnerId, isVisible);
    }

    @Override
    public void setCategoryVisibility(long categoryId, boolean isVisible) {
        mUpdateCategoryMetadataCallbacks.updateCategoryVisibility(categoryId, isVisible);
    }

    @Override
    public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
        mUpdateCategoryMetadataCallbacks.updateCategoryCollapsedState(categoryId, isCollapsed);
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
                PartnerCursorLoaderHelper.createArgs(search)
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
                LaGonetteContract.Filter.CONTENT_URI,
                mView.getFiltersColumns()
        )
                .setSelection(null)
                .setSelectionArgs(FilterStatement.getSelectionsArgs(search)) // TODO Do not call directly FilterStatement because of separation of concern
                .setSortOrder(null);
    }

}
