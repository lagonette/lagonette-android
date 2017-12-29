package org.lagonette.app.app.widget.performer.base;

import android.arch.lifecycle.MutableLiveData;
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

    @NonNull
    private final MutableLiveData<Integer> mTopPadding;

    @IdRes
    private final int mLocationDetailContainerRes;

    public LocationDetailFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int locationDetailContainerRed) {
        mFragmentManager = activity.getSupportFragmentManager();
        mLocationDetailContainerRes = locationDetailContainerRed;
        mTopPadding = new MutableLiveData<>();
    }

    @Override
    public void inject(@NonNull View view) {
        // Do nothing.
    }

    public void restoreFragment() {
        mFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
        if (mFragment != null) {
            mTopPadding.observe(
                    mFragment,
                    mFragment::updateTopPadding
            );
        }
    }

    public void unloadFragment() {
        if (mFragment != null) {
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

    public void loadFragment(long locationId) {
        mFragment = LocationDetailFragment.newInstance(locationId);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (isLoaded()) {
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

        mTopPadding.observe(
                mFragment,
                mFragment::updateTopPadding
        );

        if (mFragmentLoadedCommand != null) {
            mFragmentLoadedCommand.onLocationLoaded(locationId);
        }
    }

    public void updateTopPadding(int topPadding) {
        mTopPadding.setValue(topPadding);
    }

    public void onFragmentLoaded(@Nullable FragmentLoadedCommand command) {
        mFragmentLoadedCommand = command;
    }

    public void onFragmentUnloaded(@Nullable FragmentUnloadedCommand command) {
        mFragmentUnloadedCommand = command;
    }
}
