package org.lagonette.app.app.widget.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;

public class LandscapeMainPresenter
        extends MainPresenter<LandscapeMainCoordinator,
        LandscapeBottomSheetPerformer,
        LandscapeFabButtonsPerformer,
        LandscapeFiltersFragmentPerformer,
        LocationDetailFragmentPerformer,
        LandscapeMapFragmentPerformer,
        LandscapeSearchBarPerformer> {

    @Override
    public void construct(@NonNull AppCompatActivity activity) {
        super.construct(activity);

        mFiltersFragmentPerformer = new LandscapeFiltersFragmentPerformer(activity, R.id.fragment_filters);
        mBottomSheetPerformer = new LandscapeBottomSheetPerformer(activity.getResources(), R.id.bottom_sheet, R.dimen.search_bar_supposed_height);
        mMapFragmentPerformer = new LandscapeMapFragmentPerformer(activity, R.id.content, R.dimen.search_bar_supposed_height);
        mFabButtonsPerformer = new LandscapeFabButtonsPerformer(R.id.my_location_fab);
        mSearchBarPerformer = new LandscapeSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail);

        mCoordinator = new LandscapeMainCoordinator(
                mAction::markDone,
                mBottomSheetPerformer,
                mMapFragmentPerformer
        );
    }

    @Override
    public void init(@NonNull AppCompatActivity activity) {
        super.init(activity);
        mFiltersFragmentPerformer.init();
    }

    @Override
    public void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState) {
        super.restore(activity, savedInstanceState);
        mFiltersFragmentPerformer.restore();
    }
}
