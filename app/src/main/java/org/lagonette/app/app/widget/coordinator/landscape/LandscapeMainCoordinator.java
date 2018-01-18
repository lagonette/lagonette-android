package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.statement.Statement;

public class LandscapeMainCoordinator
        extends MainCoordinator {

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state) {
        finishAction.accept(action);
    }

    @Override
    protected void unloadBottomSheetFragment(@NonNull MainState state) {
        if (state.isLocationDetailLoaded) {
            unloadLocationDetail.run();
        }
    }

    /**
     * Because of the view#post() call, bottom sheet state is set after performer is connected to LiveData.
     * So bottom sheet state initialization is saved into LiveData.
     * If you do 2 orientation changes, the bottom sheet state is not the same as the started state.
     */
    @Override
    protected void computeRestore(@NonNull MainAction action, @NonNull MainState state) {
        if (!state.isFiltersLoaded) {
            loadFilters.run();
        }
        else {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                    if (state.isLocationDetailLoaded) {
                        openBottomSheet.run();
                    } else {
                        closeBottomSheet.run();
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                case BottomSheetBehavior.STATE_EXPANDED:
                case BottomSheetBehavior.STATE_SETTLING:
                    if (state.isLocationDetailLoaded) {
                        finishAction.accept(action);
                    } else {
                        closeBottomSheet.run();
                    }
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    if (state.isLocationDetailLoaded) {
                        unloadLocationDetail.run();
                    }
                    else {
                        finishAction.accept(action);
                    }
                    break;
            }
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state) {
        if (action.locationId > Statement.NO_ID) {
            openLocation.accept(action.locationId);
        } else if (action.item == null) {
            finishAction.accept(action);
        } else if (state.isLocationDetailLoaded) {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    long selectedId = action.item.getId();
                    if (action.shouldMove) {
                        action.shouldMove = false; //TODO Unidirectional Data flow !
                        moveMapToLocation.accept(action.item);
                    } else if (state.loadedLocationId != selectedId) {
                        loadLocationDetail.accept(action.item.getId());
                    } else {
                        finishAction.accept(action);
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    finishAction.accept(action);
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
                                    moveMapToLocation.accept(action.item);
                                } else {
                                    openBottomSheet.run();
                                }
                            } else {
                                finishAction.accept(action);
                            }
                            break;
                    }
                    break;
            }
        } else {
            switch (state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    closeBottomSheet.run();
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    finishAction.accept(action);
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    // Well, it's okay. Just wait.
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    loadLocationDetail.accept(action.item.getId());
                    break;
            }
        }
    }
}
