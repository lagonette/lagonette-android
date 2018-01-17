package org.lagonette.app.app.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.FiltersViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.adapter.FilterAdapter;
import org.lagonette.app.room.reader.FilterReader;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ID;

//TODO Do not show empty fragment when there is no partner.
public class FiltersFragment
        extends BaseFragment {

    public static final String TAG = "FiltersFragment";

    @NonNull
    public static FiltersFragment newInstance() {
        Bundle args = new Bundle(0);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MainLiveEventBusViewModel mEventBus;

    private FiltersViewModel mFiltersViewModel;

    private StateMapActivityViewModel mStateViewModel;

    private LiveData<FilterReader> mFilters;

    private MutableLiveData<String> mSearch;

    private View mFilterContainer;

    private RecyclerView mRecyclerView;

    private FilterAdapter mFilterAdapter;

    //TODO Fix category visibility

    @Override
    protected void construct() {

        mEventBus = ViewModelProviders
                .of(getActivity()) // TODO maybe activity is not created
                .get(MainLiveEventBusViewModel.class);

        mFiltersViewModel = ViewModelProviders
                .of(FiltersFragment.this)
                .get(FiltersViewModel.class);

        mStateViewModel = ViewModelProviders
                .of(getActivity())
                .get(StateMapActivityViewModel.class);

        mFilters = mFiltersViewModel.getFilters();
        mSearch = mStateViewModel.getSearch();

        mFilterAdapter = new FilterAdapter(getContext(), getResources());
        mFilterAdapter.setHasStableIds(true);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_filters;
    }

    @Override
    protected void inject(@NonNull View view) {
        mFilterContainer = view.findViewById(R.id.filter_container);
        mRecyclerView = view.findViewById(R.id.filter_list);
    }

    @Override
    protected void construct(@NonNull FragmentActivity activity) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFilterAdapter);
        mRecyclerView.setItemAnimator(null); //TODO Remove
    }

    @Override
    protected void init() {
        // Do nothing
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        // Do nothing
    }

    @Override
    protected void connect() {
        mFilters.observe(
                FiltersFragment.this,
                mFilterAdapter::setFilterReader //TODO manage loading & error
        );

        mSearch.observe(
                FiltersFragment.this,
                mFiltersViewModel.getSearch()::setValue //TODO Weird
        );

        mFilterAdapter.onLocationClick = locationId -> mEventBus.publish(OPEN_LOCATION_ID, locationId);
        mFilterAdapter.onLocationVisibilityClick = mFiltersViewModel::changeLocationVisibility;
        mFilterAdapter.onCategoryVisibilityClick = mFiltersViewModel::changeCategoryVisibility;
        mFilterAdapter.onCategoryCollapsedClick = mFiltersViewModel::changeCategoryCollapsed;
        mFilterAdapter.onLocationShortcutClick = mFiltersViewModel::showAllLocations;
        mFilterAdapter.onExchangeOfficeShortcutClick = mFiltersViewModel::showAllExchangeOffices;
        mFilterAdapter.onOfficeShortcutClick = () -> {/* YOLO */};
    }

    public void updateTopPadding(int top) {
        mFilterContainer.setPadding(0, top, 0, 0);
    }

}