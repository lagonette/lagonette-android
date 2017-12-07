package org.lagonette.app.app.widget.performer;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.widget.coordinator.base.MainCoordinator;
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
    private LocationDetailFragment mLocationDetailFragment;

    @Nullable
    private FiltersFragment mFiltersFragment;

    private Observer mObserver;

    @Nullable
    private FragmentManager mFragmentManager;

    @IdRes
    private final int mFiltersContainerRes;

    @IdRes
    private final int mLocationDetailContainerRes;

    @NonNull
    private Padding mPadding;

    public BottomSheetFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @NonNull Resources resources,
            @IdRes int filtersContainerRes,
            @IdRes int locationDetailContainerRed,
            @DimenRes int searchBarHeightRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFiltersContainerRes = filtersContainerRes;
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

        mFiltersFragment = (FiltersFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
        mLocationDetailFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);

        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
                if (mFiltersFragment != null) {
                    mFragmentManager
                            .beginTransaction()
                            .remove(mFiltersFragment)
                            .commit();
                    mFiltersFragment = null;
                }

                if (mLocationDetailFragment != null) {
                    mFragmentManager
                            .beginTransaction()
                            .remove(mLocationDetailFragment)
                            .commit();
                    mLocationDetailFragment = null;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                if (mLocationDetailFragment != null) {
                    mFragmentManager
                            .beginTransaction()
                            .remove(mLocationDetailFragment)
                            .commit();
                    mLocationDetailFragment = null;
                }
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                if (mFiltersFragment != null) {
                    mFragmentManager
                            .beginTransaction()
                            .remove(mFiltersFragment)
                            .commit();
                    mFiltersFragment = null;
                }
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
        if (mFragmentManager != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (mFiltersFragment != null) {
                transaction.remove(mFiltersFragment);
                mFiltersFragment = null;
            }

            if (mLocationDetailFragment != null) {
                transaction.remove(mLocationDetailFragment);
                mLocationDetailFragment = null;
            }

            transaction.commit();
        }

        if (mObserver != null) {
            mObserver.notifyUnload();
        }
    }

    @Override
    public void loadFiltersFragment() {
        if (mFragmentManager != null) {
            mFiltersFragment = FiltersFragment.newInstance();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (mLocationDetailFragment != null) {
                transaction.remove(mLocationDetailFragment);
                mLocationDetailFragment = null;
            }

            transaction.add(
                    mFiltersContainerRes,
                    mFiltersFragment,
                    FiltersFragment.TAG
            );

            transaction.commit();

            if (mObserver != null) {
                mObserver.notifyFiltersLoaded();
            }
        }
    }

    @Override
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

            if (mFiltersFragment != null) {
                transaction.remove(mFiltersFragment);
                mFiltersFragment = null;
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
            if (mFiltersFragment != null) {
                mFiltersFragment.updateTopPadding(mPadding.getTop());
            }

            if (mLocationDetailFragment != null) {
                mLocationDetailFragment.updateTopPadding(mPadding.getTop());
            }
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

}
