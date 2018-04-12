package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;

import javax.inject.Inject;

public class LandscapeMainPresenter
        extends MainPresenter<
        LandscapeFabButtonsPerformer,
        LandscapeMapFragmentPerformer,
        LandscapeSearchBarPerformer> {

    @Inject
    public LandscapeMainPresenter() {
    }

    @Override
    public void init(@NonNull PresenterActivity activity) {
        mFiltersFragmentPerformer.loadFragment();
        super.init(activity);
    }

    @Override
    public void onConstructed(@NonNull PresenterActivity activity) {
        super.onConstructed(activity);

        // Performer > Performer
        mBottomSheetPerformer.onSlideChanged = mLocationDetailFragmentPerformer::updateTopPadding;
        mSearchBarPerformer.onBottomChanged = mFiltersFragmentPerformer::updateTopPadding;
    }
}
