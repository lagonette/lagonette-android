package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.viewmodel.MainActionViewModel;
import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
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
    protected void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state) {
        mAction.finish(action);
    }

    @Override
    protected void unloadBottomSheetFragment(@NonNull MainState state) {
        if (state.isLocationDetailLoaded) {
            mLocationDetail.unloadFragment();
        }
    }

    @Override
    public void init() {
        super.init();
        mFilters.loadFragment();
    }

    /**
     * Because of the view#post() call, bottom sheet state is set after performer is connected to LiveData.
     * So bottom sheet state initialization is saved into LiveData.
     * If you do 2 orientation changes, the bottom sheet state is not the same as the started state.
     */
    @Override
    protected void computeRestore(@NonNull MainAction action, @NonNull MainState state) {
        if (!state.isFiltersLoaded) {
            mFilters.loadFragment();
        }
        else {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                    if (state.isLocationDetailLoaded) {
                        mBottomSheet.openBottomSheet();
                    } else {
                        mBottomSheet.closeBottomSheet();
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                case BottomSheetBehavior.STATE_EXPANDED:
                case BottomSheetBehavior.STATE_SETTLING:
                    if (state.isLocationDetailLoaded) {
                        mAction.finish(action);
                    } else {
                        mBottomSheet.closeBottomSheet();
                    }
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    if (state.isLocationDetailLoaded) {
                        mLocationDetail.unloadFragment();
                    }
                    else {
                        mAction.finish(action);
                    }
                    break;
            }
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state) {
        if (action.locationId > Statement.NO_ID) {
            mMap.openLocation(action.locationId);
        } else if (action.item == null) {
            mAction.finish(action);
        } else if (state.isLocationDetailLoaded) {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    long selectedId = action.item.getId();
                    if (action.shouldMove) {
                        action.shouldMove = false; //TODO Unidirectional Data flow !
                        mMap.moveToLocation(action.item);
                    } else if (state.loadedLocationId != selectedId) {
                        mLocationDetail.loadFragment(action.item.getId());
                    } else {
                        mAction.finish(action);
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mAction.finish(action);
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    switch (state.mapMovement) {

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
                                mAction.finish(action);
                            }
                            break;
                    }
                    break;
            }
        } else {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    mBottomSheet.closeBottomSheet();
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    mAction.finish(action);
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    mLocationDetail.loadFragment(action.item.getId());
                    break;
            }
        }
    }
}
