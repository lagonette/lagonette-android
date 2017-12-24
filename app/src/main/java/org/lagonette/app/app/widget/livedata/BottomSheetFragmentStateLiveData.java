package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;

public class BottomSheetFragmentStateLiveData
        extends LiveData<BottomSheetFragmentState> {

    @NonNull
    private final BottomSheetFragmentState mBottomSheetFragmentState;

    public BottomSheetFragmentStateLiveData() {
        mBottomSheetFragmentState = new BottomSheetFragmentState();
    }

    @NonNull
    @Override
    public BottomSheetFragmentState getValue() {
        BottomSheetFragmentState type = super.getValue();
        if (type == null) {
            type = mBottomSheetFragmentState;
        }
        return type;
    }

    public void notifyFiltersLoaded() {
        mBottomSheetFragmentState.setIsFiltersLoaded(true);
        setValue(mBottomSheetFragmentState);
    }

    public void notifyFiltersUnloaded() {
        mBottomSheetFragmentState.setIsFiltersLoaded(false);
        setValue(mBottomSheetFragmentState);
    }

    public void notifyLocationDetailLoaded(long locationId) {
        mBottomSheetFragmentState.setLocationId(locationId);
        setValue(mBottomSheetFragmentState);
    }

    public void notifyLocationDetailUnloaded() {
        mBottomSheetFragmentState.clearLocationId();
        setValue(mBottomSheetFragmentState);
    }
}
