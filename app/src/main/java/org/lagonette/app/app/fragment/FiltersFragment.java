package org.lagonette.app.app.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.FiltersViewModel;
import org.lagonette.app.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.app.app.widget.adapter.FilterAdapter;

// TODO Do not show empty fragment when there is no partner.
public class FiltersFragment
        extends LifecycleFragment {

    public static final String TAG = "FiltersFragment";

    private static final String ARG_SEARCH = "arg:search";

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search); // TODO
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FiltersViewModel mFiltersViewModel;

    private SharedMapsActivityViewModel mActivityViewModel;

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
                        filtersResource -> mFilterAdapter.setFilterReader(filtersResource.data) // TODO manage loading & error
                );

        mActivityViewModel = ViewModelProviders
                .of(getActivity())
                .get(SharedMapsActivityViewModel.class);

        mActivityViewModel
                .getSearch()
                .observe(
                        FiltersFragment.this,
                        search -> mFiltersViewModel.getSearch().send(search)
                );

        mFilterAdapter = new FilterAdapter(getContext(), getResources());
        mFilterAdapter.setHasStableIds(true);
        mFilterAdapter.setOnPartnerClickListener(
                partnerId -> mActivityViewModel.showPartner(partnerId, true)
        );
        mFilterAdapter.setOnPartnerVisibilityClickListener(
                (partnerId, visibility) -> mFiltersViewModel.setPartnerVisibility(partnerId, visibility)
        );
        mFilterAdapter.setOnCategoryVisibilityClickListener(
                (categoryId, visibility) -> mFiltersViewModel.setCategoryVisibility(categoryId, visibility)
        );
        mFilterAdapter.setOnCategoryCollapsedClickListener(
                (categoryId, isCollapsed) -> mFiltersViewModel.setCategoryCollapsed(categoryId, isCollapsed)
        );
        mFilterAdapter.setOnPartnerShortcutClickListener(
                () -> mFiltersViewModel.showAllPartners()
        );
        mFilterAdapter.setOnExchangeOfficeShortcutClickListener(
                () -> mFiltersViewModel.showAllExchangeOffices()
        );
        mFilterAdapter.setOnOfficeShortcutClickListener(
                () -> {/* YOLO */}
        );
    }

    // TODO Fix category visibility

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
    }

}