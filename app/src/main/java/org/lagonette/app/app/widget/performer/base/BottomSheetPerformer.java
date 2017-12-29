package org.lagonette.app.app.widget.performer.base;

import android.content.res.Resources;
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

    public interface OnSlideChangedCommand {

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

    @Nullable
    protected OnSlideChangedCommand mOnSlideChangedCommand;

    @Nullable
    private OnStateChangedCommand mOnStateChangedCommand;

    @Nullable
    protected BottomSheetBehavior<View> mBehavior;

    @Nullable
    private View mBottomSheet;

    @IdRes
    private int mBottomSheetRes;

    @NonNull
    protected Padding mPadding;

    public BottomSheetPerformer(
            @NonNull Resources resources,
            @IdRes int bottomSheetRes) {
        mBottomSheetRes = bottomSheetRes;
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtil.getStatusBarHeight(resources);
        mPadding.searchBarHeight = 0;
        mPadding.searchBarOffset = 0;
        mPadding.bottomSheetTop = 0;
    }

    //TODO Use performer interface for inject()
    public void inject(@NonNull View view) {
        mBottomSheet = view.findViewById(mBottomSheetRes);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setBottomSheetCallback(BottomSheetPerformer.this);
    }

    /**
     * Because of the view#post() call, bottom sheet state is set after performer is connected to LiveData.
     * So bottom sheet state initialization is saved into LiveData.
     * If you do 2 orientation changes, the bottom sheet state is not the same as the started state.
     */
    public void restoreOpenState() {
        mBottomSheet.post(this::openBottomSheet);
    }

    public void restoreCloseState() {
        mBottomSheet.post(this::closeBottomSheet);
    }

    public void closeBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public abstract void openBottomSheet();

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
            if (mOnSlideChangedCommand != null) {
                mOnSlideChangedCommand.updateTopPadding(mPadding.getTop());
            }
        }
    }

    public void onStateChanged(@Nullable OnStateChangedCommand onStateChangedCommand) {
        mOnStateChangedCommand = onStateChangedCommand;
    }

    public void onSlideChanged(@Nullable OnSlideChangedCommand command) {
        mOnSlideChangedCommand = command;
    }

}
