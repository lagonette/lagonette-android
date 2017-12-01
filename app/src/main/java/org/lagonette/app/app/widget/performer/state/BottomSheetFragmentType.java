package org.lagonette.app.app.widget.performer.state;

import android.support.annotation.IntDef;

import org.lagonette.app.room.statement.Statement;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BottomSheetFragmentType {

    public static final int FRAGMENT_NONE = 0;

    public static final int FRAGMENT_FILTERS = 1;

    public static final int FRAGMENT_LOCATION = 2;

    @Retention(SOURCE)
    @IntDef({
            FRAGMENT_NONE,
            FRAGMENT_FILTERS,
            FRAGMENT_LOCATION
    })
    public @interface FragmentType {

    }

    public static BottomSheetFragmentType none() {
        return new BottomSheetFragmentType(FRAGMENT_NONE);
    }

    public static BottomSheetFragmentType filters() {
        return new BottomSheetFragmentType(FRAGMENT_FILTERS);
    }

    public static BottomSheetFragmentType location(long locationId) {
        return new BottomSheetFragmentType(FRAGMENT_LOCATION, locationId);
    }

    private long mLocationId;

    @FragmentType
    private int mFragmentType;

    private BottomSheetFragmentType(@FragmentType int fragmentType, long locationId) {
        mLocationId = locationId;
        mFragmentType = fragmentType;
    }

    private BottomSheetFragmentType(@FragmentType int fragmentType) {
        mLocationId = Statement.NO_ID;
        mFragmentType = fragmentType;
    }

    public void setLocationId(long locationId) {
        mLocationId = locationId;
    }

    public long getLocationId() {
        return mLocationId;
    }

    @FragmentType
    public int getFragmentType() {
        return mFragmentType;
    }

    public boolean isNone() {
        return mFragmentType == FRAGMENT_NONE;
    }

    public boolean isFilters() {
        return mFragmentType == FRAGMENT_FILTERS;
    }

    public boolean isLocation() {
        return mFragmentType == FRAGMENT_LOCATION;
    }

    @Override
    public String toString() {
        return mFragmentType + " with id " + mLocationId;
    }
}
