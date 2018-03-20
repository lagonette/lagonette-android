package org.lagonette.app.app.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.viewmodel.FiltersViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.widget.adapter.FilterAdapter;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;

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

    // --- View Models --- //

    private MainLiveEventBusViewModel mEventBus;

    private FiltersViewModel mFiltersViewModel;

    private DataViewModel mDataViewModel;

    // --- LiveDatas --- //

    private LiveData<HeadquarterShortcut> mHeadquarterShortcut;

    private LiveData<PagedList<Filter>> mFilters;

    private MutableLiveData<String> mSearch;

    // --- Views --- //
    private View mFilterContainer;

    private RecyclerView mRecyclerView;

    // --- Widget --- //

    private FilterAdapter mFilterAdapter;

    //TODO Fix category visibility

    @Override
    protected void construct() {
        mFiltersViewModel = ViewModelProviders
                .of(FiltersFragment.this)
                .get(FiltersViewModel.class);

        mDataViewModel = ViewModelProviders
                .of(getActivity())
                .get(DataViewModel.class);

        mFilters = mFiltersViewModel.getFilters();
        mHeadquarterShortcut = mFiltersViewModel.getHeadquarterShortcut();
        mSearch = mDataViewModel.getSearch();

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

        mEventBus = ViewModelProviders
                .of(activity)
                .get(MainLiveEventBusViewModel.class);

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
    protected void onConstructed() {
        mFilters.observe(
                FiltersFragment.this,
                mFilterAdapter::setFilters //TODO manage loading & error
        );

        mHeadquarterShortcut.observe(
                FiltersFragment.this,
                mFilterAdapter::setHeadquarterShortcut
        );

        mSearch.observe(
                FiltersFragment.this,
                mFiltersViewModel.getSearch()::setValue //TODO Weird
        );

        mFilterAdapter.locationCallbacks.onClick = locationId -> mEventBus.publish(OPEN_LOCATION_ID, locationId);
        mFilterAdapter.locationCallbacks.onVisibilityClick = mFiltersViewModel::changeLocationVisibility;
        mFilterAdapter.categoryCallbacks.onVisibilityClick = mFiltersViewModel::changeCategoryVisibility;
        mFilterAdapter.categoryCallbacks.onCollapsedClick = mFiltersViewModel::changeCategoryCollapsed;
        mFilterAdapter.shortcutCallbacks.onLocationClick = mFiltersViewModel::showAllLocations;
        mFilterAdapter.shortcutCallbacks.onExchangeOfficeClick = mFiltersViewModel::showAllExchangeOffices;
        mFilterAdapter.shortcutCallbacks.onHeadquarterClick = (locationId) -> mEventBus.publish(OPEN_LOCATION_ID, locationId);
    }

    public void updateTopPadding(int top) {
        mFilterContainer.setPadding(0, top, 0, 0);
    }

}