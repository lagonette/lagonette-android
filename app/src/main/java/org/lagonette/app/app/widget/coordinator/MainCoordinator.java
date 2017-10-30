package org.lagonette.app.app.widget.coordinator;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;
import org.lagonette.app.room.entity.statement.PartnerItem;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainCoordinator {

    private static final String TAG = "MainCoordinator";

    public interface BottomSheetCallback {

        void closeBottomSheet();

        void openBottomSheet();

    }

    public interface FragmentLoader {

        void loadFiltersFragment();

        void loadLocationFragment(long locationId);

        void unloadFragment();

    }

    private static final int ACTION_IDLE = 0;

    private static final int ACTION_BACK = 1;

    private static final int ACTION_OPEN_FILTERS = 2;

    private static final int ACTION_MOVE_TO_MY_LOCATION = 3;

    private static final int ACTION_MOVE_TO_FOOTPRINT = 4;

    private static final int ACTION_MOVE_TO_CLUSTER = 5;

    private static final int ACTION_MOVE_TO_LOCATION = 6;

    @Retention(SOURCE)
    @IntDef({
            ACTION_IDLE,
            ACTION_BACK,
            ACTION_OPEN_FILTERS,
            ACTION_MOVE_TO_MY_LOCATION,
            ACTION_MOVE_TO_FOOTPRINT,
            ACTION_MOVE_TO_CLUSTER,
            ACTION_MOVE_TO_LOCATION
    })
    private @interface ActionType {

    }

    private class State {

        @BottomSheetPerformer.State
        public int bottomSheetState;

        @NonNull
        public BottomSheetFragmentType bottomSheetFragmentType;

        @MapsFragment.Movement
        public int mapMovement;

        public boolean wasCoordinatorMovement;
    }

    /**
     * Action and those attributes.
     */
    private class PendingAction {

        @ActionType
        public int type;

        @Nullable
        public Cluster<PartnerItem> cluster;

        @Nullable
        public PartnerItem item;
    }

    private final State mState;

    private final PendingAction mPendingAction;

    @NonNull
    private final BottomSheetCallback mBottomSheetCallback;

    @NonNull
    private final FragmentLoader mFragmentLoader;

    @NonNull
    private final MapFragmentPerformer mMapFragmentPerformer;

    public MainCoordinator(
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        mBottomSheetCallback = bottomSheetCallback; //TODO do init in init()
        mFragmentLoader = fragmentLoader;
        mMapFragmentPerformer = mapFragmentPerformer;
        mPendingAction = new PendingAction();
        mPendingAction.type =  ACTION_IDLE;
        mState = new State();
        mState.bottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
        mState.mapMovement = MapsFragment.STATE_MOVEMENT_IDLE;
    }

    public void openFilters() {
        Log.d(TAG, "Coordinator -> Action: OPEN FILTERS");
        mPendingAction.type = ACTION_OPEN_FILTERS;
        computeFiltersOpening();
    }

    public boolean back() {
        Log.d(TAG, "Coordinator -> Action: BACK");
        switch (mState.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                mPendingAction.type = ACTION_BACK;
                computeBack();
                return true;

            default:
            case BottomSheetBehavior.STATE_HIDDEN:
                markPendingActionDone();
                return false;
        }
    }

    public void moveToMyLocation() {
        Log.d(TAG, "Coordinator -> Action: MOVE ON MY LOCATION");
        mPendingAction.type = ACTION_MOVE_TO_MY_LOCATION;
        computeMovementToMyLocation();
    }

    public void moveToFootprint() {
        Log.d(TAG, "Coordinator -> Action: MOVE ON FOOTPRINT");
        mPendingAction.type = ACTION_MOVE_TO_FOOTPRINT;
        computeMovementToFootprint();
    }

    public void moveToCluster(@NonNull Cluster<PartnerItem> cluster) {
        Log.d(TAG, "Coordinator -> Action: MOVE ON CLUSTER");
        mPendingAction.type = ACTION_MOVE_TO_CLUSTER;
        mPendingAction.cluster = cluster;
        computeMovementToCluster();
    }

    public void moveToLocation(@NonNull PartnerItem item) {
        Log.d(TAG, "Coordinator -> Action: MOVE ON PARTNER ITEM");
        mPendingAction.type = ACTION_MOVE_TO_LOCATION;
        mPendingAction.item = item;
        computeMovementToLocation();
    }

    public void notifyMapMovementChanged(@MapsFragment.Movement int newMovement) {
        Log.d(TAG, "Coordinator <- Notification: Map movement " + newMovement);
        mState.mapMovement = newMovement;
        dispatchAction();
    }

    public void notifyBottomSheetStateChanged(@BottomSheetPerformer.State int newState) {
        Log.d(TAG, "Coordinator <- Notification: Bottom sheet state " + newState);
        mState.bottomSheetState = newState;
        dispatchAction();
    }

    public void notifyBottomSheetFragmentChanged(@NonNull BottomSheetFragmentType newFragmentType) {
        Log.d(TAG, "Coordinator <- Notification: Bottom sheet fragment " + newFragmentType);
        mState.bottomSheetFragmentType = newFragmentType;
        dispatchAction();
    }

    private void dispatchAction() {
        switch (mPendingAction.type) {

            case ACTION_BACK:
                computeBack();
                break;

            case ACTION_OPEN_FILTERS:
                computeFiltersOpening();
                break;

            case ACTION_MOVE_TO_MY_LOCATION:
                computeMovementToMyLocation();
                break;

            case ACTION_MOVE_TO_FOOTPRINT:
                computeMovementToFootprint();
                break;

            case ACTION_MOVE_TO_CLUSTER:
                computeMovementToCluster();
                break;

            case ACTION_MOVE_TO_LOCATION:
                computeMovementToLocation();
                break;

            default:
            case ACTION_IDLE:
                // Do nothing
                break;
        }
    }

    private void computeMovementToLocation() {
        switch (mState.bottomSheetFragmentType.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                switch (mState.bottomSheetState) {

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
                        if (mPendingAction.item != null) {
                            loadLocationFragment(mPendingAction.item.getId());
                        }
                        break;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                switch (mState.bottomSheetState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //TODO
                        markPendingActionDone();
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        markPendingActionDone();
                        break;

                    case BottomSheetBehavior.STATE_SETTLING:
                        // Well, it's okay. Just wait.
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        switch (mState.mapMovement) {

                            case MapsFragment.STATE_MOVEMENT_MOVE:
                                // Well, it's okay. Just wait.
                                break;

                            case MapsFragment.STATE_MOVEMENT_IDLE:
                                if (mPendingAction.item != null) {
                                    if (mState.wasCoordinatorMovement) { //TODO Use reason to mark action done if the user move something
                                        mState.wasCoordinatorMovement = false;
                                        openBottomSheet();
                                    }
                                    else {
                                        moveMapToLocation(mPendingAction.item);
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

    private void computeMovementToCluster() {
        switch (mState.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mState.mapMovement) {

                    case MapsFragment.STATE_MOVEMENT_MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case MapsFragment.STATE_MOVEMENT_IDLE:
                        if (mPendingAction.cluster != null) {
                            moveMapToCluster(mPendingAction.cluster);
                        }
                        else {
                            markPendingActionDone();
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint() {
        switch (mState.bottomSheetState) {

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
                switch (mState.mapMovement) {

                    case MapsFragment.STATE_MOVEMENT_IDLE:
                        moveMapToFootprint();
                        break;

                    case MapsFragment.STATE_MOVEMENT_MOVE:
                        markPendingActionDone();
                        break;
                }
                break;
        }
    }

    private void computeMovementToMyLocation() {
        switch (mState.bottomSheetState) {

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
                switch (mState.mapMovement) {

                    case MapsFragment.STATE_MOVEMENT_IDLE:
                        moveMapToMyLocation();
                        break;

                    case MapsFragment.STATE_MOVEMENT_MOVE:
                        markPendingActionDone();
                        break;
                }
                break;
        }
    }

    private void computeBack() {
        switch (mState.bottomSheetState) {

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

    private void computeFiltersOpening() {
        switch (mState.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mState.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        openBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (mState.bottomSheetFragmentType.getFragmentType()) {

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
                switch (mState.bottomSheetFragmentType.getFragmentType()) {

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
                switch (mState.bottomSheetFragmentType.getFragmentType()) {

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
                switch (mState.bottomSheetFragmentType.getFragmentType()) {

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
        Log.d(TAG, "Coordinator -- Action DONE.");
        mPendingAction.type = ACTION_IDLE;
        mPendingAction.cluster = null;
        mPendingAction.item = null;
        mState.wasCoordinatorMovement = false;
    }

    private void stopMovingMap() {
        mState.wasCoordinatorMovement = false;
        mMapFragmentPerformer.stopMoving();
    }

    private void moveMapToCluster(@NonNull Cluster<PartnerItem> cluster) {
        mState.wasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToCluster(cluster);
    }

    private void moveMapToFootprint() {
        mState.wasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToFootprint();
    }

    private void moveMapToMyLocation() {
        mState.wasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToMyLocation();
    }

    private void moveMapToLocation(@NonNull PartnerItem item) {
        mState.wasCoordinatorMovement = true;
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

    private void loadLocationFragment(long locationId) {
        mFragmentLoader.loadLocationFragment(locationId);
    }

    private void unloadFragment() {
        mFragmentLoader.unloadFragment();
    }

    private void wtf() {
        //TODO log exception, just in case
    }
}
