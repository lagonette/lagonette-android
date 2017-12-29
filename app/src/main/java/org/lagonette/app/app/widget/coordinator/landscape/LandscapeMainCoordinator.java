package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public class LandscapeMainCoordinator
        extends AbstractMainCoordinator {

    public LandscapeMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailPerformer,
            @NonNull MapFragmentPerformer mapPerformer) {
        super(doneMarker, bottomSheetPerformer, filtersPerformer, locationDetailPerformer, mapPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        mDoneMarker.markPendingActionAsDone();
    }

    @Override
    protected void unloadBottomSheetFragment(@NonNull MainState state) {
        if (state.bottomSheetFragmentState.isLocationDetailLoaded()) {
            mLocationDetailPerformer.unloadFragment();
        }
    }

    @Override
    public void init() {
        super.init();
        mFiltersPerformer.loadFragment();
    }

    @Override
    public void restore(@NonNull MainState state) {
        super.restore(state);
        if (!mFiltersPerformer.isLoaded()) {
            mFiltersPerformer.loadFragment();
        }
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (state.bottomSheetFragmentState.areAllLoaded()) {
                    wtf(state);
                }
                else if (state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mBottomSheetPerformer.restoreCloseState();
                }
                else if (state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mBottomSheetPerformer.restoreOpenState();
                }
                else {
                    mBottomSheetPerformer.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (state.bottomSheetFragmentState.areAllLoaded()) {
                    wtf(state);
                }
                else if (state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mBottomSheetPerformer.restoreCloseState();
                }
                else if (state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    // Do nothing
                }
                else {
                    mBottomSheetPerformer.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mLocationDetailPerformer.unloadFragment();
                break;
        }
    }
}
