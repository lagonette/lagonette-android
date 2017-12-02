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
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.app.widget.livedata.MainActionLiveData;
import org.lagonette.app.app.widget.livedata.MainStateLiveData;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.SearchBarPerformer;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MainCoordinator mCoordinator;

    private StateMapActivityViewModel mStateViewModel;

    private LiveData<MainStatefulAction> mMainStatefulAction;

    private MainActionLiveData mAction;

    private MainStateLiveData mState;

    private BottomSheetFragmentTypeLiveData mBottomSheetFragmentType;

    private MutableLiveData<String> mSearch;

    private LiveData<Integer> mWorkStatus;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FabButtonsPerformer mFabButtonsPerformer;

    private BottomSheetFragmentPerformer mBottomSheetFragmentPerformer;

    private MapFragmentPerformer mMapFragmentPerformer;

    private SearchBarPerformer mSearchBarPerformer;

    @Override
    protected void construct() {

        mStateViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(StateMapActivityViewModel.class);

        mMainStatefulAction = mStateViewModel.getMainStatefulActionLiveData();
        mAction = mStateViewModel.getMainActionLiveData();
        mState = mStateViewModel.getMainStateLiveData();
        mBottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();

        mBottomSheetFragmentPerformer = new BottomSheetFragmentPerformer(MapsActivity.this, getResources(), R.dimen.search_bar_supposed_height);
        mMapFragmentPerformer = new MapFragmentPerformer(MapsActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
        mBottomSheetPerformer = new BottomSheetPerformer(MapsActivity.this, R.id.bottom_sheet);
        mFabButtonsPerformer = new FabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mSearchBarPerformer = new SearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);

        mCoordinator = new MainCoordinator(
                mAction::markDone,
                mBottomSheetPerformer,
                mBottomSheetFragmentPerformer,
                mMapFragmentPerformer
        );
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        mMapFragmentPerformer.inject(view);
        mBottomSheetPerformer.inject(view);
        mFabButtonsPerformer.inject(view);
        mSearchBarPerformer.inject(view);
    }

    @Override
    protected void init() {
        mMapFragmentPerformer.init();
        mBottomSheetPerformer.init();
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();
        //noinspection ConstantConditions
        mBottomSheetFragmentPerformer.restore(mBottomSheetFragmentType.getValue());
    }

    @Override
    protected void onActivityCreated() {
        //TODO Check correctly initialisation and conf change.

        // Performer's action --> LiveData
        mMapFragmentPerformer.observeClusterClick(mAction::moveToCluster);
        mMapFragmentPerformer.observeItemClick(mAction::moveToLocation);
        mMapFragmentPerformer.observeMapClick(mAction::showFullMap);
        mFabButtonsPerformer.observeFiltersClick(mAction::openFilters);
        mFabButtonsPerformer.observePositionClick(mAction::moveToMyLocation);
        mFabButtonsPerformer.observePositionLongClick(mAction::moveToFootprint);

        mSearchBarPerformer.observeSearch(mSearch::setValue);

        // Performer's state --> LiveData
        mBottomSheetPerformer.observeState(mState::notifyBottomSheetStateChanged);
        mMapFragmentPerformer.observeMovement(mState::notifyMapMovementChanged);
        mBottomSheetFragmentPerformer.observe(mBottomSheetFragmentType);

        // LiveData --> Performer, Coordinator
        mWorkStatus.observe(MapsActivity.this, mSearchBarPerformer::setWorkStatus);
        mMainStatefulAction.observe(MapsActivity.this, mCoordinator::process);

        // Performer --> Performer
        // TODO store and pass value through a LiveData, give it to fragment performer, let performer send value to fragment
        mBottomSheetPerformer.observeSlide(mBottomSheetFragmentPerformer::notifyBottomSheetSlide);
        mSearchBarPerformer.observeOffset(
                offset -> {
                    mMapFragmentPerformer.notifySearchBarOffsetChanged(offset);
                    mBottomSheetFragmentPerformer.notifySearchBarOffsetChanged(offset);
                }
        );



        mBottomSheetFragmentType.observe(MapsActivity.this, mSearchBarPerformer::notifyBottomSheetFragmentChanged);
    }

    @Override
    public void onBackPressed() {
        //TODO Make getValue() @NonNull
        if (!mCoordinator.back(mMainStatefulAction.getValue())) {
            super.onBackPressed();
        }
    }

}
