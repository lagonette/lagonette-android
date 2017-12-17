package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.LocationDetailFragment;

public class LocationDetailFragmentPerformer
        implements BottomSheetPerformer.Slideable {

    public interface Observer {

        void notifyUnload();

        void notifyLocationLoaded(long locationId);

    }

    @Nullable
    private LocationDetailFragment mFragment;

    private Observer mObserver;

    @NonNull
    private FragmentManager mFragmentManager;

    @IdRes
    private final int mLocationDetailContainerRes;

    public LocationDetailFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int locationDetailContainerRed) {
        mFragmentManager = activity.getSupportFragmentManager();
        mLocationDetailContainerRes = locationDetailContainerRed;
    }

    public void restore() {
        mFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
    }

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

    public boolean isLoaded() {
        return mFragment != null;
    }

    public void loadFragment(long locationId, boolean animation) {
        if (mFragmentManager != null) {
            mFragment = LocationDetailFragment.newInstance(locationId);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (animation) {
                transaction.setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            }

            transaction.replace(
                    mLocationDetailContainerRes,
                    mFragment,
                    LocationDetailFragment.TAG
            );

            transaction.commit();

            if (mObserver != null) {
                mObserver.notifyLocationLoaded(locationId);
            }
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

    @Override
    public void updateTopPadding(int topPadding) {
        if (mFragment != null) {
            mFragment.updateTopPadding(topPadding);
        }
    }
}
