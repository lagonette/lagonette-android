package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public class PortraitMainCoordinator
        extends AbstractMainCoordinator {

    public PortraitMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersFragmentPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(doneMarker, bottomSheetPerformer, filtersFragmentPerformer, locationDetailFragmentPerformer, mapFragmentPerformer);
    }

    @Override
    public void restore(@NonNull MainState state) {
        super.restore(state);
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (state.bottomSheetFragmentState.areAllLoaded()) {
                    wtf(state);
                }
                else if (state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mLocationDetailPerformer.unloadFragment();
                    if (!mFiltersPerformer.isLoaded()) {
                        mFiltersPerformer.loadFragment();
                    }
                }
                else if (state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mFiltersPerformer.unloadFragment();
                    if (!mLocationDetailPerformer.isLoaded()) {
                        mLocationDetailPerformer.loadFragment(state.bottomSheetFragmentState.getLocationId());
                    }
                }
                else {
                    mBottomSheetPerformer.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mFiltersPerformer.unloadFragment();
                mLocationDetailPerformer.unloadFragment();
                break;
        }
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {

        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:

                if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mLocationDetailPerformer.unloadFragment();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mBottomSheetPerformer.openBottomSheet();
                }
                else {
                    mFiltersPerformer.loadFragment();
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (statefulAction.state.bottomSheetFragmentState.areAllLoaded()) {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                if (statefulAction.state.bottomSheetFragmentState.areAllLoaded()) {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isFiltersLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else {
                    wtf(statefulAction.state);
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;
        }
    }

    @Override
    protected void unloadBottomSheetFragment(@NonNull MainState state) {
        if (state.bottomSheetFragmentState.isFiltersLoaded()) {
            mFiltersPerformer.unloadFragment();
        }
        else if (state.bottomSheetFragmentState.isLocationDetailLoaded()) {
            mLocationDetailPerformer.unloadFragment();
        }
    }

}
