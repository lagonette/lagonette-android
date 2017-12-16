package org.lagonette.app.app.widget.presenter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.portrait.PortraitMainCoordinator;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;

public class PortraitMainPresenter
        extends MainPresenter<PortraitMainCoordinator,
        PortraitBottomSheetPerformer,
        PortraitFabButtonsPerformer,
        FiltersFragmentPerformer,
        LocationDetailFragmentPerformer,
        PortraitMapFragmentPerformer,
        PortraitSearchBarPerformer> {

    @Override
    public void construct(@NonNull AppCompatActivity activity) {
        super.construct(activity);

        mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity, R.id.fragment_filters);
        mBottomSheetPerformer = new PortraitBottomSheetPerformer(activity.getResources(), R.id.bottom_sheet, R.dimen.search_bar_supposed_height);
        mMapFragmentPerformer = new PortraitMapFragmentPerformer(activity, R.id.content, R.dimen.search_bar_supposed_height);
        mFabButtonsPerformer = new PortraitFabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mSearchBarPerformer = new PortraitSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail);

        mCoordinator = new PortraitMainCoordinator(
                mAction::markDone,
                mBottomSheetPerformer,
                mMapFragmentPerformer
        );
    }

    @Override
    public void init(@NonNull AppCompatActivity activity) {
        super.init(activity);
        mBottomSheetPerformer.init();
    }

    @Override
    public void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState) {
        super.restore(activity, savedInstanceState);
        //TODO Unload filters fragment on conf' changed if needed (do not forget when 2 conf' changed occurred and the bottom sheet fragment does not changed
    }

    @Override
    public void onActivityCreated(@NonNull AppCompatActivity activity) {
        super.onActivityCreated(activity);

        //TODO Check correctly initialisation and conf change.

        // Performer's action --> LiveData
        mFabButtonsPerformer.onFiltersClick(mAction::openFilters);

        // LiveData --> Performer, Coordinator
        mBottomSheetFragmentType.observe(
                activity,
                mSearchBarPerformer::notifyBottomSheetFragmentChanged
        );

        // Performer --> Performer
        mBottomSheetPerformer.setFiltersFragmentPerformer(mFiltersFragmentPerformer);
        mSearchBarPerformer.onOffsetChanged(
                offset -> {
                    mMapFragmentPerformer.notifySearchBarOffsetChanged(offset);
                    mBottomSheetPerformer.notifySearchBarOffsetChanged(offset);
                }
        );
    }

    @Override
    public boolean onBackPressed(@NonNull AppCompatActivity activity) {

        //TODO Make getValue() @NonNull
        if (mCoordinator.back(mMainStatefulAction.getValue())) {
            return true;
        }

        return super.onBackPressed(activity);
    }

}
