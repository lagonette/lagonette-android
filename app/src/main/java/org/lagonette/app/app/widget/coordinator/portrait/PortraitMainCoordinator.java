package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.statement.Statement;

public class PortraitMainCoordinator
        extends MainCoordinator {

    @Override
    public void computeRestore(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                }
                else {
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                if (state.isFiltersLoaded) {
                    unloadFilters.run();
                }
                else if (state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                }
                else {
                    finishAction.accept(action);
                }
                break;
        }
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state) {

        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:

                if (state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                }
                else if (state.isFiltersLoaded) {
                    openBottomSheet.run();
                }
                else {
                    loadFilters.run();
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (state.isLocationDetailLoaded) {
                    closeBottomSheet.run();
                }
                else if (state.isFiltersLoaded) {
                    finishAction.accept(action);
                }
                else {
                    wtf(state);
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (state.isLocationDetailLoaded) {
                    closeBottomSheet.run();
                }
                else if (state.isFiltersLoaded) {
                    finishAction.accept(action);
                }
                else {
                    wtf(state);
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    wtf(state);
                    closeBottomSheet.run();
                }
                else if (state.isFiltersLoaded) {
                    finishAction.accept(action);
                }
                else if (state.isLocationDetailLoaded) {
                    finishAction.accept(action);
                }
                else {
                    wtf(state);
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    wtf(state);
                    closeBottomSheet.run();
                }
                else if (state.isFiltersLoaded) {
                    // Wait
                }
                else if (state.isLocationDetailLoaded) {
                    closeBottomSheet.run();
                }
                else {
                    wtf(state);
                    closeBottomSheet.run();
                }
                break;
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state) {
        if (action.locationId > Statement.NO_ID) {
            openLocation.accept(action.locationId);
        }
        else if (action.item == null) {
            finishAction.accept(action);
        }
        else if (state.isLocationDetailLoaded) {
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
                            if (action.shouldMove) { //TODO Use reason to mark action done if the user move something
                                action.shouldMove = false;
                                moveMapToLocation.accept(action.item);
                            } else {
                                openBottomSheet.run();
                            }
                            break;
                    }
                    break;
            }
        }
        else {
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
                    if (state.isFiltersLoaded) {
                        unloadFilters.run();
                    }
                    else {
                        loadLocationDetail.accept(action.item.getId());
                    }
                    break;
            }
        }
    }

    @Override
    protected void computeIdle(@NonNull MainAction action, @NonNull MainState state)  {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (!state.isLocationDetailLoaded && !state.isFiltersLoaded) {
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                if (state.isFiltersLoaded) {
                    unloadFilters.run();
                }
                else if (state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                }
                break;
        }
    }

}
