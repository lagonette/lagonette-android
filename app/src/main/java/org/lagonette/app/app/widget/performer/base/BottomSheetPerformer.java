package org.lagonette.app.app.widget.performer.base;

import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.util.UiUtil;

public abstract class BottomSheetPerformer
        extends BottomSheetBehavior.BottomSheetCallback {

    public interface OnStateChangedCommand {

        void onStateChanged(@MainState.State int newState);
    }

    public interface FragmentObserver {

        void onUnload();

        void onFiltersLoaded();

        void onLocationLoaded(long locationId);

    }

    public interface Slideable {

        void updateTopPadding(int topPadding);

    }

    public static class Padding {

        public int bottomSheetTop, searchBarHeight, searchBarOffset, statusBarHeight;

        private int mTopPadding;

        // TODO Do not use searchBarHeight, use an inverted offset or something else
        public boolean updateTop() {
            int limit = statusBarHeight + searchBarHeight + searchBarOffset;
            if (bottomSheetTop <= limit) {
                mTopPadding = limit - bottomSheetTop;
                return true;
            }
            else if (mTopPadding != 0) {
                mTopPadding = 0;
                return true;
            }
            else {
                return false;
            }
        }

        public int getTop() {
            return mTopPadding;
        }
    }

    @NonNull
    protected final LocationDetailFragmentPerformer mLocationDetailFragmentPerformer;

    @Nullable
    protected Slideable mSlideablePerformer;

    @Nullable
    private OnStateChangedCommand mOnStateChangedCommand;

    @Nullable
    protected BottomSheetBehavior<View> mBehavior;

    @Nullable
    private View mBottomSheet;

    @IdRes
    private int mBottomSheetRes;

    @Nullable
    protected FragmentObserver mFragmentObserver;

    @NonNull
    private Padding mPadding;

    public BottomSheetPerformer(
            @NonNull Resources resources,
            @NonNull LocationDetailFragmentPerformer locationDetailFragmentPerformer,
            @IdRes int bottomSheetRes,
            @DimenRes int searchBarHeightRes) {
        mLocationDetailFragmentPerformer = locationDetailFragmentPerformer;
        mBottomSheetRes = bottomSheetRes;
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(resources);
        mPadding.searchBarHeight = resources.getDimensionPixelOffset(searchBarHeightRes);
        mPadding.searchBarOffset = 0;
        mPadding.bottomSheetTop = 0;
    }

    public void inject(@NonNull View view) {
        mBottomSheet = view.findViewById(mBottomSheetRes);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setBottomSheetCallback(BottomSheetPerformer.this);
    }

    public void init() {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        //mLocationDetailFragmentPerformer.init();

        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    // TODO One State class by performer ?
    // TODO Put *FragmentPerformer#restore() here
    @CallSuper
    public void restore(@NonNull MainState mainState) {
        mLocationDetailFragmentPerformer.restore();
    }

    public void closeBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void expandBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void collapseBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void unloadFragment() {

        if (mLocationDetailFragmentPerformer != null) {
            mLocationDetailFragmentPerformer.unloadFragment();
        }

        //TODO Use Observer pattern to be sure *FragmentPerformer unloading is managed each times its called
        mSlideablePerformer = null;

        if (mFragmentObserver != null) {
            mFragmentObserver.onUnload();
        }
    }

    //TODO Remove animation boolean
    public void loadLocationFragment(long locationId, boolean animation) {
        if (mLocationDetailFragmentPerformer != null) {
            mLocationDetailFragmentPerformer.loadFragment(locationId, animation);
            mSlideablePerformer = mLocationDetailFragmentPerformer;

            if (mFragmentObserver != null) {
                mFragmentObserver.onLocationLoaded(locationId);
            }
        }
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, @MainState.State int newState) {
        if (mOnStateChangedCommand != null) {
            mOnStateChangedCommand.onStateChanged(newState);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (mBottomSheet != null) {
            mPadding.bottomSheetTop = mBottomSheet.getTop();
            updateBottomSheetTopPadding();
        }
    }

    public void notifySearchBarOffsetChanged(int searchBarOffset) {
        mPadding.searchBarOffset = searchBarOffset;
        updateBottomSheetTopPadding();
    }

    private void updateBottomSheetTopPadding() {
        if (mPadding.updateTop()) {
            if (mSlideablePerformer != null) {
                mSlideablePerformer.updateTopPadding(mPadding.getTop());
            }
        }
    }

    public void onStateChanged(@Nullable OnStateChangedCommand onStateChangedCommand) {
        mOnStateChangedCommand = onStateChangedCommand;
    }

    public void onFragmentLoaded(@Nullable FragmentObserver fragmentObserver) {
        mFragmentObserver = fragmentObserver;
    }
}
