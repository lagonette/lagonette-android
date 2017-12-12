package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class BottomSheetFragmentTypeLiveData
        extends LiveData<BottomSheetFragmentType>
        implements BottomSheetPerformer.FragmentObserver {

    //TODO Do not implement BottomSheetPerformer.FragmentObserver

    @Nullable
    private BottomSheetFragmentType mNone;

    @Nullable
    private BottomSheetFragmentType mFilters;

    @Nullable
    private BottomSheetFragmentType mLocation;

    @Override
    public void onUnload() {
        super.setValue(getNone());
    }

    @Override
    public void onFiltersLoaded() {
        super.setValue(getFilters());
    }

    @Override
    public void onLocationLoaded(long locationId) {
        super.setValue(getLocation(locationId));
    }

    @NonNull
    public BottomSheetFragmentType getNone() {
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

    @NonNull
    @Override
    public BottomSheetFragmentType getValue() {
        BottomSheetFragmentType type = super.getValue();
        if (type == null) {
            type = getNone();
        }
        return type;
    }
}
