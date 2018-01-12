package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.app.viewmodel.MainActionViewModel;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public abstract class AbstractMainCoordinator implements MainCoordinator {

    private static final String TAG = "AbstractMainCoordinator";

    @NonNull
    protected final MainActionViewModel mAction;

    @NonNull
    protected final BottomSheetPerformer mBottomSheet;

    @NonNull
    protected final FiltersFragmentPerformer mFilters;

    @NonNull
    protected final LocationDetailFragmentPerformer mLocationDetail;

    @NonNull
    protected final MapFragmentPerformer mMap;

    public AbstractMainCoordinator(
            @NonNull MainActionViewModel mainActionViewModel,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull FiltersFragmentPerformer filtersFragmentPerformer,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        mBottomSheet = bottomSheetPerformer;
        mFilters = filtersFragmentPerformer;
        mLocationDetail = locationDetailFragmentPerformer;
        mMap = mapFragmentPerformer;
        mAction = mainActionViewModel;
    }

    @Override
    @CallSuper
    public void init() {
        mBottomSheet.closeBottomSheet();
        mMap.loadFragment();
    }

    @Override
    @CallSuper
    public void restore() {
        mMap.restoreFragment();
        mFilters.restoreFragment();
        mLocationDetail.restoreFragment();
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
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_SETTLING:
            case BottomSheetBehavior.STATE_DRAGGING:
                // Wait.
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMap.getMapMovement()) {

                    case IDLE:
                        mMap.moveToMyLocation();
                        break;

                    case MOVE:
                        mAction.markDone();
                        break;
                }
                break;
        }
    }

    private void computeIdle(@NonNull MainAction action) {
        switch (mBottomSheet.getState()) {

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
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                // Well, just wait.
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                        mAction.markDone();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                        mAction.markDone();
                break;
        }
    }

    protected abstract void computeMovementToAndOpeningLocation(@NonNull MainAction action);

    private void computeMovementToCluster(@NonNull MainAction action) {
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMap.getMapMovement()) {

                    case MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case IDLE:
                        if (action.cluster != null && action.shouldMove) {
                            action.shouldMove = false;
                            mMap.moveToCluster(action.cluster);
                        } else {
                        mAction.markDone();
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainAction action) {
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_SETTLING:
                mMap.stopMoving();
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                mMap.stopMoving();
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mMap.getMapMovement()) {

                    case IDLE:
                        mMap.moveToFootprint();
                        break;

                    case MOVE:
                        mAction.markDone();
                        break;
                }
                break;
        }
    }

    private void computeBack(@NonNull MainAction action) {
        switch (mBottomSheet.getState()) {

            case BottomSheetBehavior.STATE_SETTLING:
                // Do nothing
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                        mAction.markDone();
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
        switch (mMap.getMapMovement()) {
            case IDLE:
                string += "IDLE\n";
                break;
            case MOVE:
                string += "MOVE\n";
                break;
        }
        string += "\tBottom sheet state: ";
        switch (mBottomSheet.getState()) {
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
        string += "\t\tis filters loaded: " + mFilters.isLoaded() + "\n";
        string += "\t\tis location detail loaded: " + mLocationDetail.isLoaded() + "\n";
        string += "\t]\n";
        string += "]";
        return string;
    }

}
