package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.portrait.PortraitMainCoordinator;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;

public class PortraitMainPresenter
        extends MainPresenter<
        PortraitFabButtonsPerformer,
        PortraitMapFragmentPerformer,
        PortraitSearchBarPerformer> {

    @Override
    public void construct(@NonNull AppCompatActivity activity) {
        super.construct(activity);

        mMapFragmentPerformer = new PortraitMapFragmentPerformer(activity, R.id.content);
        mFabButtonsPerformer = new PortraitFabButtonsPerformer(R.id.my_location_fab, R.id.filters_fab);
        mSearchBarPerformer = new PortraitSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mBottomSheetPerformer = new PortraitBottomSheetPerformer(
                activity.getResources(),
                R.id.bottom_sheet
        );

        mCoordinator = new PortraitMainCoordinator(
                mAction,
                mBottomSheetPerformer,
                mFiltersFragmentPerformer,
                mLocationDetailFragmentPerformer,
                mMapFragmentPerformer
        );
    }

    @Override
    public void onActivityCreated(@NonNull AppCompatActivity activity) {
        super.onActivityCreated(activity);

        // Performer's state --> LiveData
        mFiltersFragmentPerformer.onFragmentLoaded(mAction::exec);
        mFiltersFragmentPerformer.onFragmentUnloaded(mAction::exec);

        // Performer's action --> LiveData
        mFabButtonsPerformer.onFiltersClick(mAction::openFilters);

        // LiveData --> Performer, Coordinator
        mLocationDetailFragmentPerformer.onFragmentLoaded(locationId -> mSearchBarPerformer.enableBehavior(true));
        mFiltersFragmentPerformer.onFragmentLoaded(() -> mSearchBarPerformer.enableBehavior(false));

        // Performer --> Performer
        mBottomSheetPerformer.onSlideChanged(
                topPadding -> {
                    mFiltersFragmentPerformer.updateTopPadding(topPadding);
                    mLocationDetailFragmentPerformer.updateTopPadding(topPadding);
                }
        );
        mSearchBarPerformer.onBottomChanged(
                offset -> {
                    mMapFragmentPerformer.notifySearchBarBottomChanged(offset);
                    mBottomSheetPerformer.notifySearchBarBottomChanged(offset);
                }
        );
    }

}
