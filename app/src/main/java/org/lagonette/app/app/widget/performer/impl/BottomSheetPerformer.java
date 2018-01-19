package org.lagonette.app.app.widget.performer.impl;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.tools.functions.IntConsumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.util.UiUtils;

public abstract class BottomSheetPerformer
        extends BottomSheetBehavior.BottomSheetCallback
        implements ViewPerformer {

    public static class Padding {

        public int bottomSheetTop, searchBarBottom, statusBarHeight;

        private int mTopPadding;

        public boolean updateTop() {
            int limit = Math.max(statusBarHeight, searchBarBottom);
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
    public IntConsumer onSlideChanged = NullFunctions::doNothing;

    @NonNull
    public IntConsumer onStateChanged = NullFunctions::doNothing;

    @Nullable
    protected BottomSheetBehavior<View> mBehavior;

    @Nullable
    protected View mBottomSheet;

    @IdRes
    private int mBottomSheetRes;

    @NonNull
    protected Padding mPadding;

    public BottomSheetPerformer(
            @NonNull Resources resources,
            @IdRes int bottomSheetRes) {
        mBottomSheetRes = bottomSheetRes;
        mPadding = new Padding();
        mPadding.statusBarHeight = UiUtils.getStatusBarHeight(resources);
        mPadding.searchBarBottom = 0;
        mPadding.bottomSheetTop = 0;
    }

    @Override
    public void inject(@NonNull View view) {
        mBottomSheet = view.findViewById(mBottomSheetRes);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setBottomSheetCallback(BottomSheetPerformer.this);
    }

    public void changeBottomSheetState(@BottomSheetBehavior.State int newState) {
        if (mBehavior != null) {
            if (mBehavior.getState() == newState) {
                onStateChanged.accept(newState);
            }
            else {
                mBehavior.setState(newState);
            }
        }
    }

    public final void closeBottomSheet() {
        changeBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void openBottomSheet() {
        changeBottomSheetState(getOpenBottomSheetState());
    }

    @BottomSheetBehavior.State
    public abstract int getOpenBottomSheetState();

    @Override
    public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
        onStateChanged.accept(newState);
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (mBottomSheet != null) {
            mPadding.bottomSheetTop = mBottomSheet.getTop();
            updateBottomSheetTopPadding();
        }
    }

    public void notifySearchBarBottomChanged(int searchBarBottom) {
        mPadding.searchBarBottom = searchBarBottom;
        updateBottomSheetTopPadding();
    }

    private void updateBottomSheetTopPadding() {
        if (mPadding.updateTop()) {
            onSlideChanged.accept(mPadding.getTop());
        }
    }

    @BottomSheetBehavior.State
    public int getState() {
        return mBehavior.getState();
    }

}
