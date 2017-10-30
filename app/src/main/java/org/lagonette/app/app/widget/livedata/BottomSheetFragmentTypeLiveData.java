package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class BottomSheetFragmentTypeLiveData
        extends LiveData<BottomSheetFragmentType>
        implements BottomSheetFragmentPerformer.Observer {

    @Nullable
    private BottomSheetFragmentType mNone;

    @Nullable
    private BottomSheetFragmentType mFilters;

    @Nullable
    private BottomSheetFragmentType mLocation;

    @Override
    public void notifyUnload() {
        super.setValue(getNone());
    }

    @Override
    public void notifyFiltersLoaded() {
        super.setValue(getFilters());
    }

    @Override
    public void notifyLocationLoaded(long locationId) {
        super.setValue(getLocation(locationId));
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
        return mLocation;
    }
}
