package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.fragment.LocationDetailFragment;

public class LocationDetailFragmentPerformer implements Performer {

    public interface FragmentLoadedCommand {

        void onLocationLoaded(long locationId);
    }

    public interface FragmentUnloadedCommand {

        void onLocationUnloaded();
    }

    @Nullable
    private LocationDetailFragment mFragment;

    @NonNull
    private FragmentManager mFragmentManager;

    @Nullable
    private FragmentUnloadedCommand mFragmentUnloadedCommand;

    @Nullable
    private FragmentLoadedCommand mFragmentLoadedCommand;

    @IdRes
    private final int mLocationDetailContainerRes;

    public LocationDetailFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int locationDetailContainerRed) {
        mFragmentManager = activity.getSupportFragmentManager();
        mLocationDetailContainerRes = locationDetailContainerRed;
    }

    @Override
    public void inject(@NonNull View view) {
        // Do nothing.
    }

    public void restoreFragment() {
        mFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
    }

    public void unloadFragment() {
        if (mFragmentManager != null && mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
        }

        if (mFragmentUnloadedCommand != null) {
            mFragmentUnloadedCommand.onLocationUnloaded();
        }
    }

    public boolean isLoaded() {
        return mFragment != null;
    }

    //TODO Remove animation boolean
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

            if (mFragmentLoadedCommand != null) {
                mFragmentLoadedCommand.onLocationLoaded(locationId);
            }
        }
    }

    public void updateTopPadding(int topPadding) {
        if (mFragment != null) {
            mFragment.updateTopPadding(topPadding);
        }
    }

    public void onFragmentLoaded(@Nullable FragmentLoadedCommand command) {
        mFragmentLoadedCommand = command;
    }

    public void onFragmentUnloaded(@Nullable FragmentUnloadedCommand command) {
        mFragmentUnloadedCommand = command;
    }
}
