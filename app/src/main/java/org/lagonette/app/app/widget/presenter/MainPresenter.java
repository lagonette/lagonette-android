package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.viewmodel.MainActionViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;

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
        //TODO FragmentPerformer#init should be here ?
        mCoordinator.init();
    }

    @Override
    @CallSuper
    public void restore(@NonNull PresenterActivity activity, @NonNull Bundle savedInstanceState) {
        //TODO FragmentPerformer#restore should be here ?
        mCoordinator.restore();
    }

    @Override
    @CallSuper
    public void connect(@NonNull PresenterActivity activity) {

        // Event bus --> LiveData
        mEventBus.subscribe(
                OPEN_LOCATION_ITEM,
                activity,
                mAction::moveToAndOpenLocation
        );
        mEventBus.subscribe(
                MOVE_TO_CLUSTER,
                activity,
                mAction::moveToCluster
        );
        mEventBus.subscribe(
                SHOW_FULL_MAP,
                activity,
                mAction::showFullMap
        );
        mEventBus.subscribe(
                OPEN_LOCATION_ID,
                activity,
                mAction::moveToAndOpenLocation
        );

        // Performer's action --> LiveData
        mFabButtonsPerformer.onPositionClick(mAction::moveToMyLocation);
        mFabButtonsPerformer.onPositionLongClick(mAction::moveToFootprint);

        mSearchBarPerformer.onSearch(mSearch::setValue);

        // Performer's state --> LiveData
        mLocationDetailFragmentPerformer.onFragmentLoaded(locationId -> mAction.exec());
        mLocationDetailFragmentPerformer.onFragmentUnloaded(mAction::exec);
        mBottomSheetPerformer.onStateChanged(newState -> mAction.exec());
        mMapFragmentPerformer.onMapMovementChanged(mapMovement -> mAction.exec());

        // LiveData --> Performer, Coordinator
        mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);
        mAction.getLiveData().observe(activity, mCoordinator::process);
    }

    public boolean onBackPressed(@NonNull PresenterActivity activity) {
        //TODO Put this in coordinator

        if (mBottomSheetPerformer.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            return false;
        }

        mAction.back();
        return true;
    }

}
