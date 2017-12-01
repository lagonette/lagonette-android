package org.lagonette.app.app.widget.coordinator;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;
import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.room.statement.Statement;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_BACK;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_IDLE;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_SHOW_FULL_MAP;

public class MainCoordinator {

    private static final String TAG = "MainCoordinator";

    public interface BottomSheetCallback {

        void closeBottomSheet();

        void openBottomSheet();

    }

    public interface FragmentLoader {

        void loadFiltersFragment();

        void loadLocationFragment(long locationId, boolean animation);

        void unloadFragment();

    }

    public interface DoneMarker {

        void markPendingActionasDone();

    }

    @NonNull
    private final DoneMarker mDoneMarker;

    @NonNull
    private final BottomSheetCallback mBottomSheetCallback;

    @NonNull
    private final FragmentLoader mFragmentLoader;

    @NonNull
    private final MapFragmentPerformer mMapFragmentPerformer;

    public MainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        mBottomSheetCallback = bottomSheetCallback; //TODO do init in init()
        mFragmentLoader = fragmentLoader;
        mMapFragmentPerformer = mapFragmentPerformer;
        mDoneMarker = doneMarker;
    }

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
                markPendingActionDone();
                return false;
        }
    }

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

    private void computeIdle(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                break;
            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                switch (statefulAction.state.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        unloadFragment();
                        break;
                }
                break;

            default:
            case BottomSheetFragmentType.FRAGMENT_NONE:
                break;
        }
    }

    private void computeFullMapShowing(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                // Well, just wait.
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                markPendingActionDone();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                markPendingActionDone();
                break;
        }
    }

    private void computeMovementToLocation(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                switch (statefulAction.state.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetCallback.closeBottomSheet();
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        markPendingActionDone();
                        break;

                    case BottomSheetBehavior.STATE_SETTLING:
                        // Well, it's okay. Just wait.
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        if (statefulAction.action.item != null) {
                            loadLocationFragment(statefulAction.action.item.getId(), false);
                        }
                        else {
                            markPendingActionDone();
                        }
                        break;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                switch (statefulAction.state.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        long selectedId = statefulAction.action.item != null
                                ?  statefulAction.action.item.getId()
                                : Statement.NO_ID;
                        if (statefulAction.action.shouldMove) {
                            statefulAction.action.shouldMove = false;
                            mMapFragmentPerformer.moveToLocation(statefulAction.action.item);
                        }
                        else if (statefulAction.state.bottomSheetFragmentType.getLocationId() != selectedId) {
                            loadLocationFragment(statefulAction.action.item.getId(), true);
                        } else {
                            markPendingActionDone();
                        }
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        markPendingActionDone();
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
                                        moveMapToLocation(statefulAction.action.item);
                                    }
                                    else {
                                        openBottomSheet();
                                    }
                                }
                                else {
                                    markPendingActionDone();
                                }
                                break;
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToCluster(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetCallback.closeBottomSheet();
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
                            moveMapToCluster(statefulAction.action.cluster);
                        }
                        else {
                            markPendingActionDone();
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                stopMovingMap();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                stopMovingMap();
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.mapMovement) {

                    case MainState.STATE_MOVEMENT_IDLE:
                        moveMapToFootprint();
                        break;

                    case MainState.STATE_MOVEMENT_MOVE:
                        markPendingActionDone();
                        break;
                }
                break;
        }
    }

    private void computeMovementToMyLocation(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                stopMovingMap();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                stopMovingMap();
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.mapMovement) {

                    case MainState.STATE_MOVEMENT_IDLE:
                        moveMapToMyLocation();
                        break;

                    case MainState.STATE_MOVEMENT_MOVE:
                        markPendingActionDone();
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
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                markPendingActionDone();
                break;
        }
    }

    private void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        openBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;
                }
                break;
        }
    }

    private void markPendingActionDone() {
        mDoneMarker.markPendingActionasDone();
    }

    private void stopMovingMap() {
        mMapFragmentPerformer.stopMoving();
    }

    private void moveMapToCluster(@NonNull Cluster<PartnerItem> cluster) {
        mMapFragmentPerformer.moveToCluster(cluster);
    }

    private void moveMapToFootprint() {
        mMapFragmentPerformer.moveToFootprint();
    }

    private void moveMapToMyLocation() {
        mMapFragmentPerformer.moveToMyLocation();
    }

    private void moveMapToLocation(@NonNull PartnerItem item) {
        mMapFragmentPerformer.moveToLocation(item);
    }

    private void closeBottomSheet() {
        mBottomSheetCallback.closeBottomSheet();
    }

    private void openBottomSheet() {
        mBottomSheetCallback.openBottomSheet();
    }

    private void loadFiltersFragment() {
        mFragmentLoader.loadFiltersFragment();
    }

    private void loadLocationFragment(long locationId, boolean animation) {
        mFragmentLoader.loadLocationFragment(locationId, animation);
    }

    private void unloadFragment() {
        mFragmentLoader.unloadFragment();
    }

    private void wtf() {
        //TODO log exception, just in case
    }
}
