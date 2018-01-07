package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.room.entity.statement.LocationItem;

public abstract class MapFragmentPerformer implements Performer {

    protected MapsFragment mFragment;

    @NonNull
    private final FragmentManager mFragmentManager;
    
    @IdRes
    protected int mMapFragmentRes;

    public MapFragmentPerformer(@NonNull AppCompatActivity activity, @IdRes int mapFragmentRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mMapFragmentRes = mapFragmentRes;
    }

    public void loadFragment() {
        mFragment = MapsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mFragment, MapsFragment.TAG)
                .commit();
    }

    public void restoreFragment() {
        mFragment = (MapsFragment) mFragmentManager.findFragmentByTag(MapsFragment.TAG);
    }

    @Nullable
    public LocationItem retrieveLocationItem(long locationId) {
        if (mFragment != null) {
            return mFragment.retrieveLocationItem(locationId);
        }
        return null;
    }

    public void moveToMyLocation() {
        if (mFragment != null) {
            mFragment.moveToMyLocation();
        }
    }

    public boolean moveToFootprint() {
        if (mFragment != null) {
            mFragment.moveToFootprint();
            return true;
        }
        return false;
    }

    public void moveToCluster(@NonNull Cluster<LocationItem> cluster) {
        if (mFragment != null) {
            mFragment.moveToCluster(cluster);
        }
    }

    public void moveToLocation(@NonNull LocationItem item) {
        if (mFragment != null) {
            mFragment.moveToLocation(item);
        }
    }

    public void stopMoving() {
        if (mFragment != null) {
            mFragment.stopMoving();
        }
    }

    public void onMovement(@Nullable MapsFragment.OnMapMovementCommand command) {
        if (mFragment != null) {
            mFragment.onMovement(command);
        }
    }

    public void onClusterClick(@Nullable MapsFragment.OnClusterClickCommand command) {
        if (mFragment != null) {
            mFragment.onClusterClick(command);
        }
    }

    public void onItemClick(@Nullable MapsFragment.OnItemClickCommand command) {
        if (mFragment != null) {
            mFragment.observeItemClick(command);
        }
    }

    public void onMapClick(@Nullable MapsFragment.OnMapClickCommand command) {
        if (mFragment != null) {
            mFragment.observeMapClick(command);
        }
    }
}
