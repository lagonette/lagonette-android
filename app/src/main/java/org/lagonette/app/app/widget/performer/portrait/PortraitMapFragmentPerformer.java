package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.behavior.ParallaxBehavior;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.util.UiUtils;

import javax.inject.Inject;

public class PortraitMapFragmentPerformer
        extends MapFragmentPerformer {

    public static class Padding {

        public int parallaxOffset, searchBarBottom, statusBarHeight;

        public int getTop() {
            return Math.max(statusBarHeight, searchBarBottom) - parallaxOffset;
        }

        public int getBottom() {
            return -parallaxOffset;
        }
    }

    @NonNull
    private final Padding mPadding;

    @Nullable
    private ParallaxBehavior<View> mBehavior;

    @Inject
    public PortraitMapFragmentPerformer(@NonNull AppCompatActivity activity) {
        super(activity);
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtils.getStatusBarHeight(activity.getResources());
        mPadding.searchBarBottom = 0;
    }

    @Override
    public void inject(@NonNull View view) {
        View mapFragmentView = view.findViewById(R.id.content);
        mBehavior = ParallaxBehavior.from(mapFragmentView);
        mBehavior.setOnParallaxTranslationListener(this::notifyParallaxOffsetChanged);
    }

    public void notifySearchBarBottomChanged(int searchBarBottom) {
        mPadding.searchBarBottom = searchBarBottom;
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
