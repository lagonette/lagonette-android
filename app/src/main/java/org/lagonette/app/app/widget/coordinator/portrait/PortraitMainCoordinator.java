package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.viewmodel.MainActionViewModel;
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
            @NonNull MainActionViewModel mainActionViewModel,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersFragmentPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(mainActionViewModel, bottomSheetPerformer, filtersFragmentPerformer, locationDetailFragmentPerformer, mapFragmentPerformer);
    }

    @Override
    public void restore() {
        super.restore();
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (mFilters.isLoaded() && mLocationDetail.isLoaded()) {
                    mLocationDetail.unloadFragment();
                }
                else {
                    mBottomSheet.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mFilters.unloadFragment();
                mLocationDetail.unloadFragment();
                break;
        }
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action) {

        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_HIDDEN:

                if (mLocationDetail.isLoaded()) {
                    mLocationDetail.unloadFragment();
                }
                else if (mFilters.isLoaded()) {
                    mBottomSheet.openBottomSheet();
                }
                else {
                    mFilters.loadFragment();
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (mLocationDetail.isLoaded()) {
                    mBottomSheet.closeBottomSheet();
                }
                else if (mFilters.isLoaded()) {
                    mAction.markDone();
                }
                else {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (mLocationDetail.isLoaded()) {
                    mBottomSheet.closeBottomSheet();
                }
                else if (mFilters.isLoaded()) {
                    mAction.markDone();
                }
                else {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (mFilters.isLoaded() && mLocationDetail.isLoaded()) {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                else if (mFilters.isLoaded()) {
                    mAction.markDone();
                }
                else if (mLocationDetail.isLoaded()) {
                    mAction.markDone();
                }
                else {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                if (mFilters.isLoaded() && mLocationDetail.isLoaded()) {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                else if (mFilters.isLoaded()) {
                    // Wait
                }
                else if (mLocationDetail.isLoaded()) {
                    mBottomSheet.closeBottomSheet();
                }
                else {
                    wtf();
                    mBottomSheet.closeBottomSheet();
                }
                break;
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action) {
        if (action.locationId > Statement.NO_ID) {
            mMap.openLocation(action.locationId);
        }
        else if (action.item == null) {
            mAction.markDone();
        }
        else if (mLocationDetail.isLoaded()) {
            switch (mBottomSheet.getState()) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    long selectedId = action.item != null
                            ? action.item.getId()
                            : Statement.NO_ID;
                    if (action.shouldMove) {
                        action.shouldMove = false; //TODO Unidirectional Data flow !
                        mMap.moveToLocation(action.item);
                    } else if (!mLocationDetail.isLoaded(selectedId)) {
                        mLocationDetail.loadFragment(action.item.getId());
                    } else {
                        mAction.markDone();
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mAction.markDone();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    switch (mMap.getMapMovement()) {

                        case MOVE:
                            // Well, it's okay. Just wait.
                            break;

                        case IDLE:
                            if (action.item != null) {
                                if (action.shouldMove) { //TODO Use reason to mark action done if the user move something
                                    action.shouldMove = false;
                                    mMap.moveToLocation(action.item);
                                } else {
                                    mBottomSheet.openBottomSheet();
                                }
                            } else {
                                mAction.markDone();
                            }
                            break;
                    }
                    break;
            }
        }
        else {
            switch (mBottomSheet.getState()) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    mBottomSheet.closeBottomSheet();
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mAction.markDone();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    if (mFilters.isLoaded()) {
                        mFilters.unloadFragment();
                    }
                    else if (action.item != null) {
                        mLocationDetail.loadFragment(action.item.getId());
                    } else {
                        mAction.markDone();
                    }
                    break;
            }
        }
    }

    @Override
    protected void unloadBottomSheetFragment() {
        if (mFilters.isLoaded()) {
            mFilters.unloadFragment();
        }
        else if (mLocationDetail.isLoaded()) {
            mLocationDetail.unloadFragment();
        }
    }

}
