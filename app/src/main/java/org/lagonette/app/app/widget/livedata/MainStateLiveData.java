package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;

public class MainStateLiveData
        extends LiveData<MainState> {

    private static final String TAG = "MainCoordinatorStateLiv";

    @NonNull
    private MainState mState;

    public MainStateLiveData(@NonNull MainState state) {
        mState = state;
    }

    // TODO Workaround
    @Nullable
    @Override
    public MainState getValue() {
        MainState value = super.getValue();
        if (value == null) {
            value = mState;
        }
        return value;
    }

    public void notifyMapMovementChanged(@MainState.Movement int newMovement) {
        Log.d(TAG, "State <- Map movement " + newMovement);
        mState.mapMovement = newMovement;
        setValue(mState);
    }

    public void notifyBottomSheetStateChanged(@MainState.State int newState) {
        Log.d(TAG, "State <- Bottom sheet state " + newState);
        mState.bottomSheetState = newState;
        setValue(mState);
    }

    public void notifyBottomSheetFragmentChanged(@NonNull BottomSheetFragmentState newFragmentType) {
        Log.d(TAG, "State <- Bottom sheet fragment " + newFragmentType);
        mState.bottomSheetFragmentState = newFragmentType;
        setValue(mState);
    }

}
