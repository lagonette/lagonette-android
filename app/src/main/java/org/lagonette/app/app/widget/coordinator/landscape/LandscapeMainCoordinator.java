package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.room.statement.Statement;

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
    protected void computeFiltersOpening(@NonNull MainAction action) {
        mDoneMarker.markPendingActionAsDone();
    }

    @Override
    protected void unloadBottomSheetFragment() {
        if (mLocationDetailPerformer.isLoaded()) {
            mLocationDetailPerformer.unloadFragment();
        }
    }

    @Override
    public void init() {
        super.init();
        mFiltersPerformer.loadFragment();
    }

    @Override
    public void restore() {
        super.restore();
        if (!mFiltersPerformer.isLoaded()) {
            mFiltersPerformer.loadFragment();
        }
        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (mFiltersPerformer.isLoaded() && mLocationDetailPerformer.isLoaded()) {
                    wtf();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mBottomSheetPerformer.restoreCloseState();
                }
                else if (mLocationDetailPerformer.isLoaded()) {
                    mBottomSheetPerformer.restoreOpenState();
                }
                else {
                    mBottomSheetPerformer.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (mFiltersPerformer.isLoaded() && mLocationDetailPerformer.isLoaded()) {
                    wtf();
                }
                else if (mFiltersPerformer.isLoaded()) {
                    mBottomSheetPerformer.restoreCloseState();
                }
                else if (mLocationDetailPerformer.isLoaded()) {
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
                    if (action.item != null) {
                        mLocationDetailPerformer.loadFragment(action.item.getId());
                    } else {
                        mDoneMarker.markPendingActionAsDone();
                    }
                    break;
            }
        }
    }
}
