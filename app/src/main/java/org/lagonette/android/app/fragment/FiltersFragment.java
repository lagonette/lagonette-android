package org.lagonette.android.app.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.android.R;
import org.lagonette.android.app.viewmodel.FiltersViewModel;
import org.lagonette.android.app.widget.adapter.FilterAdapter;
import org.lagonette.android.room.reader.FilterReader;

public class FiltersFragment
        extends LifecycleFragment
        implements FilterAdapter.OnFilterClickListener {

    public static final String TAG = "FiltersFragment";

    private static final String ARG_SEARCH = "arg:search";

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search); // TODO
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // TODO unused?
    public interface Callback {

        void showPartner(long partnerId, boolean zoom);

    }

    private FiltersViewModel mFiltersViewModel;

    private Callback mCallback;

    private RecyclerView mFilterList;

    private FilterAdapter mFilterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilterAdapter = new FilterAdapter(getContext(), getResources(), FiltersFragment.this);
        mFilterAdapter.setHasStableIds(true);

        mFiltersViewModel = ViewModelProviders
                .of(FiltersFragment.this)
                .get(FiltersViewModel.class);

        mFiltersViewModel.getFilters().observe(
                FiltersFragment.this,
                filterReader -> mFilterAdapter.setFilterReader(filterReader)
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFilterList = view.findViewById(R.id.filter_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mFilterList.setLayoutManager(layoutManager);
        mFilterList.setAdapter(mFilterAdapter);
        mFilterList.setItemAnimator(null); // TODO Remove

        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() + " must implement " + Callback.class);
        }
    }

    @Override
    public void onPartnerClick(@NonNull FilterAdapter.PartnerViewHolder holder) {
        mCallback.showPartner(holder.partnerId, true);
    }

    @Override
    public void onCategoryVisibilityClick(@NonNull FilterAdapter.CategoryViewHolder holder) {
        mFiltersViewModel.setCategoryVisibility(holder.categoryId, !holder.isVisible);
    }

    @Override
    public void onCategoryCollapsedClick(@NonNull FilterAdapter.CategoryViewHolder holder) {
        mFiltersViewModel.setCategoryCollapsed(holder.categoryId, !holder.isCollapsed);
    }

    @Override
    public void onPartnerVisibilityClick(@NonNull FilterAdapter.PartnerViewHolder holder) {
        mFiltersViewModel.setPartnerVisibility(holder.partnerId, !holder.isVisible);
    }

    public void LoadFilters() {
        // TODO
//        mPresenter.loadFilters();
    }

    public void filterPartner(@NonNull String search) {
        mFiltersViewModel.filterPartners(search);
    }

}