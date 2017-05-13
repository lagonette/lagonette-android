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
import org.lagonette.android.content.loader.callbacks.InsertPartnerCallbacks;
import org.lagonette.android.content.loader.callbacks.LoadPartnerCallbacks;
import org.lagonette.android.content.loader.callbacks.LoadPartnersVisibilityCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.content.reader.PartnersVisibilityReader;
import org.lagonette.android.util.SearchUtil;

public class FiltersPresenter
        extends BundleLoaderPresenter
        implements FiltersContract.Presenter,
        CursorLoaderCallbacks.Callbacks,
        InsertPartnerCallbacks.Callbacks,
        LoadPartnerCallbacks.Callbacks,
        LoadPartnersVisibilityCallbacks.Callbacks {

    private static final String ARG_SEARCH = "arg:search";

    private LoadPartnerCallbacks mLoadPartnerCallbacks;

    private LoadPartnersVisibilityCallbacks mLoadPartnersVisibilityCallbacks;

    private InsertPartnerCallbacks mInsertPartnerCallbacks;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mView.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
        mLoadPartnersVisibilityCallbacks = new LoadPartnersVisibilityCallbacks(
                FiltersPresenter.this,
                R.id.loader_query_filters_partners_visibility
        );
        mLoadPartnerCallbacks = new LoadPartnerCallbacks(
                FiltersPresenter.this,
                R.id.loader_query_filters_partners
        );
        mInsertPartnerCallbacks = new InsertPartnerCallbacks(FiltersPresenter.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void reattachLoaders() {
        mInsertPartnerCallbacks.reattachLoader();
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
        mInsertPartnerCallbacks.insertPartnerVisibility(partnerId, visibility);
    }

    @Override
    public void setPartnersVisibility(boolean visibility) {
        mInsertPartnerCallbacks.insertPartnersVisibility(visibility);
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

    private void loadPartners() {
        mLoadPartnerCallbacks.loadPartners();
    }

    private void loadPartners(@NonNull String search) {
        mLoadPartnerCallbacks.loadPartners(
                PartnerCursorLoaderHelper.getArgs(search)
        );
    }

    private void loadPartnersVisibility() {
        mLoadPartnersVisibilityCallbacks.loadPartnersVisibility();
    }

    @Override
    public void setPartnerReader(@Nullable PartnerReader reader) {
        mView.displayPartners(reader);
        mStartUp = false;
    }

    @Override
    public CursorLoaderParams getPartnerLoaderParams(@Nullable Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoaderParams(
                GonetteContract.Partner.EXTENDED_CONTENT_URI,
                new String[]{
                        GonetteContract.Partner.ID,
                        GonetteContract.Partner.NAME,
                        GonetteContract.PartnerMetadata.IS_VISIBLE
                }
        )
                .setSelection(!TextUtils.isEmpty(search)
                        ? GonetteContract.Partner.NAME + " LIKE ?"
                        : null
                )
                .setSelectionArgs(!TextUtils.isEmpty(search)
                        ? new String[]{"%" + search + "%"}
                        : null
                )
                .setSortOrder(GonetteContract.Partner.NAME + " ASC");

    }

    @Override
    public void setPartnersVisibilityReader(@Nullable PartnersVisibilityReader reader) {
        mView.displayPartnersVisibility(reader);

        if (mStartUp) {
            loadPartners();
        }
    }

    @Override
    public CursorLoaderParams getPartnersVisibilityLoaderParams(@Nullable Bundle args) {
        return new CursorLoaderParams(
                GonetteContract.Partner.EXTENDED_CONTENT_URI,
                new String[]{
                        PartnersVisibilityReader.getPartnerVisibilityCountProjection()
                }
        )
                .setSelection(GonetteContract.PartnerMetadata.IS_VISIBLE + " > ?")
                .setSelectionArgs(new String[]{
                        String.valueOf(0)
                });
    }
}
