package org.lagonette.android.app.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.FiltersContract;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.util.DB;
import org.lagonette.android.util.SearchUtil;

public class FiltersPresenter
        extends BundleLoaderPresenter<FiltersContract.View>
        implements FiltersContract.Presenter,
        BaseLoaderCallbacks.Callbacks {

    private static final String TAG = "FiltersPresenter";

    private static final String ARG_SEARCH = "arg:search";

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    private String mCurrentSearch = SearchUtil.DEFAULT_SEARCH;

    public FiltersPresenter(@NonNull FiltersContract.View view) {
        super(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mView.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void reattachLoaders() {
        // do nothing
    }

    @Override
    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        DB.get(getContext())
                .partnerDao()
                .updatePartnerVisibility(partnerId, isVisible);
    }

    @Override
    public void setCategoryVisibility(long categoryId, boolean isVisible) {
        DB.get(getContext())
                .categoryDao()
                .updateCategoryVisibility(categoryId, isVisible);
    }

    @Override
    public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
        DB.get(getContext())
                .categoryDao()
                .updateCategoryCollapsed(categoryId, isCollapsed);
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
        LaGonetteDatabase database = DB.get(mView.getContext());
        Cursor cursor = database.mainDao().getFilters("%");
        FilterReader reader = FilterReader.create(cursor);
        mView.displayFilters(reader);
    }

    private void loadFilters(@NonNull String search) {
        LaGonetteDatabase database = DB.get(mView.getContext());
        Cursor cursor = database.mainDao().getFilters("%" + search + "%");
        FilterReader reader = FilterReader.create(cursor);
        mView.displayFilters(reader);
    }

}
