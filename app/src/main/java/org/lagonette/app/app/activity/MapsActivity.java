package org.lagonette.app.app.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.SearchBarPerformer;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MainCoordinator mCoordinator;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FabButtonsPerformer mFabButtonsPerformer;

    private BottomSheetFragmentPerformer mBottomSheetFragmentPerformer;

    private StateMapActivityViewModel mStateViewModel;

    private MapFragmentPerformer mMapFragmentPerformer;

    private SearchBarPerformer mSearchBarPerformer;

    @Override
    protected void construct() {
        mBottomSheetFragmentPerformer = new BottomSheetFragmentPerformer(getResources(), R.dimen.search_bar_supposed_height);
        mMapFragmentPerformer = new MapFragmentPerformer(MapsActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
        mBottomSheetPerformer = new BottomSheetPerformer(MapsActivity.this, R.id.bottom_sheet);
        mFabButtonsPerformer = new FabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mSearchBarPerformer = new SearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mCoordinator = new MainCoordinator(
                mBottomSheetPerformer,
                mBottomSheetFragmentPerformer,
                mMapFragmentPerformer
        );

        mStateViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(StateMapActivityViewModel.class);

        mBottomSheetFragmentPerformer.inject(MapsActivity.this); //TODO Pass activity through constructor and inject view if needed with inject method
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        //TODO Change activity lifecycle in BaseActivity
        mMapFragmentPerformer.inject(view);
        mBottomSheetPerformer.inject(view);
        mFabButtonsPerformer.inject(view);
        mSearchBarPerformer.inject(view);
    }

    @Override
    protected void init() {
        mMapFragmentPerformer.init();

        BottomSheetFragmentTypeLiveData bottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        //noinspection ConstantConditions
        mBottomSheetPerformer.init(bottomSheetState.getValue());
        //noinspection ConstantConditions
        mBottomSheetFragmentPerformer.init(bottomSheetFragmentType.getValue());
//        mSearchBarPerformer.init();
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();

        BottomSheetFragmentTypeLiveData bottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        //noinspection ConstantConditions
        mBottomSheetPerformer.restore(bottomSheetState.getValue());
        //noinspection ConstantConditions
        mBottomSheetFragmentPerformer.restore(bottomSheetFragmentType.getValue());
        //noinspection ConstantConditions
//        mSearchBarPerformer.restore(mMapViewModel.getMapPartners().getValue().status);
    }

    @Override
    protected void onActivityCreated() {
        //TODO Check correctly initialisation and conf change.
        //TODO Do coordinator state must be stored in LiveData ?

        BottomSheetFragmentTypeLiveData bottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();
        MutableLiveData<String> search = mStateViewModel.getSearch();
        LiveData<Integer> workStatus = mStateViewModel.getWorkStatus();

        // Coordinator -- Observe states
        mBottomSheetFragmentPerformer.observe(bottomSheetFragmentType);
        mBottomSheetPerformer.observeState(bottomSheetState::setValue);

        bottomSheetFragmentType.observe(MapsActivity.this, mCoordinator::notifyBottomSheetFragmentChanged);
        bottomSheetState.observe(MapsActivity.this, mCoordinator::notifyBottomSheetStateChanged);

        mMapFragmentPerformer.observeMovement(mCoordinator::notifyMapMovementChanged); //TODO Maybe save & restore camera movement into LiveData ?

        // Coordinator -- Observe actions
        mMapFragmentPerformer.observeClusterClick(mCoordinator::moveToCluster); //TODO Maybe save & restore click state into LiveData ?
        mMapFragmentPerformer.observeItemClick(mCoordinator::moveToLocation); //TODO Maybe save & restore click state into LiveData ?
        mMapFragmentPerformer.observeMapClick(mCoordinator::showFullMap);
        mFabButtonsPerformer.observeFiltersClick(mCoordinator::openFilters);
        mFabButtonsPerformer.observePositionClick(mCoordinator::moveToMyLocation);
        mFabButtonsPerformer.observePositionLongClick(mCoordinator::moveToFootprint);

        // Performers -- Interact
        workStatus.observe(MapsActivity.this, mSearchBarPerformer::setWorkStatus);

        mSearchBarPerformer.observeSearch(search::setValue);
        mSearchBarPerformer.observeOffset(
                offset -> {
                    mMapFragmentPerformer.notifySearchBarOffsetChanged(offset);
                    mBottomSheetFragmentPerformer.notifySearchBarOffsetChanged(offset);
                }
        );

        // TODO store and pass value through a LiveData, give it to fragment performer, let performer send value to fragment
        mBottomSheetPerformer.observeSlide(mBottomSheetFragmentPerformer::notifyBottomSheetSlide);

        bottomSheetFragmentType.observe(MapsActivity.this, mSearchBarPerformer::notifyBottomSheetFragmentChanged);
    }

    @Override
    public void onBackPressed() {
        if (!mCoordinator.back()) {
            super.onBackPressed();
        }
    }

}
