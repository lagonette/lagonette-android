package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class MainStateLiveData
        extends LiveData<MainState>
        implements BottomSheetFragmentPerformer.Observer {

    private static final String TAG = "MainCoordinatorStateLiv";

    @NonNull
    private MainState mState;

    @Nullable
    private BottomSheetFragmentType mNone;

    @Nullable
    private BottomSheetFragmentType mFilters;

    @Nullable
    private BottomSheetFragmentType mLocation;

    public MainStateLiveData(@NonNull MainState state) {
        mState = state;
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

    public void notifyBottomSheetFragmentChanged(@NonNull BottomSheetFragmentType newFragmentType) {
        Log.d(TAG, "State <- Bottom sheet fragment " + newFragmentType);
        mState.bottomSheetFragmentType = newFragmentType;
        setValue(mState);
    }

    //TODO extract BottomSheetFragmentType in its own custom LiveData and do not implements BottomSheetFragmentPerformer.Observer
    @Override
    public void notifyUnload() {
        notifyBottomSheetFragmentChanged(getNone());
    }

    @Override
    public void notifyFiltersLoaded() {
        notifyBottomSheetFragmentChanged(getFilters());
    }

    @Override
    public void notifyLocationLoaded(long locationId) {
        notifyBottomSheetFragmentChanged(getLocation(locationId));
    }

    @NonNull
    private BottomSheetFragmentType getNone() {
        if (mNone == null) {
            mNone = BottomSheetFragmentType.none();
        }
        return mNone;
    }

    @NonNull
    private BottomSheetFragmentType getFilters() {
        if (mFilters == null) {
            mFilters = BottomSheetFragmentType.filters();
        }
        return mFilters;
    }

    @NonNull
    private BottomSheetFragmentType getLocation(long locationId) {
        if (mLocation == null) {
            mLocation = BottomSheetFragmentType.location(locationId);
        }
        else {
            mLocation.setLocationId(locationId);
        }
        return mLocation;
    }
}
