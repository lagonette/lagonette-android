package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.room.statement.Statement;

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
    public void restore() {
        super.restore();
        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (mFiltersPerformer.isLoaded() && mLocationDetailPerformer.isLoaded()) {
                    mLocationDetailPerformer.unloadFragment();
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
    protected void computeFiltersOpening(@NonNull MainAction action) {

        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_HIDDEN:

                if (mLocationDetailPerformer.isLoaded()) {
                    mLocationDetailPerformer.unloadFragment();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mBottomSheetPerformer.openBottomSheet();
                }
                else {
                    mFiltersPerformer.loadFragment();
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (mLocationDetailPerformer.isLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (mLocationDetailPerformer.isLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (mFiltersPerformer.isLoaded() && mLocationDetailPerformer.isLoaded()) {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else if (mLocationDetailPerformer.isLoaded()) {
                    mDoneMarker.markPendingActionAsDone();
                }
                else {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                if (mFiltersPerformer.isLoaded() && mLocationDetailPerformer.isLoaded()) {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    // Wait
                }
                else if (mLocationDetailPerformer.isLoaded()) {
                    mBottomSheetPerformer.closeBottomSheet();
                }
                else {
                    wtf();
                    mBottomSheetPerformer.closeBottomSheet();
                }
                break;
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action) {
        if (action.locationId > Statement.NO_ID) { //TODO Immutability && unidirectional data flow
            action.item = mMapFragmentPerformer.retrieveLocationItem(action.locationId);
            action.locationId = Statement.NO_ID;
        }

        if (mLocationDetailPerformer.isLoaded()) {
            switch (mBottomSheetPerformer.getState()) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    long selectedId = action.item != null
                            ? action.item.getId()
                            : Statement.NO_ID;
                    if (action.shouldMove) {
                        action.shouldMove = false; //TODO Unidirectional Data flow !
                        mMapFragmentPerformer.moveToLocation(action.item);
                    } else if (!mLocationDetailPerformer.isLoaded(selectedId)) {
                        mLocationDetailPerformer.loadFragment(action.item.getId());
                    } else {
                        mDoneMarker.markPendingActionAsDone();
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mDoneMarker.markPendingActionAsDone();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    switch (mMapFragmentPerformer.getMapMovement()) {

                        case MOVE:
                            // Well, it's okay. Just wait.
                            break;

                        case IDLE:
                            if (action.item != null) {
                                if (action.shouldMove) { //TODO Use reason to mark action done if the user move something
                                    action.shouldMove = false;
                                    mMapFragmentPerformer.moveToLocation(action.item);
                                } else {
                                    mBottomSheetPerformer.openBottomSheet();
                                }
                            } else {
                                mDoneMarker.markPendingActionAsDone();
                            }
                            break;
                    }
                    break;
            }
        }
        else {
            switch (mBottomSheetPerformer.getState()) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    mBottomSheetPerformer.closeBottomSheet();
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mDoneMarker.markPendingActionAsDone();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    if (mFiltersPerformer.isLoaded()) {
                        mFiltersPerformer.unloadFragment();
                    }
                    else if (action.item != null) {
                        mLocationDetailPerformer.loadFragment(action.item.getId());
                    } else {
                        mDoneMarker.markPendingActionAsDone();
                    }
                    break;
            }
        }
    }

    @Override
    protected void unloadBottomSheetFragment() {
        if (mFiltersPerformer.isLoaded()) {
            mFiltersPerformer.unloadFragment();
        }
        else if (mLocationDetailPerformer.isLoaded()) {
            mLocationDetailPerformer.unloadFragment();
        }
    }

}
