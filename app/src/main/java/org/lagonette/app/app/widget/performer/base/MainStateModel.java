package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;

public class MainStateModel {

    public interface OnStateChangedCommand {

        void notifyStateChanged(@NonNull MainState state);
    }

    @Nullable
    private OnStateChangedCommand mOnStateChangedCommand;

    private MainState mState;

    public void initState(@NonNull MainState state) {
        mState = state;
    }

    public MainState getState() {
        return mState;
    }

    public void notifyAction(@NonNull MainAction action) {
        mState = mState.action(action);
        notifyStateChanged();
    }

    public void notifyMapMovement(@NonNull MainState.MapMovement mapMovement) {
        mState = mState.mapMovement(mapMovement);
        notifyStateChanged();
    }

    public void notifyBottomSheetState(@BottomSheetBehavior.State int bottomSheetState) {
        mState = mState.bottomSheetState(bottomSheetState);
        notifyStateChanged();
    }

    public void notifyFiltersLoading(boolean isFiltersLoaded) {
        mState = mState.filtersLoading(isFiltersLoaded);
        notifyStateChanged();
    }

    public void notifyLocationDetailLoading(boolean isLocationDetailLoaded, long loadedLocationId) {
        mState = mState.locationDetailLoading(isLocationDetailLoaded, loadedLocationId);
        notifyStateChanged();
    }

    public void notifyLocationDetailLoading(boolean isLocationDetailLoaded) {
        mState = mState.locationDetailLoading(isLocationDetailLoaded);
        notifyStateChanged();
    }

    public void notifyLoadedLocationId(long loadedLocationId) {
        mState = mState.loadedLocationId(loadedLocationId);
        notifyStateChanged();
    }

    public void onStateChanged(@NonNull OnStateChangedCommand command) {
        mOnStateChangedCommand = command;
    }

    private void notifyStateChanged() {
        if (mOnStateChangedCommand != null) {
            mOnStateChangedCommand.notifyStateChanged(mState);
        }
    }
}
