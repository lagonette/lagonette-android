package org.lagonette.app.app.widget.performer.landscape;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class LandscapeBottomSheetPerformer
        extends BottomSheetPerformer {

    public LandscapeBottomSheetPerformer(
            @NonNull Resources resources,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @IdRes int bottomSheetRes,
            @DimenRes int searchBarHeightRes) {
        super(resources, locationDetailFragmentPerformer, bottomSheetRes, searchBarHeightRes);
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
                        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        mSlideablePerformer = null;
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        mSlideablePerformer = null;
                        break;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                switch (mainState.bottomSheetState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (!mLocationDetailFragmentPerformer.isLoaded()) {
                            mLocationDetailFragmentPerformer.loadFragment(mainState.bottomSheetFragmentType.getLocationId(), false);
                        }
                        mSlideablePerformer = mLocationDetailFragmentPerformer;
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

}
