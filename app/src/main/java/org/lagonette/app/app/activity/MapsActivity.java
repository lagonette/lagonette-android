package org.lagonette.app.app.activity;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.SearchBarPerformer;
import org.lagonette.app.repo.Resource;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MainCoordinator mCoordinator;

    private SharedMapsActivityViewModel mSharedViewModel;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FabButtonsPerformer mFabButtonsPerformer;

    private BottomSheetFragmentPerformer mBottomSheetFragmentPerformer;

    private StateMapActivityViewModel mStateViewModel;

    private MapViewModel mMapViewModel;

    private MapFragmentPerformer mMapFragmentPerformer;

    private SearchBarPerformer mSearchBarPerformer;

    @Override
    protected void construct() {
        mBottomSheetFragmentPerformer = new BottomSheetFragmentPerformer();
        mMapFragmentPerformer = new MapFragmentPerformer();
        mBottomSheetPerformer = new BottomSheetPerformer(R.id.bottom_sheet);
        mFabButtonsPerformer = new FabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mSearchBarPerformer = new SearchBarPerformer(R.id.search_bar, R.id.progress_bar);
        mCoordinator = new MainCoordinator(
                mBottomSheetPerformer,
                mBottomSheetFragmentPerformer,
                mMapFragmentPerformer
        );

        mMapFragmentPerformer.inject(MapsActivity.this);
        mBottomSheetFragmentPerformer.inject(MapsActivity.this);

        mMapViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(MapViewModel.class);

        mSharedViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(SharedMapsActivityViewModel.class);

        mStateViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(StateMapActivityViewModel.class);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        //TODO Change activity lifecycle in BaseActivity
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

        BottomSheetFragmentTypeLiveData bottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        // Coordinator -- Observe states
        mBottomSheetFragmentPerformer.observe(bottomSheetFragmentType);
        mBottomSheetPerformer.observe(bottomSheetState::setValue);

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

        // Performers -- Interactions
        mMapViewModel.getMapPartners()
                .observe(
                        MapsActivity.this,
                        resource -> mSearchBarPerformer.setWorkState(
                                resource != null
                                        ? resource.status
                                        : Resource.SUCCESS
                        )
                );

//        mSharedViewModel
//                .getWorkInProgress()
//                .observe(
//                        MapsActivity.this,
//                        workInProgress -> {
//                            if (workInProgress != null && workInProgress) {
//                                mCoordinator.showProgressBar();
//                            } else {
//                                mCoordinator.hideProgressBar();
//                            }
//                        }
//                );

//        mSharedViewModel
//                .getMapIsReady()
//                .observe(
//                        MapsActivity.this,
//                        aVoid -> mCoordinator.onMapReady()
//                );

//        mSharedViewModel
//                .getEnableMyPositionFAB()
//                .observe(
//                        MapsActivity.this,
//                        enable -> {
//                            if (enable != null && enable) {
//                                mCoordinator.showMyLocationButton();
//                            } else {
//                                mCoordinator.hideMyLocationButton();
//                            }
//                        }
//                );

        mSharedViewModel
                .getShowLocationRequest()
                .observe(
                        MapsActivity.this,
                        request -> {
                            if (request != null) {
                                showLocation(
                                        request.locationId,
                                        request.zoom
                                );
                            } else {
                                showFullMap();
                            }
                        }
                );

        //TODO Maybe make interface to send data xor just observe data, like LiveEvent but for LiveData
        //TODO Maybe use LiveData to exchange info between Coordinator & Activity?
//        mCoordinator = new OldMainCoordinator(
//                MapsActivity.this,
//                search -> mSharedViewModel.search(search),
//                mSharedViewModel.getMapMovementSender(),
//                () -> SoftKeyboardUtil.hideSoftKeyboard(MapsActivity.this),
//                topPadding ->  mSharedViewModel.getMapTopPadding().setValue(topPadding),
//                bottomPadding ->  mSharedViewModel.getMapBottomPadding().setValue(bottomPadding),
//                MapsActivity.this
//        );
//        mCoordinator.inject(findViewById(android.R.id.content));
//        mCoordinator.start(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mCoordinator.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!mCoordinator.back()) {
            super.onBackPressed();
        }
    }

    public void showFullMap() {
//        mCoordinator.focusOnMap();
//        mCoordinator.closeBottomSheet();
    }

    public void showLocation(long locationId, boolean zoom) {
//        mCoordinator.focusOnMap();
//        mCoordinator.showLocation(locationId, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mCoordinator.onSaveInstanceState(outState);
    }

}
