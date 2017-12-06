package org.lagonette.app.app.widget.performer.landscape;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;
import org.lagonette.app.util.UiUtil;

public class LandscapeBottomSheetFragmentPerformer {

    public interface Observer {

        void notifyUnload();

        void notifyLocationLoaded(long locationId);

    }

    //TODO extract Slideable in its own file
    public interface Slideable {

        void updateTopPadding(int topPadding);

    }

    public static class Padding {

        public int bottomSheetTop, searchBarHeight, searchBarOffset, statusBarHeight;

        private int mTopPadding;

        // TODO Do not use searchBarHeight, use an inverted offset or something else
        public boolean updateTop() {
            int limit = statusBarHeight + searchBarHeight + searchBarOffset;
            if (bottomSheetTop <= limit) {
                mTopPadding = limit - bottomSheetTop;
                return true;
            }
            else if (mTopPadding != 0) {
                mTopPadding = 0;
                return true;
            }
            else {
                return false;
            }
        }

        public int getTop() {
            return mTopPadding;
        }
    }

    @Nullable
    private LocationDetailFragment mLocationDetailFragment;

    private Observer mObserver;

    @Nullable
    private FragmentManager mFragmentManager;

    @IdRes
    private final int mLocationDetailContainerRes;

    @NonNull
    private Padding mPadding;

    public LandscapeBottomSheetFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @NonNull Resources resources,
            @IdRes int locationDetailContainerRed,
            @DimenRes int searchBarHeightRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mLocationDetailContainerRes = locationDetailContainerRed;
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(resources);
        mPadding.searchBarHeight = resources.getDimensionPixelOffset(searchBarHeightRes);
        mPadding.searchBarOffset = 0;
        mPadding.bottomSheetTop = 0;
    }

    public void restore(@NonNull BottomSheetFragmentType type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        mLocationDetailFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);

        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
            case BottomSheetFragmentType.FRAGMENT_NONE:
                if (mLocationDetailFragment != null) {
                    mFragmentManager
                            .beginTransaction()
                            .remove(mLocationDetailFragment)
                            .commit();
                    mLocationDetailFragment = null;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                break;
        }
    }

    private void loadFragment(@NonNull BottomSheetFragmentType type) {
        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
            case BottomSheetFragmentType.FRAGMENT_NONE:
                unloadFragment();
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                loadLocationFragment(type.getLocationId(), false);
                break;
        }
    }

    public void unloadFragment() {
        if (mFragmentManager != null && mLocationDetailFragment != null) {
            mFragmentManager.beginTransaction()
                        .remove(mLocationDetailFragment)
                        .commit();
            mLocationDetailFragment = null;
        }

        if (mObserver != null) {
            mObserver.notifyUnload();
        }
    }

    public void loadLocationFragment(long locationId, boolean animation) {
        if (mFragmentManager != null) {
            mLocationDetailFragment = LocationDetailFragment.newInstance(locationId);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (animation) {
                transaction.setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            }

            transaction.replace(
                    mLocationDetailContainerRes,
                    mLocationDetailFragment,
                    LocationDetailFragment.TAG
            );

            transaction.commit();

            if (mObserver != null) {
                mObserver.notifyLocationLoaded(locationId);
            }
        }
    }

    public void notifySearchBarOffsetChanged(int searchBarOffset) {
        mPadding.searchBarOffset = searchBarOffset;
        updateBottomSheetTopPadding();
    }

    public void notifyBottomSheetSlide(int top) {
        mPadding.bottomSheetTop = top;
        updateBottomSheetTopPadding();
    }

    private void updateBottomSheetTopPadding() {
        if (mPadding.updateTop()) {
            if (mLocationDetailFragment != null) {
                mLocationDetailFragment.updateTopPadding(mPadding.getTop());
            }
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

}
