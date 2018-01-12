package org.lagonette.app.app.widget.coordinator.landscape;

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

public class LandscapeMainCoordinator
        extends AbstractMainCoordinator {

    public LandscapeMainCoordinator(
            @NonNull MainActionViewModel mainActionViewModel,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailPerformer,
            @NonNull MapFragmentPerformer mapPerformer) {
        super(mainActionViewModel, bottomSheetPerformer, filtersPerformer, locationDetailPerformer, mapPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action) {
        mAction.markDone();
    }

    @Override
    protected void unloadBottomSheetFragment() {
        if (mLocationDetail.isLoaded()) {
            mLocationDetail.unloadFragment();
        }
    }

    @Override
    public void init() {
        super.init();
        mFilters.loadFragment();
    }

    @Override
    public void restore() {
        super.restore();
        if (!mFilters.isLoaded()) {
            mFilters.loadFragment();
        }
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (mFilters.isLoaded() && mLocationDetail.isLoaded()) {
                    wtf();
                } else if (mFilters.isLoaded()) {
                    mBottomSheet.restoreCloseState();
                } else if (mLocationDetail.isLoaded()) {
                    mBottomSheet.restoreOpenState();
                } else {
                    mBottomSheet.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (mFilters.isLoaded() && mLocationDetail.isLoaded()) {
                    wtf();
                } else if (mFilters.isLoaded()) {
                    mBottomSheet.restoreCloseState();
                } else if (mLocationDetail.isLoaded()) {
                    // Do nothing
                } else {
                    mBottomSheet.restoreCloseState();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mLocationDetail.unloadFragment();
                break;
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action) {
        if (action.locationId > Statement.NO_ID) {
            mMap.openLocation(action.locationId);
        } else if (action.item == null) {
            mAction.markDone();
        } else if (mLocationDetail.isLoaded()) {
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
                                    action.shouldMove = false; //TODO Unidirectional Data flow !
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
        } else {
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
                    if (action.item != null) {
                        mLocationDetail.loadFragment(action.item.getId());
                    } else {
                        mAction.markDone();
                    }
                    break;
            }
        }
    }
}
