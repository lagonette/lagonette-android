package org.lagonette.app.app.widget.performer;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.fragment.SlideableFragment;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;
import org.lagonette.app.util.UiUtil;

public class BottomSheetFragmentPerformer
        implements MainCoordinator.FragmentLoader {

    public interface Observer {

        void notifyUnload();

        void notifyFiltersLoaded();

        void notifyLocationLoaded(long locationId);

    }

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
    private SlideableFragment mFragment;

    private Observer mObserver;

    @Nullable
    private FragmentManager mFragmentManager;

    @NonNull
    private Padding mPadding;

    public BottomSheetFragmentPerformer(@NonNull AppCompatActivity activity, @NonNull Resources resources, @DimenRes int searchBarHeightRes) {
        mPadding = new Padding();
        mFragmentManager = activity.getSupportFragmentManager();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(resources);
        mPadding.searchBarHeight = resources.getDimensionPixelOffset(searchBarHeightRes);
        mPadding.searchBarOffset = 0;
        mPadding.bottomSheetTop = 0;
    }

    public void init(@NonNull BottomSheetFragmentType type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        loadFragment(type);
    }

    public void restore(@NonNull BottomSheetFragmentType type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
                mFragment = null;
                break;

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                mFragment = (SlideableFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                mFragment = (SlideableFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
                break;
        }
    }

    private void loadFragment(@NonNull BottomSheetFragmentType type) {
        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
                unloadFragment();
                break;

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                loadFiltersFragment();
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                loadLocationFragment(type.getLocationId(), false);
                break;
        }
    }

    @Override
    public void unloadFragment() {
        if (mFragmentManager != null && mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
        }

        if (mObserver != null) {
            mObserver.notifyUnload();
        }
    }

    @Override
    public void loadFiltersFragment() {
        if (mFragmentManager != null) {
            mFragment = FiltersFragment.newInstance();
            mFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            FiltersFragment.TAG
                    )
                    .commit();

            if (mObserver != null) {
                mObserver.notifyFiltersLoaded();
            }
        }
    }

    @Override
    public void loadLocationFragment(long locationId, boolean animation) {
        if (mFragmentManager != null) {
            mFragment = LocationDetailFragment.newInstance(locationId);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (animation) {
                transaction.setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            }
            transaction
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            LocationDetailFragment.TAG
                    )
                    .commit();

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
        if (mFragment != null && mPadding.updateTop()) {
            mFragment.updateTopPadding(mPadding.getTop());
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

}
