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

public class LandscapeMainPresenter
        extends MainPresenter<
        LandscapeFabButtonsPerformer,
        LandscapeMapFragmentPerformer,
        LandscapeSearchBarPerformer> {

    @NonNull
    @Override
    protected MainCoordinator createCoordinator() {
        return new LandscapeMainCoordinator();
    }

    @NonNull
    @Override
    protected BottomSheetPerformer createBottomSheetPerformer(@NonNull PresenterActivity activity) {
        return new LandscapeBottomSheetPerformer(activity.getResources(), R.id.bottom_sheet);
    }

    @NonNull
    @Override
    protected LandscapeSearchBarPerformer createSearchBarPerformer(@NonNull PresenterActivity activity) {
        return new LandscapeSearchBarPerformer(R.id.search_bar, R.id.progress_bar, R.id.search_text);
    }

    @NonNull
    @Override
    protected LandscapeFabButtonsPerformer createFabButtonPerformer(@NonNull PresenterActivity activity) {
        return new LandscapeFabButtonsPerformer(R.id.my_location_fab);
    }

    @NonNull
    @Override
    protected LandscapeMapFragmentPerformer createMapFragmentPerformer(@NonNull PresenterActivity activity) {
        return new LandscapeMapFragmentPerformer(activity, R.id.content);
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
