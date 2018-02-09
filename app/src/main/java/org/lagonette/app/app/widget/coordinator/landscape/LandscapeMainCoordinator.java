package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.statement.Statement;

public class LandscapeMainCoordinator
        extends MainCoordinator {

    @NonNull
    @Override
    public MainState init(@NonNull MainState state) {

        if (!state.isFiltersLoaded) {
            loadFilters.run();
        }

        return super.init(state).buildUppon()
                .setFiltersLoaded(true)
                .build();
    }

    @NonNull
    @Override
    public MainState restore(@NonNull MainState state) {
        state = super.restore(state);
        MainState.Builder builder = state.buildUppon();

        if (!state.isFiltersLoaded) {
            loadFilters.run();
            builder.setFiltersLoaded(true);
        }

        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
            case BottomSheetBehavior.STATE_COLLAPSED:
                if (state.isLocationDetailLoaded) {
                    openBottomSheet.run();
                    builder.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    closeBottomSheet.run();
                    builder.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                if (!state.isLocationDetailLoaded) {
                    closeBottomSheet.run();
                    builder.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                if (state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                    builder.setLocationDetailLoaded(true);
                    builder.clearLoadedLocationId();
                }
                break;
        }

        return builder.build();
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state) {
        finishAction.run();
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state) {
        if (action.locationId > Statement.NO_ID) {
            openLocation.accept(action.locationId);
        } else if (action.item == null) {
            finishAction.run();
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
                        finishAction.run();
                    }
                    break;

                case BottomSheetBehavior.STATE_DRAGGING:
                    finishAction.run();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    wait.run();
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    switch (state.mapMovement) {

                        case MOVE:
                            wait.run();
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
                                finishAction.run();
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
                    finishAction.run();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    wait.run();
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    loadLocationDetail.accept(action.item.getId());
                    break;
            }
        }
    }

    @Override
    protected void computeIdle(@NonNull MainAction action, @NonNull MainState state)  {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                wait.run();
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (!state.isLocationDetailLoaded) {
                    closeBottomSheet.run();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                if (state.isLocationDetailLoaded) {
                    unloadLocationDetail.run();
                }
                break;
        }
    }
}
