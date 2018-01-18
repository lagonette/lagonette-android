package org.lagonette.app.app.widget.coordinator.portrait;

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
    public void computeRestore(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    mLocationDetail.unloadFragment();
                }
                else {
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                if (state.isFiltersLoaded) {
                    mFilters.unloadFragment();
                }
                else if (state.isLocationDetailLoaded) {
                    mLocationDetail.unloadFragment();
                }
                else {
                    mAction.finish(action);
                }
                break;
        }
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state) {

        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:

                if (state.isLocationDetailLoaded) {
                    mLocationDetail.unloadFragment();
                }
                else if (state.isFiltersLoaded) {
                    mBottomSheet.openBottomSheet();
                }
                else {
                    mFilters.loadFragment();
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                if (state.isLocationDetailLoaded) {
                    mBottomSheet.closeBottomSheet();
                }
                else if (state.isFiltersLoaded) {
                    mAction.finish(action);
                }
                else {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                if (state.isLocationDetailLoaded) {
                    mBottomSheet.closeBottomSheet();
                }
                else if (state.isFiltersLoaded) {
                    mAction.finish(action);
                }
                else {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                else if (state.isFiltersLoaded) {
                    mAction.finish(action);
                }
                else if (state.isLocationDetailLoaded) {
                    mAction.finish(action);
                }
                else {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                else if (state.isFiltersLoaded) {
                    // Wait
                }
                else if (state.isLocationDetailLoaded) {
                    mBottomSheet.closeBottomSheet();
                }
                else {
                    wtf(state);
                    mBottomSheet.closeBottomSheet();
                }
                break;
        }
    }

    @Override
    protected void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state) {
        if (action.locationId > Statement.NO_ID) {
            mMap.openLocation(action.locationId);
        }
        else if (action.item == null) {
            mAction.finish(action);
        }
        else if (state.isLocationDetailLoaded) {
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
                                    action.shouldMove = false;
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
        }
        else {
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
                    if (state.isFiltersLoaded) {
                        mFilters.unloadFragment();
                    }
                    else if (action.item != null) { //TODO
                        mLocationDetail.loadFragment(action.item.getId());
                    } else {
                        mAction.finish(action);
                    }
                    break;
            }
        }
    }

    @Override
    protected void unloadBottomSheetFragment(@NonNull MainState state) {
        if (state.isFiltersLoaded) {
            mFilters.unloadFragment();
        }
        else if (state.isLocationDetailLoaded) {
            mLocationDetail.unloadFragment();
        }
    }

}
