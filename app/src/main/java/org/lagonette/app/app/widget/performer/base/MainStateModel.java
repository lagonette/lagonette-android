package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.tools.functions.Producer;

public class MainStateModel {

    private static final String TAG = "MainStateModel";

    @NonNull
    public Consumer<MainState> onStateChanged = NullFunctions::doNothing;

    public Producer<MainState> initState;

    private MainState mState;

    public MainState getState() {
        if (mState == null) {
            mState = initState.get();
        }
        return mState;
    }

    public void notifyAction(@Nullable MainAction action) {
        Log.d(TAG, "STATE <-- Action: " + (action != null ? action.type : null));
        mState = getState().action(action);
        notifyStateChanged();
    }

    public void notifyMapMovement(@NonNull MainState.MapMovement mapMovement) {
        Log.d(TAG, "STATE <-- MapMovement: " + mapMovement);
        mState = getState().mapMovement(mapMovement);
        notifyStateChanged();
    }

    public void notifyBottomSheetState(@BottomSheetBehavior.State int bottomSheetState) {
        Log.d(TAG, "STATE <-- BottomSheetState: " + bottomSheetState);
        mState = getState().bottomSheetState(bottomSheetState);
        notifyStateChanged();
    }

    public void notifyFiltersLoading(boolean isFiltersLoaded) {
        Log.d(TAG, "STATE <-- FiltersLoading: "+ isFiltersLoaded);
        mState = getState().filtersLoading(isFiltersLoaded);
        notifyStateChanged();
    }

    public void notifyLocationDetailLoading(boolean isLocationDetailLoaded, long loadedLocationId) {
        Log.d(TAG, "STATE <-- LocationDetailLoading: " + isLocationDetailLoaded + " id: " + loadedLocationId);
        mState = getState().locationDetailLoading(isLocationDetailLoaded, loadedLocationId);
        notifyStateChanged();
    }

    public void notifyLocationDetailLoading(boolean isLocationDetailLoaded) {
        Log.d(TAG, "STATE <-- LocationDetailLoading: ");
        mState = getState().locationDetailLoading(isLocationDetailLoaded);
        notifyStateChanged();
    }

    public void notifyLoadedLocationId(long loadedLocationId) {
        Log.d(TAG, "STATE <-- LoadedLocationId: " + loadedLocationId);
        mState = getState().loadedLocationId(loadedLocationId);
        notifyStateChanged();
    }

    private void notifyStateChanged() {
        onStateChanged.accept(mState);
    }
}
