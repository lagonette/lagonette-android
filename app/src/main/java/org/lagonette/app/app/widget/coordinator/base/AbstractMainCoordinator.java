package org.lagonette.app.app.widget.coordinator.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.app.app.viewmodel.MainActionViewModel;
import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;

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
    public void process(@NonNull MainState state) {
        if (state.action != null) {
            Log.d(TAG, "Action -> " + state.action.type.name());
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
                // Wait.
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (state.mapMovement) {

                    case IDLE:
                        mMap.moveToMyLocation();
                        break;

                    case MOVE:
                        mAction.finish(action);
                        break;
                }
                break;
        }
    }

    private void computeIdle(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                unloadBottomSheetFragment(state);
                break;
        }
    }

    protected abstract void unloadBottomSheetFragment(@NonNull MainState state);

    private void computeFullMapShowing(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                // Well, just wait.
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                        mAction.finish(action);
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                        mAction.finish(action);
                break;
        }
    }

    protected abstract void computeMovementToAndOpeningLocation(@NonNull MainAction action, @NonNull MainState state);

    private void computeMovementToCluster(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                // Well, it's okay. Just wait.
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (state.mapMovement) {

                    case MOVE:
                        // Well, it's okay. Just wait.
                        break;

                    case IDLE:
                        if (action.cluster != null && action.shouldMove) {
                            action.shouldMove = false;
                            mMap.moveToCluster(action.cluster);
                        } else {
                            mAction.finish(action);
                        }
                        break;
                }
                break;
        }
    }

    private void computeMovementToFootprint(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

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
                switch (state.mapMovement) {

                    case IDLE:
                        mMap.moveToFootprint();
                        break;

                    case MOVE:
                        mAction.finish(action);
                        break;
                }
                break;
        }
    }

    private void computeBack(@NonNull MainAction action, @NonNull MainState state) {
        switch (state.bottomSheetState) {

            case BottomSheetBehavior.STATE_SETTLING:
                // Do nothing
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheet.closeBottomSheet();
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                        mAction.finish(action);
                break;
        }
    }

    protected void wtf(@NonNull MainState state) {
        FirebaseCrash.logcat(Log.ERROR, TAG, state.toString());
        FirebaseCrash.report(new IllegalArgumentException("Coordinator received a weird state"));
    }

}
