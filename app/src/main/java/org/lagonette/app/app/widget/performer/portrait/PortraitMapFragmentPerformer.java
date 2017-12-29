package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.widget.behavior.ParallaxBehavior;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.util.UiUtil;

public class PortraitMapFragmentPerformer
        extends MapFragmentPerformer {

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

    @NonNull
    private final Padding mPadding;

    @Nullable
    private ParallaxBehavior<View> mBehavior;

    public PortraitMapFragmentPerformer(@NonNull AppCompatActivity activity, int mapFragmentRes, int searchBarHeightRes) {
        super(activity, mapFragmentRes, searchBarHeightRes);
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(activity.getResources());
        mPadding.searchBarHeight = activity.getResources().getDimensionPixelOffset(searchBarHeightRes);
    }

    @Override
    public void inject(@NonNull View view) {
        View mapFragmentView = view.findViewById(mMapFragmentRes);
        mBehavior = ParallaxBehavior.from(mapFragmentView);
        mBehavior.setOnParallaxTranslationListener(this::notifyParallaxOffsetChanged);
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
}
