package org.lagonette.app.app.widget.performer.state;

import org.lagonette.app.room.statement.Statement;

public class BottomSheetFragmentState {

    private long mLoadedLocationId = Statement.NO_ID;

    private boolean mIsFiltersLoaded = false;

    public void setLocationId(long locationId) {
        mLoadedLocationId = locationId;
    }

    public void clearLocationId() {
        mLoadedLocationId = Statement.NO_ID;
    }

    public void setIsFiltersLoaded(boolean isFiltersLoaded) {
        mIsFiltersLoaded = isFiltersLoaded;
    }

    public long getLocationId() {
        return mLoadedLocationId;
    }

    public boolean isClear() {
        return !isFiltersLoaded() && !isLocationDetailLoaded();
    }

    public boolean isFiltersLoaded() {
        return mIsFiltersLoaded;
    }

    public boolean isLocationDetailLoaded() {
        return mLoadedLocationId > Statement.NO_ID;
    }

    public boolean isLocationDetailLoaded(long locationId) {
        return mLoadedLocationId == locationId;
    }

    public boolean areAllLoaded() {
        return isLocationDetailLoaded() && isFiltersLoaded();
    }

}
