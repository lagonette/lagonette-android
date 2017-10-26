package org.lagonette.app.app.widget.coordinator;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentManager;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.MapFragmentPerformer;
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
    private @interface Action {

    }

    @NonNull
    private final BottomSheetCallback mBottomSheetCallback;

    @NonNull
    private final FragmentLoader mFragmentLoader;

    @NonNull
    private final MapFragmentPerformer mMapFragmentPerformer;

    @Action
    private int mPendingAction;

    @Nullable
    private Cluster<PartnerItem> mPendingCluster;

    @Nullable
    private PartnerItem mPendingItem;

    @BottomSheetPerformer.State
    private int mBottomSheetState;

    @BottomSheetFragmentManager.FragmentType
    private int mBottomSheetFragment;

    @MapsFragment.Movement
    private int mMapMovement;

    private boolean mWasCoordinatorMovement;

    public MainCoordinator(
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        mBottomSheetCallback = bottomSheetCallback;
        mFragmentLoader = fragmentLoader;
        mMapFragmentPerformer = mapFragmentPerformer;
        mPendingAction = ACTION_IDLE;
        mBottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
        mBottomSheetFragment = BottomSheetFragmentManager.FRAGMENT_NONE;
        mMapMovement = MapsFragment.STATE_MOVEMENT_IDLE;
    }

    public void openFilters() {
        Log.d(TAG, "Coordinator -> Action: OPEN FILTERS");
        mPendingAction = ACTION_OPEN_FILTERS;
        computeFiltersOpening();
    }

    public boolean back() {
        Log.d(TAG, "Coordinator -> Action: BACK");
        switch (mBottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                mPendingAction = ACTION_BACK;
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
        mPendingAction = ACTION_MOVE_TO_MY_LOCATION;
        computeMovementToMyLocation();
    }

    public void moveToFootprint() {
        Log.d(TAG, "Coordinator -> Action: MOVE ON FOOTPRINT");
        mPendingAction = ACTION_MOVE_TO_FOOTPRINT;
        computeMovementToFootprint();
    }

    public void moveToCluster(@NonNull Cluster<PartnerItem> cluster) {
        Log.d(TAG, "Coordinator -> Action: MOVE ON CLUSTER");
        mPendingAction = ACTION_MOVE_TO_CLUSTER;
        mPendingCluster = cluster;
        computeMovementToCluster();
    }

    public void moveToLocation(@NonNull PartnerItem item) {
        Log.d(TAG, "Coordinator -> Action: MOVE ON PARTNER ITEM");
        mPendingAction = ACTION_MOVE_TO_LOCATION;
        mPendingItem = item;
        computeMovementToLocation();
    }

    public void notifyMapMovementChanged(@MapsFragment.Movement int newMovement) {
        Log.d(TAG, "Coordinator <- Notification: Map movement " + newMovement);
        mMapMovement = newMovement;
        dispatchAction();
    }

    public void notifyBottomSheetStateChanged(@BottomSheetPerformer.State int newState) {
        Log.d(TAG, "Coordinator <- Notification: Bottom sheet state " + newState);
        mBottomSheetState = newState;
        dispatchAction();
    }

    public void notifyBottomSheetFragmentChanged(@BottomSheetFragmentManager.FragmentType int newFragment) {
        Log.d(TAG, "Coordinator <- Notification: Bottom sheet fragment " + newFragment);
        mBottomSheetFragment = newFragment;
        dispatchAction();
    }

    private void dispatchAction() {
        switch (mPendingAction) {

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
        switch (mBottomSheetFragment) {

            case BottomSheetFragmentManager.FRAGMENT_NONE:
            case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                switch (mBottomSheetState) {

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
                        if (mPendingItem != null) {
                            loadLocationFragment(mPendingItem.getId());
                        }
                        break;
                }
                break;

            case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                switch (mBottomSheetState) {

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
                        switch (mMapMovement) {

                            case MapsFragment.STATE_MOVEMENT_MOVE:
                                // Well, it's okay. Just wait.
                                break;

                            case MapsFragment.STATE_MOVEMENT_IDLE:
                                if (mPendingItem != null) {
                                    if (mWasCoordinatorMovement) { //TODO Use reason to mark action done if the user move something
                                        mWasCoordinatorMovement = false;
                                        openBottomSheet();
                                    }
                                    else {
                                        moveMapToLocation(mPendingItem);
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
        switch (mBottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetCallback.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMapMovement) {

                    case MapsFragment.STATE_MOVEMENT_MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case MapsFragment.STATE_MOVEMENT_IDLE:
                        if (mPendingCluster != null) {
                            moveMapToCluster(mPendingCluster);
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
        switch (mBottomSheetState) {

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
                switch (mMapMovement) {

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
        switch (mBottomSheetState) {

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
                switch (mMapMovement) {

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
        switch (mBottomSheetState) {

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
        switch (mBottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        openBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;
                }
                break;
        }
    }

    private void markPendingActionDone() {
        Log.d(TAG, "Coordinator -- Action DONE.");
        mPendingAction = ACTION_IDLE;
        mPendingCluster = null;
        mPendingItem = null;
        mWasCoordinatorMovement = false;
    }

    private void stopMovingMap() {
        mWasCoordinatorMovement = false;
        mMapFragmentPerformer.stopMoving();
    }

    private void moveMapToCluster(@NonNull Cluster<PartnerItem> cluster) {
        mWasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToCluster(cluster);
    }

    private void moveMapToFootprint() {
        mWasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToFootprint();
    }

    private void moveMapToMyLocation() {
        mWasCoordinatorMovement = true;
        mMapFragmentPerformer.moveToMyLocation();
    }

    private void moveMapToLocation(@NonNull PartnerItem item) {
        mWasCoordinatorMovement = true;
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
