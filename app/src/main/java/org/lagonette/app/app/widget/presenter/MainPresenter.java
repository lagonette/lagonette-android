package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.viewmodel.MainActionViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.base.MainStateModel;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.arch.LongObserver;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ID;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.SHOW_FULL_MAP;

public abstract class MainPresenter<
        FBP extends FabButtonsPerformer,
        MFP extends MapFragmentPerformer,
        SBP extends SearchBarPerformer>
        implements PresenterActivity.Lifecycle {

    protected StateMapActivityViewModel mStateViewModel;

    protected MainLiveEventBusViewModel mEventBus;

    protected MainActionViewModel mAction;

    protected MainStateModel mMainState;

    protected MutableLiveData<String> mSearch;

    protected LiveData<Integer> mWorkStatus;

    protected MainCoordinator mCoordinator;

    protected BottomSheetPerformer mBottomSheetPerformer;

    protected FBP mFabButtonsPerformer;

    protected MFP mMapFragmentPerformer;

    protected SBP mSearchBarPerformer;

    protected FiltersFragmentPerformer mFiltersFragmentPerformer;

    protected LocationDetailFragmentPerformer mLocationDetailFragmentPerformer;

    @Override
    @CallSuper
    public void construct(@NonNull PresenterActivity activity) {

        mStateViewModel = ViewModelProviders
                .of(activity)
                .get(StateMapActivityViewModel.class);

        mEventBus = ViewModelProviders
                .of(activity)
                .get(MainLiveEventBusViewModel.class);

        mAction = ViewModelProviders
                .of(activity)
                .get(MainActionViewModel.class);

        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();

        mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity, R.id.fragment_filters);
        mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail);
        mMainState = new MainStateModel();
    }

    @Override
    @LayoutRes
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void inject(@NonNull View view) {
        mBottomSheetPerformer.inject(view);
        mSearchBarPerformer.inject(view);
        mFabButtonsPerformer.inject(view);
    }

    @Override
    @CallSuper
    public void init(@NonNull PresenterActivity activity) {
        mMapFragmentPerformer.loadFragment();
        mBottomSheetPerformer.closeBottomSheet();
    }

    @Override
    @CallSuper
    public void restore(@NonNull PresenterActivity activity, @NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restoreFragment();
        mFiltersFragmentPerformer.restoreFragment();
        mLocationDetailFragmentPerformer.restoreFragment();

        mAction.start(MainAction.restore(mAction.getLiveData().getValue()));
    }

    @Override
    @CallSuper
    public void connect(@NonNull PresenterActivity activity) {

        // ======================== //
        //   State initialisation   //
        // ======================== //

        mMainState.initState = this::initState;

        // ========================================== //
        //                                            //
        //                    X                       //
        //                    ^                       //
        //   ⋄ ---------> Coordinator ----------- ⋄   //
        //   |                |                   |   //
        //   |                |                   |   //
        //   State            |                   |   //
        //   ^                |                   |   //
        //   |                v                   v   //
        //   Model <--------- ⋄ <-------- Performer   //
        //                                |       ^   //
        //                                v       |   //
        //                       User --> View -- ⋄   //
        //                                            //
        // ========================================== //

        // Coordinator > Performer
        mCoordinator.finishAction = mAction::finish;

        mCoordinator.openBottomSheet = mBottomSheetPerformer::openBottomSheet;
        mCoordinator.closeBottomSheet = mBottomSheetPerformer::closeBottomSheet;

        mCoordinator.moveMapToCluster = mMapFragmentPerformer::moveToCluster;
        mCoordinator.moveMapToLocation = mMapFragmentPerformer::moveToLocation;
        mCoordinator.moveMapToMyLocation = mMapFragmentPerformer::moveToMyLocation;
        mCoordinator.stopMapMoving = mMapFragmentPerformer::stopMoving;
        mCoordinator.moveMapToFootprint = mMapFragmentPerformer::moveToFootprint;
        mCoordinator.openLocation = mMapFragmentPerformer::openLocation;

        mCoordinator.loadFilters = mFiltersFragmentPerformer::loadFragment;
        mCoordinator.unloadFilters = mFiltersFragmentPerformer::unloadFragment;

        mCoordinator.loadLocationDetail = mLocationDetailFragmentPerformer::loadFragment;
        mCoordinator.unloadLocationDetail = mLocationDetailFragmentPerformer::unloadFragment;

        // Event bus > action
        mEventBus.subscribe(
                OPEN_LOCATION_ITEM,
                activity,
                locationItem -> mAction.start(MainAction.moveToAndOpenLocation(locationItem))
        );
        mEventBus.subscribe(
                MOVE_TO_CLUSTER,
                activity,
                cluster -> mAction.start(MainAction.moveToCluster(cluster))
        );
        mEventBus.subscribe(
                SHOW_FULL_MAP,
                activity,
                aVoid -> mAction.start(MainAction.showFullMap())
        );
        mEventBus.subscribe(
                OPEN_LOCATION_ID,
                activity,
                LongObserver.unbox(
                        Statement.NO_ID,
                        locationId -> mAction.start(MainAction.moveToAndOpenLocation(locationId))
                )
        );

        // Performer > action
        mFabButtonsPerformer.onPositionClick = () -> mAction.start(MainAction.moveToMyLocation());
        mFabButtonsPerformer.onPositionLongClick = () -> mAction.start(MainAction.moveToFootprint());

        // Performer > model
        mLocationDetailFragmentPerformer.onFragmentLoaded(mMainState::notifyLoadedLocationId);
        mLocationDetailFragmentPerformer.onFragmentUnloaded(() -> mMainState.notifyLocationDetailLoading(false));
        mBottomSheetPerformer.onStateChanged = mMainState::notifyBottomSheetState;
        mMapFragmentPerformer.onMapMovementChanged = mMainState::notifyMapMovement;

        // Action > State
        mAction.getLiveData().observe(activity, mMainState::notifyAction);

        // State > Coordinator
        mMainState.onStateChanged = mCoordinator::process;

        // ------------------------------- //
        //                                 //
        //    User > ViewModels > View     //
        //                                 //
        // ------------------------------- //

        // User > ViewModels
        mSearchBarPerformer.onSearch = mSearch::setValue;

        // ViewModels > View
        mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);

    }

    public boolean onBackPressed(@NonNull PresenterActivity activity) {

        if (mMainState.getState().bottomSheetState == BottomSheetBehavior.STATE_HIDDEN) {
            return false;
        }

        mAction.start(MainAction.back());
        return true;
    }

    @NonNull
    private MainState initState() {
        return new MainState(
                mAction.getLiveData().getValue(),
                mMapFragmentPerformer.getMapMovement(),
                mBottomSheetPerformer.getState(),
                mFiltersFragmentPerformer.isLoaded(),
                mLocationDetailFragmentPerformer.isLoaded(),
                mLocationDetailFragmentPerformer.getLoadedId()
        );
    }
}
