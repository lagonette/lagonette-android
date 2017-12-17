package org.lagonette.app.app.widget.performer.portrait;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class PortraitBottomSheetPerformer
        extends BottomSheetPerformer {

    @NonNull
    protected final FiltersFragmentPerformer mFiltersFragmentPerformer;

    //TODO Useless ?
    public PortraitBottomSheetPerformer(
            @NonNull Resources resources,
            @NonNull FiltersFragmentPerformer filtersFragmentPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @IdRes int bottomSheetRes,
            @DimenRes int searchBarHeightRes) {
        super(resources, locationDetailFragmentPerformer, bottomSheetRes, searchBarHeightRes);
        mFiltersFragmentPerformer = filtersFragmentPerformer;
    }

    @Override
    public void restore(@NonNull MainState mainState) {
        super.restore(mainState);

        switch (mainState.bottomSheetFragmentType.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                switch (mainState.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        if (!mFiltersFragmentPerformer.isLoaded()) {
                            mFiltersFragmentPerformer.loadFragment();
                        }
                        mSlideablePerformer = mFiltersFragmentPerformer;
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        if (mFiltersFragmentPerformer.isLoaded()) {
                            mFiltersFragmentPerformer.unloadFragment();
                        }
                        mSlideablePerformer = null;
                        break;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                switch (mainState.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        if (!mLocationDetailFragmentPerformer.isLoaded()) {
                            mLocationDetailFragmentPerformer.loadFragment(mainState.bottomSheetFragmentType.getLocationId(), false);
                        }
                        mSlideablePerformer = mLocationDetailFragmentPerformer;
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        if (mLocationDetailFragmentPerformer.isLoaded()) {
                            mLocationDetailFragmentPerformer.unloadFragment();
                        }
                        mSlideablePerformer = null;
                        break;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_NONE:
                if (mFiltersFragmentPerformer.isLoaded()) {
                    mFiltersFragmentPerformer.unloadFragment();
                }
                if (mLocationDetailFragmentPerformer.isLoaded()) {
                    mLocationDetailFragmentPerformer.unloadFragment();
                }
                mSlideablePerformer = null;
                break;
        }
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void unloadFragment() {
        if (mFiltersFragmentPerformer != null) {
            mFiltersFragmentPerformer.unloadFragment();
        }
        super.unloadFragment();
    }

    public void loadFiltersFragment() {
        if (mFiltersFragmentPerformer != null) {
            mFiltersFragmentPerformer.loadFragment();
            mSlideablePerformer = mFiltersFragmentPerformer;

            if (mFragmentObserver != null) {
                mFragmentObserver.onFiltersLoaded();
            }
        }
    }
}
