package org.lagonette.app.app.widget.performer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.MapsFragment;

public class MapFragmentPerformer {

    private MapsFragment mFragment;

    @Nullable
    private FragmentManager mFragmentManager;

    public void inject(@NonNull AppCompatActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void init() {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        mFragment = MapsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mFragment, MapsFragment.TAG)
                .commit();
    }

    public void restore() {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        mFragment = (MapsFragment) mFragmentManager.findFragmentByTag(MapsFragment.TAG);
    }

    public void moveOnMyLocation() {
        if (mFragment != null) {
            mFragment.moveOnMyLocation();
        }
    }

    public boolean moveOnFootprint() {
        if (mFragment != null) {
            mFragment.moveOnFootprint();
            return true;
        }
        return false;
    }

    public void stopMoving() {
        if (mFragment != null) {
            mFragment.stopMoving();
        }
    }

    public void observeMovement(@Nullable MapsFragment.MapMovementCallback callback) {
        if (mFragment != null) {
            mFragment.observeMovement(callback);
        }
    }

    public void observeClusterClick(@Nullable MapsFragment.ClusterClickCallback callback) {
        if (mFragment != null) {
            mFragment.observeClusterClick(callback);
        }
    }
}
