package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
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
        FFP extends FiltersFragmentPerformer,
        LDFP extends LocationDetailFragmentPerformer,
        MFP extends MapFragmentPerformer,
        SBP extends SearchBarPerformer>
        implements ActivityPresenter {

    protected StateMapActivityViewModel mStateViewModel;

    protected MainActionLiveData mAction;

    protected MainStateLiveData mState;

    protected MainStatefulActionLiveData mMainStatefulAction;

    protected BottomSheetFragmentTypeLiveData mBottomSheetFragmentType;

    protected MutableLiveData<String> mSearch;

    protected LiveData<Integer> mWorkStatus;

    protected CO mCoordinator;

    protected BSP mBottomSheetPerformer;

    protected FBP mFabButtonsPerformer;

    protected FFP mFiltersFragmentPerformer;

    protected LDFP mLocationDetailFragmentPerformer;

    protected MFP mMapFragmentPerformer;

    protected SBP mSearchBarPerformer;

    @Override
    @CallSuper
    public void construct(@NonNull AppCompatActivity activity) {

        mStateViewModel = ViewModelProviders
                .of(activity)
                .get(StateMapActivityViewModel.class);

        mMainStatefulAction = mStateViewModel.getMainStatefulActionLiveData();
        mAction = mStateViewModel.getMainActionLiveData();
        mState = mStateViewModel.getMainStateLiveData();
        mBottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();
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
        mMapFragmentPerformer.init();
    }

    @Override
    @CallSuper
    public void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();
        mFiltersFragmentPerformer.restore();
        mLocationDetailFragmentPerformer.restore();
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
        mBottomSheetPerformer.onFragmentLoaded(mBottomSheetFragmentType);
        mBottomSheetPerformer.onStateChanged(mState::notifyBottomSheetStateChanged);
        mMapFragmentPerformer.onMovement(mState::notifyMapMovementChanged);

        // LiveData --> Performer, Coordinator
        mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);
        mMainStatefulAction.observe(activity, mCoordinator::process);

        // Performer --> Performer
        mBottomSheetPerformer.setLocationDetailFragmentPerformer(mLocationDetailFragmentPerformer);
    }

    @CallSuper
    public boolean onBackPressed(@NonNull AppCompatActivity activity) {
        return false;
    }
}
