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

public abstract class MainPresenter<CO extends MainCoordinator,
        BSP extends BottomSheetPerformer,
        FBP extends FabButtonsPerformer,
        MFP extends MapFragmentPerformer,
        SBP extends SearchBarPerformer>
        implements ActivityPresenter {

    protected StateMapActivityViewModel mStateViewModel;

    protected MainActionLiveData mAction;

    protected MainStateLiveData mState;

    protected MainStatefulActionLiveData mMainStatefulAction;

    protected BottomSheetFragmentStateLiveData mBottomSheetFragmentState;

    protected MutableLiveData<String> mSearch;

    protected LiveData<Integer> mWorkStatus;

    protected CO mCoordinator;

    protected BSP mBottomSheetPerformer;

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
        // Performer's action --> LiveData
        mMapFragmentPerformer.onClusterClick(mAction::moveToCluster);
        mMapFragmentPerformer.onItemClick(mAction::moveToLocation);
        mMapFragmentPerformer.onMapClick(mAction::showFullMap);
        mFabButtonsPerformer.onPositionClick(mAction::moveToMyLocation);
        mFabButtonsPerformer.onPositionLongClick(mAction::moveToFootprint);

        mSearchBarPerformer.onSearch(mSearch::setValue);

        // Performer's state --> LiveData
        mLocationDetailFragmentPerformer.onFragmentLoaded(mBottomSheetFragmentState::notifyLocationDetailLoaded);
        mLocationDetailFragmentPerformer.onFragmentUnloaded(mBottomSheetFragmentState::notifyLocationDetailUnloaded);
        mBottomSheetPerformer.onStateChanged(mState::notifyBottomSheetStateChanged);
        mMapFragmentPerformer.onMovement(mState::notifyMapMovementChanged);

        // LiveData --> Performer, Coordinator
        mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);
        mMainStatefulAction.observe(activity, mCoordinator::process);
    }

    @CallSuper
    public boolean onBackPressed(@NonNull AppCompatActivity activity) {
        return false;
    }
}
