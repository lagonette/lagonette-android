package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;

public class UiStateStore extends ViewModel {

    private final MutableLiveData<MainState> mStateLiveData;

    public UiStateStore() {
        mStateLiveData = new MutableLiveData<>();
    }

    public LiveData<MainState> getState() {
        return mStateLiveData;
    }

    public void setState(@NonNull MainState state) {
        mStateLiveData.setValue(state);
    }

    public void notifyBottomSheetStateChanged(@MainState.BottomSheetState int state) {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setBottomSheetState(state)
                        .build()
        );
    }

    public void notifyLocationIdLoaded(long locationId) {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setLocationDetailLoaded(true)
                        .setLoadedLocationId(locationId)
                        .build()
        );
    }

    public void notifyLocationDetailUnload() {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setLocationDetailLoaded(false)
                        .clearLoadedLocationId()
                        .build()
        );
    }

    public void notifyFiltersLoading(boolean isFilterLoaded) {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setFiltersLoaded(isFilterLoaded)
                        .build()
        );
    }

    public void notifyMapMovement(@NonNull MainState.MapMovement mapMovement) {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setMapMovement(mapMovement)
                        .build()
        );
    }

    public void notifyActionIsFinished() {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setAction(null)
                        .build()
        );
    }

    public void startAction(@NonNull MainAction action) {
        mStateLiveData.setValue(
                MainState.build(mStateLiveData.getValue())
                        .setAction(action)
                        .build()
        );
    }
}
