package org.lagonette.app.app.widget.performer;

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
import org.lagonette.app.app.widget.behavior.ParallaxBehavior;
import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.util.UiUtil;

public class MapFragmentPerformer {

    public static class Padding {

        public int parallaxOffset, searchBarOffset, searchBarHeight, statusBarHeight;

        // TODO Do not use searchBarHeight, use an inverted offset or something else
        public int getTop() {
            return statusBarHeight + searchBarHeight + searchBarOffset - parallaxOffset;
        }

        public int getBottom() {
            return -parallaxOffset;
        }
    }

    private MapsFragment mFragment;

    @NonNull
    private final FragmentManager mFragmentManager;
    
    @IdRes
    private int mMapFragmentRes;

    @Nullable
    private ParallaxBehavior<View> mBehavior;

    @NonNull
    private final Padding mPadding;

    public MapFragmentPerformer(@NonNull AppCompatActivity activity, @IdRes int mapFragmentRes, @DimenRes int searchBarHeightRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mMapFragmentRes = mapFragmentRes;
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(activity.getResources());
        mPadding.searchBarHeight = activity.getResources().getDimensionPixelOffset(searchBarHeightRes);
    }

    public void inject(@NonNull View view) {
        View mapFragmentView = view.findViewById(mMapFragmentRes);
        mBehavior = ParallaxBehavior.from(mapFragmentView);

        mBehavior.setOnParallaxTranslationListener(this::notifyParallaxOffsetChanged);
    }

    public void init() {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        mFragment = MapsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mFragment, MapsFragment.TAG)
                .commit();
    }

    public void restore() {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

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

    public void notifySearchBarOffsetChanged(int searchBarOffset) {
        mPadding.searchBarOffset = searchBarOffset;
        updateMapPadding();
    }

    public void notifyParallaxOffsetChanged(float parallaxOffset) {
        mPadding.parallaxOffset = (int) parallaxOffset;
        updateMapPadding();
    }

    private void updateMapPadding() {
        mFragment.setMapPadding(
                0,
                mPadding.getTop(),
                0,
                mPadding.getBottom()
        );
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
