package org.lagonette.app.app.widget.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
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
        LandscapeMapFragmentPerformer,
        LandscapeSearchBarPerformer> {

    protected FiltersFragmentPerformer mFiltersFragmentPerformer;

    @Override
    public void construct(@NonNull AppCompatActivity activity) {
        super.construct(activity);

        mMapFragmentPerformer = new LandscapeMapFragmentPerformer(activity, R.id.content, R.dimen.search_bar_supposed_height);
        mFabButtonsPerformer = new LandscapeFabButtonsPerformer(R.id.my_location_fab);
        mSearchBarPerformer = new LandscapeSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
        mFiltersFragmentPerformer = new LandscapeFiltersFragmentPerformer(activity, R.id.fragment_filters);
        mBottomSheetPerformer = new LandscapeBottomSheetPerformer(
                activity.getResources(),
                new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail),
                R.id.bottom_sheet,
                R.dimen.search_bar_supposed_height
        );

        mCoordinator = new LandscapeMainCoordinator(
                mAction::markDone,
                mBottomSheetPerformer,
                mMapFragmentPerformer
        );
    }

    @Override
    public void init(@NonNull AppCompatActivity activity) {
        mFiltersFragmentPerformer.init();
        super.init(activity);
    }

    @Override
    public void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState) {
        mFiltersFragmentPerformer.restore();
        super.restore(activity, savedInstanceState);
    }
}
