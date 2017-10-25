package org.lagonette.app.app.activity;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.app.app.viewmodel.StateMapsActivityViewModel;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentManager;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MainCoordinator mCoordinator;

    private SharedMapsActivityViewModel mViewModel;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FabButtonsPerformer mFabButtonsPerformer;

    private BottomSheetFragmentManager mBottomSheetFragmentManager;

    private StateMapsActivityViewModel mStateViewModel;

    private MapFragmentPerformer mMapFragmentPerformer;

    @Override
    protected void construct() {
        mBottomSheetFragmentManager = new BottomSheetFragmentManager();
        mMapFragmentPerformer = new MapFragmentPerformer();
        mBottomSheetPerformer = new BottomSheetPerformer(R.id.bottom_sheet);
        mFabButtonsPerformer = new FabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mCoordinator = new MainCoordinator(
                mBottomSheetPerformer,
                mBottomSheetFragmentManager,
                mMapFragmentPerformer
        );

        mMapFragmentPerformer.inject(MapsActivity.this);
        mBottomSheetFragmentManager.inject(MapsActivity.this);

        mViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(SharedMapsActivityViewModel.class);

        mStateViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(StateMapsActivityViewModel.class);
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
    }

    @Override
    protected void init() {
        mMapFragmentPerformer.init();

        MutableLiveData<Integer> bottomSheetFragment = mStateViewModel.getBottomSheetFragment();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        //noinspection ConstantConditions
        mBottomSheetPerformer.init(bottomSheetState.getValue());
        //noinspection ConstantConditions
        mBottomSheetFragmentManager.init(bottomSheetFragment.getValue());
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();

        MutableLiveData<Integer> bottomSheetFragment = mStateViewModel.getBottomSheetFragment();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        //noinspection ConstantConditions
        mBottomSheetPerformer.restore(bottomSheetState.getValue());
        //noinspection ConstantConditions
        mBottomSheetFragmentManager.restore(bottomSheetFragment.getValue());
    }

    @Override
    protected void onActivityCreated() {

        MutableLiveData<Integer> bottomSheetFragment = mStateViewModel.getBottomSheetFragment();
        MutableLiveData<Integer> bottomSheetState = mStateViewModel.getBottomSheetState();

        mBottomSheetFragmentManager.observe(bottomSheetFragment::setValue);
        mBottomSheetPerformer.observe(bottomSheetState::setValue);

        bottomSheetFragment.observe(MapsActivity.this, mCoordinator::notifyBottomSheetFragmentChanged);
        bottomSheetState.observe(MapsActivity.this, mCoordinator::notifyBottomSheetStateChanged);

        mMapFragmentPerformer.observeMovement(mCoordinator::notifyMapMovementChanged); //TODO Maybe save & restore camera movement into LiveData ?

        mMapFragmentPerformer.observeClusterClick(cluster -> mCoordinator.moveOnCluster(cluster)); //TODO Maybe save & restore click state into LiveData ?
        mFabButtonsPerformer.observeFiltersClick(mCoordinator::openFilters);
        mFabButtonsPerformer.observePositionClick(mCoordinator::moveOnMyLocation);
        mFabButtonsPerformer.observePositionLongClick(mCoordinator::moveOnFootprint);

//        mViewModel
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

//        mViewModel
//                .getMapIsReady()
//                .observe(
//                        MapsActivity.this,
//                        aVoid -> mCoordinator.onMapReady()
//                );

//        mViewModel
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

        mViewModel
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
//                search -> mViewModel.search(search),
//                mViewModel.getMapMovementSender(),
//                () -> SoftKeyboardUtil.hideSoftKeyboard(MapsActivity.this),
//                topPadding ->  mViewModel.getMapTopPadding().setValue(topPadding),
//                bottomPadding ->  mViewModel.getMapBottomPadding().setValue(bottomPadding),
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
