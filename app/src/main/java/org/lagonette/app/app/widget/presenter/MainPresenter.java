package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.arch.LiveEvent;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentStateLiveData;
import org.lagonette.app.app.widget.livedata.MainActionLiveData;
import org.lagonette.app.app.widget.livedata.MainStateLiveData;
import org.lagonette.app.app.widget.livedata.MainStatefulActionLiveData;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.SearchBarPerformer;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.NOTIFY_MAP_MOVEMENT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.OPEN_LOCATION_ID;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.SHOW_FULL_MAP;

public abstract class MainPresenter<
        FBP extends FabButtonsPerformer,
        MFP extends MapFragmentPerformer,
        SBP extends SearchBarPerformer>
        implements ActivityPresenter {

    protected StateMapActivityViewModel mStateViewModel;

    protected MainLiveEventBusViewModel mEventBus;

    protected MainActionLiveData mAction;

    protected MainStateLiveData mState;

    protected MainStatefulActionLiveData mMainStatefulAction;

    protected BottomSheetFragmentStateLiveData mBottomSheetFragmentState;

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
    public void construct(@NonNull AppCompatActivity activity) {

        mStateViewModel = ViewModelProviders
                .of(activity)
                .get(StateMapActivityViewModel.class);

        mEventBus = ViewModelProviders
                .of(activity)
                .get(MainLiveEventBusViewModel.class);

        mMainStatefulAction = mStateViewModel.getMainStatefulActionLiveData();
        mAction = mStateViewModel.getMainActionLiveData();
        mState = mStateViewModel.getMainStateLiveData();
        mBottomSheetFragmentState = mStateViewModel.getBottomSheetFragmentState();
        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();

        mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity, R.id.fragment_filters);
        mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail);
    }

    @Override
    public void setContentView(@NonNull AppCompatActivity activity) {
        activity.setContentView(R.layout.activity_main);
    }

    @Override
    public void onViewCreated(@NonNull View view) {
        mMapFragmentPerformer.inject(view);
        mBottomSheetPerformer.inject(view);
        mSearchBarPerformer.inject(view);
        mFabButtonsPerformer.inject(view);
        mLocationDetailFragmentPerformer.inject(view);
        mFiltersFragmentPerformer.inject(view);
    }

    @Override
    @CallSuper
    public void init(@NonNull AppCompatActivity activity) {
        mCoordinator.init();
    }

    @Override
    @CallSuper
    public void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState) {
        mCoordinator.restore(mMainStatefulAction.getValue().state);
    }

    @Override
    @CallSuper
    public void onActivityCreated(@NonNull AppCompatActivity activity) {

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

        // Performer's state --> performers
        mLocationDetailFragmentPerformer.onFragmentLoaded(mBottomSheetFragmentState::notifyLocationDetailLoaded);
        mLocationDetailFragmentPerformer.onFragmentUnloaded(mBottomSheetFragmentState::notifyLocationDetailUnloaded);
        mBottomSheetPerformer.onStateChanged(mState::notifyBottomSheetStateChanged);

        // Event bus --> Performers
        mEventBus.subscribe(
                NOTIFY_MAP_MOVEMENT,
                activity,
                mState::notifyMapMovementChanged
        );

        // LiveData --> Performer, Coordinator
        mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);
        mMainStatefulAction.observe(activity, mCoordinator::process);
    }

    @CallSuper
    public boolean onBackPressed(@NonNull AppCompatActivity activity) {
        return false;
    }
}
