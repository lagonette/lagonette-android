package org.lagonette.app.app.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.app.widget.livedata.MainActionLiveData;
import org.lagonette.app.app.widget.livedata.MainStateLiveData;
import org.lagonette.app.app.widget.livedata.MainStatefulActionLiveData;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.MainMapFragmentPerformer;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MainCoordinator mCoordinator;

    private StateMapActivityViewModel mStateViewModel;

    private MainStatefulActionLiveData mMainStatefulAction;

    private MainActionLiveData mAction;

    private MainStateLiveData mState;

    private BottomSheetFragmentTypeLiveData mBottomSheetFragmentType;

    private MutableLiveData<String> mSearch;

    private LiveData<Integer> mWorkStatus;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FabButtonsPerformer mFabButtonsPerformer;

    private BottomSheetFragmentPerformer mBottomSheetFragmentPerformer;

    private LandscapeFiltersFragmentPerformer mLandscapeFiltersFragmentPerformer;

    private LandscapeBottomSheetFragmentPerformer mLandscapeBottomSheetFragmentPerformer;

    private MapFragmentPerformer mMapFragmentPerformer;

    private SearchBarPerformer mSearchBarPerformer;

    @Override
    protected void construct() {
        final Resources resources = getResources();

        mStateViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(StateMapActivityViewModel.class);

        mMainStatefulAction = mStateViewModel.getMainStatefulActionLiveData();
        mAction = mStateViewModel.getMainActionLiveData();
        mState = mStateViewModel.getMainStateLiveData();
        mBottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();

        mBottomSheetPerformer = new BottomSheetPerformer(R.id.bottom_sheet);

        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMapFragmentPerformer = new LandscapeMapFragmentPerformer(MapsActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
            mLandscapeFiltersFragmentPerformer = new LandscapeFiltersFragmentPerformer(MapsActivity.this, R.id.fragment_filters);
            mLandscapeBottomSheetFragmentPerformer = new LandscapeBottomSheetFragmentPerformer(
                    MapsActivity.this,
                    resources,
                    R.id.fragment_location_detail,
                    R.dimen.search_bar_supposed_height
            );
        }
        else {
            mMapFragmentPerformer = new MainMapFragmentPerformer(MapsActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
            mBottomSheetFragmentPerformer = new BottomSheetFragmentPerformer(
                    MapsActivity.this,
                    resources,
                    R.id.fragment_filters,
                    R.id.fragment_location_detail,
                    R.dimen.search_bar_supposed_height
            );
            mFabButtonsPerformer = new FabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
            mSearchBarPerformer = new SearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);

            mCoordinator = new MainCoordinator(
                    mAction::markDone,
                    mBottomSheetPerformer,
                    mBottomSheetFragmentPerformer,
                    mMapFragmentPerformer
            );
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        mMapFragmentPerformer.inject(view);
        mBottomSheetPerformer.inject(view);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else {
            mFabButtonsPerformer.inject(view);
            mSearchBarPerformer.inject(view);
        }
    }

    @Override
    protected void init() {
        mMapFragmentPerformer.init();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLandscapeFiltersFragmentPerformer.init();
        }
        else {
            mBottomSheetPerformer.init();
        }
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLandscapeFiltersFragmentPerformer.restore();
            mLandscapeBottomSheetFragmentPerformer.restore(mBottomSheetFragmentType.getValue());
        }
        else {
            //noinspection ConstantConditions
            mBottomSheetFragmentPerformer.restore(mBottomSheetFragmentType.getValue());
        }
    }

    @Override
    protected void onActivityCreated() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else {
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
                        //TODO Do better
                        ((MainMapFragmentPerformer) mMapFragmentPerformer).notifySearchBarOffsetChanged(offset);
                        mBottomSheetFragmentPerformer.notifySearchBarOffsetChanged(offset);
                    }
            );

            mBottomSheetFragmentType.observe(MapsActivity.this, mSearchBarPerformer::notifyBottomSheetFragmentChanged);
        }
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else {
            //TODO Make getValue() @NonNull
            if (!mCoordinator.back(mMainStatefulAction.getValue())) {
                super.onBackPressed();
            }
        }
    }

}
