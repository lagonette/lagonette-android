package com.zxcv.gonette.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.widget.adapter.FilterAdapter;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.loader.InsertPartnerVisibilityLoader;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public class FiltersFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, FilterAdapter.OnPartnerClickListener {

    public interface Callback {

        void showPartner(long partnerId, boolean zoom);

    }

    public static final String TAG = "FiltersFragment";

    private Callback mCallback;

    private RecyclerView mFilterList;

    private FilterAdapter mFilterAdapter;

    private LoaderManager.LoaderCallbacks<Bundle> mBundleLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bundle>() {
        @Override
        public Loader<Bundle> onCreateLoader(int id, Bundle args) {
            return new InsertPartnerVisibilityLoader(getContext(), args);
        }

        @Override
        public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<Bundle> loader) {
            // Do nothing
        }
    };

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFilterList = (RecyclerView) view.findViewById(R.id.filter_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFilterAdapter = new FilterAdapter(FiltersFragment.this);
        mFilterAdapter.setHasStableIds(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mFilterList.setLayoutManager(layoutManager);
        mFilterList.setAdapter(mFilterAdapter);

        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() + " must implement " + Callback.class);
        }

        queryPartnersVisibility();
        queryPartners();
    }

    @Override
    public void onPartnerClick(FilterAdapter.PartnerViewHolder holder) {
        mCallback.showPartner(holder.partnerId, true);
    }

    @Override
    public void onAllPartnerVisibilityClick(FilterAdapter.AllPartnerViewHolder holder) {
        insertAllPartnerVisibility(!holder.isVisible);
    }

    @Override
    public void onPartnerVisibilityClick(FilterAdapter.PartnerViewHolder holder) {
        insertPartnerVisibility(holder.partnerId, !holder.isVisible);
    }

    private void insertPartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(
                partnerId,
                isVisible
        );
        getLoaderManager().initLoader(R.id.loader_insert_partner_visibility, args, mBundleLoaderCallbacks);
    }

    private void insertAllPartnerVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        getLoaderManager().initLoader(R.id.loader_insert_partner_visibility, args, mBundleLoaderCallbacks);
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
        return new CursorLoader(
                getContext(),
                GonetteContract.Partner.METADATA_CONTENT_URI,
                new String[]{
                        GonetteContract.Partner.ID,
                        GonetteContract.Partner.NAME,
                        GonetteContract.PartnerMetadata.IS_VISIBLE
                },
                null,
                null,
                GonetteContract.Partner.NAME + " ASC"
        );
    }

    private Loader<Cursor> onCreateQueryPartnersVisibilityLoader(Bundle args) {
        return new CursorLoader(
                getContext(),
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
        mFilterAdapter.setPartnerReader(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    private void onQueryPartnersVisibilityLoadFinished(Cursor cursor) {
        mFilterAdapter.setPartnersVisibilityCursor(
                cursor != null
                        ? new PartnersVisibilityReader(cursor)
                        : null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_filters_partners:
                mFilterAdapter.setPartnerReader(null);
                break;
            case R.id.loader_query_filters_partners_visibility:
                mFilterAdapter.setPartnersVisibilityCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id:" + id);
        }
    }

    private void queryPartners() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_filters_partners, null, FiltersFragment.this);
    }

    private void queryPartnersVisibility() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_filters_partners_visibility, null, FiltersFragment.this);
    }

}