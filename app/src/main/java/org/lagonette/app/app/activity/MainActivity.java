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
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.coordinator.portrait.PortraitMainCoordinator;
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
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;

public class MainActivity
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

    private FiltersFragmentPerformer mFiltersFragmentPerformer;

    private LocationDetailFragmentPerformer mLocationDetailFragmentPerformer;

    private MapFragmentPerformer mMapFragmentPerformer;

    private SearchBarPerformer mSearchBarPerformer;

    @Override
    protected void construct() {
        final Resources resources = getResources();

        mStateViewModel = ViewModelProviders
                .of(MainActivity.this)
                .get(StateMapActivityViewModel.class);

        mMainStatefulAction = mStateViewModel.getMainStatefulActionLiveData();
        mAction = mStateViewModel.getMainActionLiveData();
        mState = mStateViewModel.getMainStateLiveData();
        mBottomSheetFragmentType = mStateViewModel.getBottomSheetFragmentType();
        mSearch = mStateViewModel.getSearch();
        mWorkStatus = mStateViewModel.getWorkStatus();

        mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(MainActivity.this, R.id.fragment_location_detail);

        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mFiltersFragmentPerformer = new LandscapeFiltersFragmentPerformer(MainActivity.this, R.id.fragment_filters);
            mBottomSheetPerformer = new LandscapeBottomSheetPerformer(resources, R.id.bottom_sheet, R.dimen.search_bar_supposed_height);
            mMapFragmentPerformer = new LandscapeMapFragmentPerformer(MainActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
            mFabButtonsPerformer = new LandscapeFabButtonsPerformer(R.id.my_location_fab);
            mSearchBarPerformer = new LandscapeSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);

            mCoordinator = new LandscapeMainCoordinator(
                    mAction::markDone,
                    mBottomSheetPerformer,
                    mMapFragmentPerformer
            );
        }
        else {
            mFiltersFragmentPerformer = new FiltersFragmentPerformer(MainActivity.this, R.id.fragment_filters);
            mBottomSheetPerformer = new PortraitBottomSheetPerformer(resources, R.id.bottom_sheet, R.dimen.search_bar_supposed_height);
            mMapFragmentPerformer = new PortraitMapFragmentPerformer(MainActivity.this, R.id.content, R.dimen.search_bar_supposed_height);
            mFabButtonsPerformer = new PortraitFabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
            mSearchBarPerformer = new PortraitSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);

            mCoordinator = new PortraitMainCoordinator(
                    mAction::markDone,
                    mBottomSheetPerformer,
                    mMapFragmentPerformer
            );
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        mMapFragmentPerformer.inject(view);
        mBottomSheetPerformer.inject(view);
        mSearchBarPerformer.inject(view);
        mFabButtonsPerformer.inject(view);
    }

    @Override
    protected void init() {
        mMapFragmentPerformer.init();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mFiltersFragmentPerformer.init();
        }
        else {
            mBottomSheetPerformer.init();
        }
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mMapFragmentPerformer.restore();
        mFiltersFragmentPerformer.restore();
        mLocationDetailFragmentPerformer.restore();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mFiltersFragmentPerformer.restore();
        }
        else {
            //TODO Unload filters fragment on conf' changed if needed (do not forget when 2 conf' changed occurred and the bottom sheet fragment does not changed
        }
    }

    @Override
    protected void onActivityCreated() {
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
        mWorkStatus.observe(MainActivity.this, mSearchBarPerformer::setWorkStatus);
        mMainStatefulAction.observe(MainActivity.this, mCoordinator::process);

        // Performer --> Performer
        mBottomSheetPerformer.setLocationDetailFragmentPerformer(mLocationDetailFragmentPerformer);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else {
            //TODO Check correctly initialisation and conf change.

            // Performer's action --> LiveData
            ((PortraitFabButtonsPerformer) mFabButtonsPerformer).onFiltersClick(mAction::openFilters); //TODO Do better

            // LiveData --> Performer, Coordinator
            mBottomSheetFragmentType.observe(
                    MainActivity.this,
                    ((PortraitSearchBarPerformer)mSearchBarPerformer)::notifyBottomSheetFragmentChanged
            );

            // Performer --> Performer
            mBottomSheetPerformer.setFiltersFragmentPerformer(mFiltersFragmentPerformer);
            ((PortraitSearchBarPerformer)mSearchBarPerformer).onOffsetChanged(
                    offset -> {
                        //TODO Do better
                        ((PortraitMapFragmentPerformer) mMapFragmentPerformer).notifySearchBarOffsetChanged(offset);
                        mBottomSheetPerformer.notifySearchBarOffsetChanged(offset);
                    }
            );
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
