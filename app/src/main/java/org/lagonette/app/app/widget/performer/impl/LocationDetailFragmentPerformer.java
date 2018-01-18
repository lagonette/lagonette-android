package org.lagonette.app.app.widget.performer.impl;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.app.widget.performer.base.FragmentPerformer;

import java.util.ArrayList;

public class LocationDetailFragmentPerformer implements FragmentPerformer {

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

    @NonNull
    private final ArrayList<FragmentUnloadedCommand> mFragmentUnloadedCommands;

    @NonNull
    private final ArrayList<FragmentLoadedCommand> mFragmentLoadedCommands;

    @NonNull
    private final MutableLiveData<Integer> mTopPadding;

    @NonNull
    private final MutableLiveData<Void> mLoadedNotifier;

    @IdRes
    private final int mLocationDetailContainerRes;

    public LocationDetailFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int locationDetailContainerRed) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentLoadedCommands = new ArrayList<>();
        mFragmentUnloadedCommands = new ArrayList<>();
        mLocationDetailContainerRes = locationDetailContainerRed;
        mTopPadding = new MutableLiveData<>();
        mLoadedNotifier = new MutableLiveData<>();
        mLoadedNotifier.setValue(null);
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

    @Override
    public void unloadFragment() {
        if (mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
        }

        for (FragmentUnloadedCommand command : mFragmentUnloadedCommands) {
            command.onLocationUnloaded();
        }
    }

    public boolean isLoaded() {
        return mFragment != null;
    }

    public boolean isLoaded(long locationId) {
        return isLoaded() && mFragment.isLoaded(locationId);
    }

    public long getLoadedId() {
        return isLoaded() ? mFragment.getLocationId() : Statement.NO_ID;
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


        mLoadedNotifier.observe(
                mFragment,
                aVoid -> {
                    for (FragmentLoadedCommand command : mFragmentLoadedCommands) {
                        command.onLocationLoaded(locationId);
                    }
                }
        );
    }

    public void updateTopPadding(int topPadding) {
        mTopPadding.setValue(topPadding);
    }

    public void onFragmentLoaded(@Nullable FragmentLoadedCommand command) {
        mFragmentLoadedCommands.add(command);
    }

    //TODO make one single command for loaded & unloaded notif'
    public void onFragmentUnloaded(@Nullable FragmentUnloadedCommand command) {
        mFragmentUnloadedCommands.add(command);
    }
}
