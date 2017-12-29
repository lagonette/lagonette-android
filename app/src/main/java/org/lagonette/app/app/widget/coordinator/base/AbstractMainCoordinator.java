package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.room.statement.Statement;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_BACK;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_IDLE;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_SHOW_FULL_MAP;

public abstract class AbstractMainCoordinator<BSP extends BottomSheetPerformer> implements MainCoordinator {

    private static final String TAG = "AbstractMainCoordinator";

    @NonNull
    protected final DoneMarker mDoneMarker;

    @NonNull
    protected final BSP mBottomSheetPerformer;

    @NonNull
    protected final FiltersFragmentPerformer mFiltersPerformer;

    @NonNull
    protected final LocationDetailFragmentPerformer mLocationDetailPerformer;

    @NonNull
    protected final MapFragmentPerformer mMapFragmentPerformer;

    public AbstractMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BSP bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersFragmentPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        mBottomSheetPerformer = bottomSheetPerformer;
        mFiltersPerformer = filtersFragmentPerformer;
        mLocationDetailPerformer = locationDetailFragmentPerformer;
        mMapFragmentPerformer = mapFragmentPerformer;
        mDoneMarker = doneMarker;
    }

    @Override
    @CallSuper
    public void init() {
        mBottomSheetPerformer.closeBottomSheet();
        mMapFragmentPerformer.loadFragment();
    }

    @Override
    @CallSuper
    public void restore(@NonNull MainState state) {
        mMapFragmentPerformer.restoreFragment();
        mFiltersPerformer.restoreFragment();
        mLocationDetailPerformer.restoreFragment();
    }

    @Override
    public boolean back(@NonNull MainStatefulAction statefulAction) {
        Log.d(TAG, "Coordinator -> Action: BACK");
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                statefulAction.action.type = ACTION_BACK;
                computeBack(statefulAction);
                return true;

            default:
            case BottomSheetBehavior.STATE_HIDDEN:
                mDoneMarker.markPendingActionAsDone();
                return false;
        }
    }

    @Override
    @CallSuper
    public void process(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.action.type) {

            case ACTION_BACK:
                computeBack(statefulAction);
                break;

            case ACTION_OPEN_FILTERS:
                computeFiltersOpening(statefulAction);
                break;

            case ACTION_MOVE_TO_MY_LOCATION:
                computeMovementToMyLocation(statefulAction);
                break;

            case ACTION_MOVE_TO_FOOTPRINT:
                computeMovementToFootprint(statefulAction);
                break;

            case ACTION_MOVE_TO_CLUSTER:
                computeMovementToCluster(statefulAction);
                break;

            case ACTION_MOVE_TO_LOCATION:
                computeMovementToLocation(statefulAction);
                break;

            case ACTION_SHOW_FULL_MAP:
                computeFullMapShowing(statefulAction);
                break;

            default:
            case ACTION_IDLE:
                computeIdle(statefulAction);
                break;
        }
    }

    protected abstract void computeFiltersOpening(@NonNull MainStatefulAction statefulAction);

    private void computeMovementToMyLocation(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
            case BottomSheetBehavior.STATE_DRAGGING:
                // Wait.
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.mapMovement) {

                    case MainState.STATE_MOVEMENT_IDLE:
                        mMapFragmentPerformer.moveToMyLocation();
                        break;

                    case MainState.STATE_MOVEMENT_MOVE:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;
        }
    }

    private void computeIdle(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                unloadBottomSheetFragment(statefulAction.state);
                break;
        }
    }

    protected abstract void unloadBottomSheetFragment(@NonNull MainState state);

    private void computeFullMapShowing(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                // Well, just wait.
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                mDoneMarker.markPendingActionAsDone();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mDoneMarker.markPendingActionAsDone();
                break;
        }
    }

    private void computeMovementToLocation(@NonNull MainStatefulAction statefulAction) {
        if (statefulAction.state.bottomSheetFragmentState.isLocationDetailLoaded()) {
            switch (statefulAction.state.bottomSheetState) {

                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                    long selectedId = statefulAction.action.item != null
                            ? statefulAction.action.item.getId()
                            : Statement.NO_ID;
                    if (statefulAction.action.shouldMove) {
                        statefulAction.action.shouldMove = false;
                        mMapFragmentPerformer.moveToLocation(statefulAction.action.item);
                    } else if (statefulAction.state.bottomSheetFragmentState.getLocationId() != selectedId) {
                        mLocationDetailPerformer.loadFragment(statefulAction.action.item.getId(), true);
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
                    switch (statefulAction.state.mapMovement) {

                        case MainState.STATE_MOVEMENT_MOVE:
                            // Well, it's okay. Just wait.
                            break;

                        case MainState.STATE_MOVEMENT_IDLE:
                            if (statefulAction.action.item != null) {
                                if (statefulAction.action.shouldMove) { //TODO Use reason to mark action done if the user move something
                                    statefulAction.action.shouldMove = false;
                                    mMapFragmentPerformer.moveToLocation(statefulAction.action.item);
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
            switch (statefulAction.state.bottomSheetState) {

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
                    if (statefulAction.action.item != null) {
                        mLocationDetailPerformer.loadFragment(statefulAction.action.item.getId(), false);
                    } else {
                        mDoneMarker.markPendingActionAsDone();
                    }
                    break;
            }
        }
    }

    private void computeMovementToCluster(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.mapMovement) {

                    case MainState.STATE_MOVEMENT_MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case MainState.STATE_MOVEMENT_IDLE:
                        if (statefulAction.action.cluster != null && statefulAction.action.shouldMove) {
                            statefulAction.action.shouldMove = false;
                            mMapFragmentPerformer.moveToCluster(statefulAction.action.cluster);
                        } else {
                            mDoneMarker.markPendingActionAsDone();
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                mMapFragmentPerformer.stopMoving();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                mMapFragmentPerformer.stopMoving();
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.mapMovement) {

                    case MainState.STATE_MOVEMENT_IDLE:
                        mMapFragmentPerformer.moveToFootprint();
                        break;

                    case MainState.STATE_MOVEMENT_MOVE:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;
        }
    }

    private void computeBack(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                // Do nothing
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mDoneMarker.markPendingActionAsDone();
                break;
        }
    }

    protected void wtf(@NonNull MainState state) {
        FirebaseCrash.logcat(Log.ERROR, TAG, state.toString());
        FirebaseCrash.report(new IllegalArgumentException("Coordinator received a weird state"));
    }

}
