package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public abstract class AbstractMainCoordinator implements MainCoordinator {

    private static final String TAG = "AbstractMainCoordinator";

    @NonNull
    protected final DoneMarker mDoneMarker;

    @NonNull
    protected final BottomSheetPerformer mBottomSheetPerformer;

    @NonNull
    protected final FiltersFragmentPerformer mFiltersPerformer;

    @NonNull
    protected final LocationDetailFragmentPerformer mLocationDetailPerformer;

    @NonNull
    protected final MapFragmentPerformer mMapFragmentPerformer;

    public AbstractMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
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
    public void restore() {
        mMapFragmentPerformer.restoreFragment();
        mFiltersPerformer.restoreFragment();
        mLocationDetailPerformer.restoreFragment();
    }

    @Override
    @CallSuper
    public void process(@NonNull MainAction action) {
        switch (action.type) {

            case BACK:
                computeBack(action);
                break;

            case OPEN_FILTERS:
                computeFiltersOpening(action);
                break;

            case MOVE_TO_MY_LOCATION:
                computeMovementToMyLocation(action);
                break;

            case MOVE_TO_FOOTPRINT:
                computeMovementToFootprint(action);
                break;

            case MOVE_TO_CLUSTER:
                computeMovementToCluster(action);
                break;

            case MOVE_TO_AND_OPEN_LOCATION:
                computeMovementToAndOpeningLocation(action);
                break;

            case SHOW_FULL_MAP:
                computeFullMapShowing(action);
                break;

            default:
            case IDLE:
                computeIdle(action);
                break;
        }
    }

    protected abstract void computeFiltersOpening(@NonNull MainAction action);

    private void computeMovementToMyLocation(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_SETTLING:
            case BottomSheetBehavior.STATE_DRAGGING:
                // Wait.
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMapFragmentPerformer.getMapMovement()) {

                    case IDLE:
                        mMapFragmentPerformer.moveToMyLocation();
                        break;

                    case MOVE:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;
        }
    }

    private void computeIdle(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                unloadBottomSheetFragment();
                break;
        }
    }

    protected abstract void unloadBottomSheetFragment();

    private void computeFullMapShowing(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

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

    protected abstract void computeMovementToAndOpeningLocation(@NonNull MainAction action);

    private void computeMovementToCluster(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetPerformer.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMapFragmentPerformer.getMapMovement()) {

                    case MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case IDLE:
                        if (action.cluster != null && action.shouldMove) {
                            action.shouldMove = false;
                            mMapFragmentPerformer.moveToCluster(action.cluster);
                        } else {
                            mDoneMarker.markPendingActionAsDone();
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

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
                switch (mMapFragmentPerformer.getMapMovement()) {

                    case IDLE:
                        mMapFragmentPerformer.moveToFootprint();
                        break;

                    case MOVE:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;
        }
    }

    private void computeBack(@NonNull MainAction action) {
        switch (mBottomSheetPerformer.getState()) {

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

    protected void wtf() {
        FirebaseCrash.logcat(Log.ERROR, TAG, getStateLog());
        FirebaseCrash.report(new IllegalArgumentException("Coordinator received a weird state"));
    }

    public String getStateLog() {
        String string = "MainState: [\n";
        string += "\tMap movement: ";
        switch (mMapFragmentPerformer.getMapMovement()) {
            case IDLE:
                string += "IDLE\n";
                break;
            case MOVE:
                string += "MOVE\n";
                break;
        }
        string += "\tBottom sheet state: ";
        switch (mBottomSheetPerformer.getState()) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                string += "STATE_COLLAPSED\n";
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                string += "STATE_DRAGGING\n";
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                string += "STATE_EXPANDED\n";
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                string += "STATE_HIDDEN\n";
                break;
            case BottomSheetBehavior.STATE_SETTLING:
                string += "STATE_SETTLING\n";
                break;
        }
        string += "\tBottom sheet fragment state: [\n";
        string += "\t\tis filters loaded: " + mFiltersPerformer.isLoaded() + "\n";
        string += "\t\tis location detail loaded: " + mLocationDetailPerformer.isLoaded() + "\n";
        string += "\t]\n";
        string += "]";
        return string;
    }

}
