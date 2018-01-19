package org.lagonette.app.app.widget.coordinator;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.Logger;
import org.lagonette.app.tools.functions.LongConsumer;
import org.lagonette.app.tools.functions.NullFunctions;

public abstract class MainCoordinator {

    private static final String TAG = "MainCoordinator";

    @NonNull
    public Consumer<MainAction> finishAction = NullFunctions::doNothing;

    @NonNull
    public Runnable openBottomSheet = NullFunctions::doNothing;

    @NonNull
    public Runnable closeBottomSheet = NullFunctions::doNothing;

    @NonNull
    public Consumer<Cluster<LocationItem>> moveMapToCluster = NullFunctions::doNothing;

    @NonNull
    public Consumer<LocationItem> moveMapToLocation = NullFunctions::doNothing;

    @NonNull
    public Runnable moveMapToMyLocation = NullFunctions::doNothing;

    @NonNull
    public Runnable stopMapMoving = NullFunctions::doNothing;

    @NonNull
    public Runnable moveMapToFootprint = NullFunctions::doNothing;

    @NonNull
    public LongConsumer openLocation = NullFunctions::doNothing;

    @NonNull
    public Runnable loadFilters = NullFunctions::doNothing;

    @NonNull
    public Runnable unloadFilters = NullFunctions::doNothing;

    @NonNull
    public LongConsumer loadLocationDetail = NullFunctions::doNothing;

    @NonNull
    public Runnable unloadLocationDetail = NullFunctions::doNothing;

    @NonNull
    protected Runnable wait = NullFunctions::doNothing;

    @CallSuper
    public void process(@NonNull MainState state) {
        if (state.action != null) {
            switch (state.action.type) {

                case RESTORE:
                    computeRestore(state.action, state);
                    break;

                case BACK:
                    computeBack(state.action, state);
                    break;

                case OPEN_FILTERS:
                    computeFiltersOpening(state.action, state);
                    break;

                case MOVE_TO_MY_LOCATION:
                    computeMovementToMyLocation(state.action, state);
                    break;

                case MOVE_TO_FOOTPRINT:
                    computeMovementToFootprint(state.action, state);
                    break;

                case MOVE_TO_CLUSTER:
                    computeMovementToCluster(state.action, state);
                    break;

                case MOVE_TO_AND_OPEN_LOCATION:
                    computeMovementToAndOpeningLocation(state.action, state);
                    break;

                case SHOW_FULL_MAP:
                    computeFullMapShowing(state.action, state);
                    break;

                default:
                case IDLE:
                    computeIdle(state.action, state);
                    break;
            }
        }
    }

    protected abstract void computeRestore(@NonNull MainAction action, @NonNull MainState state);

    protected abstract void computeFiltersOpening(@NonNull MainAction action, @NonNull MainState state);

    private void computeMovementToMyLocation(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
            case BottomSheetBehavior.STATE_DRAGGING:
                wait.run();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (state.mapMovement) {

                    case IDLE:
                        moveMapToMyLocation.run();
                        break;

                    case MOVE:
                        finishAction.accept(action);
                        break;
                }
                break;
        }
    }

    protected abstract void computeIdle(@NonNull MainAction action, @NonNull MainState state);

    private void computeFullMapShowing(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                wait.run();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                        finishAction.accept(action);
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                        finishAction.accept(action);
                break;
        }
    }

    protected abstract void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state);

    private void computeMovementToCluster(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                wait.run();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (state.mapMovement) {

                    case MOVE:
                        wait.run();
                        break;

                    case IDLE:
                        if (action.cluster != null && action.shouldMove) {
                            action.shouldMove = false;
                            moveMapToCluster.accept(action.cluster);
                        } else {
                            finishAction.accept(action);
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                stopMapMoving.run();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                stopMapMoving.run();
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (state.mapMovement) {

                    case IDLE:
                        moveMapToFootprint.run();
                        break;

                    case MOVE:
                        finishAction.accept(action);
                        break;
                }
                break;
        }
    }

    private void computeBack(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                finishAction.accept(action);
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                closeBottomSheet.run();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                finishAction.accept(action);
                break;
        }
    }

    protected void wtf(@NonNull MainState state) {
        FirebaseCrash.logcat(Log.ERROR, TAG, state.toString());
        FirebaseCrash.report(new IllegalArgumentException("Coordinator received a weird state"));
    }

    public void setupLoggers() {
        if (BuildConfig.DEBUG) {
            finishAction = Logger.log(
                    TAG, "INTENT --> finishAction",
                    finishAction
            );

            openBottomSheet = Logger.log(
                    TAG, "INTENT --> openBottomSheet",
                    openBottomSheet
            );
            closeBottomSheet = Logger.log(
                    TAG, "INTENT --> closeBottomSheet",
                    closeBottomSheet
            );

            moveMapToCluster = Logger.log(
                    TAG, "INTENT --> moveMapToCluster",
                    moveMapToCluster
            );
            moveMapToLocation = Logger.log(
                    TAG, "INTENT --> moveMapToLocation",
                    moveMapToLocation
            );
            moveMapToMyLocation = Logger.log(
                    TAG, "INTENT --> moveMapToMyLocation",
                    moveMapToMyLocation
            );
            stopMapMoving = Logger.log(
                    TAG, "INTENT --> stopMapMoving",
                    stopMapMoving
            );
            moveMapToFootprint = Logger.log(
                    TAG, "INTENT --> moveMapToFootprint",
                    moveMapToFootprint
            );
            openLocation = Logger.log(
                    TAG, "INTENT --> openLocation",
                    openLocation
            );

            loadFilters = Logger.log(
                    TAG, "INTENT --> loadFilters",
                    loadFilters
            );
            unloadFilters = Logger.log(
                    TAG, "INTENT --> unloadFilters",
                    unloadFilters
            );

            loadLocationDetail = Logger.log(
                    TAG, "INTENT --> loadLocationDetail",
                    loadLocationDetail
            );
            unloadLocationDetail = Logger.log(
                    TAG, "INTENT --> unloadLocationDetail",
                    unloadLocationDetail
            );

            wait = Logger.log(
                    TAG, "INTENT --X wait",
                    (Runnable) NullFunctions::doNothing
            );
        }
    }

}
