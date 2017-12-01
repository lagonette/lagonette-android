package org.lagonette.app.app.widget.performer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.util.UiUtil;

public class BottomSheetPerformer extends BottomSheetBehavior.BottomSheetCallback
        implements MainCoordinator.BottomSheetCallback {

    public interface StateObserver {

        void notifyBottomSheetStateChanged(@MainState.State int newState);
    }

    public interface SlideObserver {

        void notifyBottomSheetTopChanged(int top);
    }

    @Nullable
    private StateObserver mStateObserver;

    @Nullable
    private SlideObserver mSlideObserver;

    @Nullable
    private BottomSheetBehavior<View> mBehavior;

    @Nullable
    private View mBottomSheet;

    private final int mStatusBarHeight;

    @IdRes
    private int mBottomSheetRes;

    public BottomSheetPerformer(@NonNull Context context, int bottomSheetRes) {
        mStatusBarHeight = UiUtil.getStatusBarHeight(context.getResources());
        mBottomSheetRes = bottomSheetRes;
    }

    public void inject(@NonNull View view) {
        mBottomSheet = view.findViewById(mBottomSheetRes);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setBottomSheetCallback(BottomSheetPerformer.this);
    }

    public void init(@MainState.State int initState) {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        mBehavior.setState(initState);
    }

    public void restore(@MainState.State int restoredState) {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        switch (restoredState) {

            case BottomSheetBehavior.STATE_DRAGGING:
            case BottomSheetBehavior.STATE_SETTLING:
                restoredState = BottomSheetBehavior.STATE_COLLAPSED;
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
            case BottomSheetBehavior.STATE_COLLAPSED:
            case BottomSheetBehavior.STATE_EXPANDED:
                // Do nothing
                break;
        }

        mBehavior.setState(restoredState);
    }

    @Override
    public void closeBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, @MainState.State int newState) {
        if (mStateObserver != null) {
            mStateObserver.notifyBottomSheetStateChanged(newState);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (mSlideObserver!= null && mBottomSheet != null) {
            int top = mBottomSheet.getTop();
            mSlideObserver.notifyBottomSheetTopChanged(top);
        }
    }

    public void observeState(@Nullable StateObserver stateObserver) {
        mStateObserver = stateObserver;
    }

    public void observeSlide(@Nullable SlideObserver slideObserver) {
        mSlideObserver = slideObserver;
    }

}
