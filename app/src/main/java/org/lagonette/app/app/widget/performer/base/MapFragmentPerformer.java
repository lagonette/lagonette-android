package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.room.entity.statement.PartnerItem;

public  abstract class MapFragmentPerformer {

    protected MapsFragment mFragment;

    @NonNull
    private final FragmentManager mFragmentManager;
    
    @IdRes
    protected int mMapFragmentRes;

    public MapFragmentPerformer(@NonNull AppCompatActivity activity, @IdRes int mapFragmentRes, @DimenRes int searchBarHeightRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mMapFragmentRes = mapFragmentRes;
    }

    public abstract void inject(@NonNull View view);

    public void init() {
        //TODO Check inject is called before init

        mFragment = MapsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mFragment, MapsFragment.TAG)
                .commit();
    }

    public void restore() {
        //TODO Check inject is called before restore

        mFragment = (MapsFragment) mFragmentManager.findFragmentByTag(MapsFragment.TAG);
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

    public void moveToCluster(@NonNull Cluster<PartnerItem> cluster) {
        if (mFragment != null) {
            mFragment.moveToCluster(cluster);
        }
    }

    public void moveToLocation(@NonNull PartnerItem item) {
        if (mFragment != null) {
            mFragment.moveToLocation(item);
        }
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

    public void observeItemClick(@Nullable MapsFragment.ItemClickCallback callback) {
        if (mFragment != null) {
            mFragment.observeItemClick(callback);
        }
    }

    public void observeMapClick(@Nullable MapsFragment.MapClickCallback callback) {
        if (mFragment != null) {
            mFragment.observeMapClick(callback);
        }
    }
}
