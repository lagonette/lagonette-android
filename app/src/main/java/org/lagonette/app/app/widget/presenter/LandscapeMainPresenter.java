package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;

public class LandscapeMainPresenter
        extends MainPresenter<
        LandscapeFabButtonsPerformer,
        LandscapeMapFragmentPerformer,
        LandscapeSearchBarPerformer> {

    @Override
    public void construct(@NonNull AppCompatActivity activity) {
        super.construct(activity);

        mMapFragmentPerformer = new LandscapeMapFragmentPerformer(activity, R.id.content);
        mFabButtonsPerformer = new LandscapeFabButtonsPerformer(R.id.my_location_fab);
        mSearchBarPerformer = new LandscapeSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mBottomSheetPerformer = new LandscapeBottomSheetPerformer(activity.getResources(), R.id.bottom_sheet);

        mCoordinator = new LandscapeMainCoordinator(
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

        // Performer --> Performer
        mBottomSheetPerformer.onSlideChanged(mLocationDetailFragmentPerformer::updateTopPadding);
        mSearchBarPerformer.onBottomChanged(mFiltersFragmentPerformer::updateTopPadding);
    }
}
