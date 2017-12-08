package org.lagonette.app.app.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.FiltersViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.adapter.FilterAdapter;

//TODO Do not show empty fragment when there is no partner.
public class FiltersFragment
        extends SlideableFragment {

    public static final String TAG = "FiltersFragment";

    public static FiltersFragment newInstance() {
        Bundle args = new Bundle(0);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FiltersViewModel mFiltersViewModel;

    private StateMapActivityViewModel mStateViewModel;

    private View mFilterContainer;

    private RecyclerView mFilterList;

    private FilterAdapter mFilterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFiltersViewModel = ViewModelProviders
                .of(FiltersFragment.this)
                .get(FiltersViewModel.class);

        mFiltersViewModel
                .getFilters()
                .observe(
                        FiltersFragment.this,
                        filtersResource -> mFilterAdapter.setFilterReader(filtersResource.data) //TODO manage loading & error
                );

        mStateViewModel = ViewModelProviders
                .of(getActivity())
                .get(StateMapActivityViewModel.class);

        mStateViewModel
                .getSearch()
                .observe(
                        FiltersFragment.this,
                        mFiltersViewModel.getSearch()::setValue
                );

        mFilterAdapter = new FilterAdapter(getContext(), getResources());
        mFilterAdapter.setHasStableIds(true);
        mFilterAdapter.setOnLocationVisibilityClickListener(
                (locationId, visibility) -> mFiltersViewModel.setLocationVisibility(locationId, visibility)
        );
        mFilterAdapter.setOnCategoryVisibilityClickListener(
                (categoryKey, visibility) -> mFiltersViewModel.setCategoryVisibility(categoryKey, visibility)
        );
        mFilterAdapter.setOnCategoryCollapsedClickListener(
                (categoryKey, isCollapsed) -> mFiltersViewModel.setCategoryCollapsed(categoryKey, isCollapsed)
        );
        mFilterAdapter.setOnLocationShortcutClickListener(
                () -> mFiltersViewModel.showAllLocations()
        );
        mFilterAdapter.setOnExchangeOfficeShortcutClickListener(
                () -> mFiltersViewModel.showAllExchangeOffices()
        );
        mFilterAdapter.setOnOfficeShortcutClickListener(
                () -> {/* YOLO */}
        );
    }

    //TODO Fix category visibility

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFilterContainer = view.findViewById(R.id.filter_container);
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
        mFilterList.setItemAnimator(null); //TODO Remove
    }

    @Override
    public void updateTopPadding(int top) {
        mFilterContainer.setPadding(0, top, 0, 0);
    }

}