package org.lagonette.app.app.widget.performer;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BottomSheetPerformer extends BottomSheetBehavior.BottomSheetCallback
        implements MainCoordinator.BottomSheetCallback {

    public interface StateObserver {

        void notifyBottomSheetStateChanged(@BottomSheetPerformer.State int newState);
    }

    @Retention(SOURCE)
    @IntDef({
            BottomSheetBehavior.STATE_DRAGGING,
            BottomSheetBehavior.STATE_SETTLING,
            BottomSheetBehavior.STATE_EXPANDED,
            BottomSheetBehavior.STATE_COLLAPSED,
            BottomSheetBehavior.STATE_HIDDEN
    })
    public @interface State {

    }

    @Nullable
    private StateObserver mStateObserver;

    @Nullable
    private BottomSheetBehavior<View> mBehavior;

    @IdRes
    private int mBottomSheetRes;

    public BottomSheetPerformer(int bottomSheetRes) {
        mBottomSheetRes = bottomSheetRes;
    }

    public void inject(@NonNull View view) {
        View bottomSheet = view.findViewById(mBottomSheetRes);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(BottomSheetPerformer.this);
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
    public void onStateChanged(@NonNull View bottomSheet, @BottomSheetPerformer.State int newState) {
        if (mStateObserver != null) {
            mStateObserver.notifyBottomSheetStateChanged(newState);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        // Do nothing
    }

    public void setStateObserver(@Nullable StateObserver stateObserver) {
        mStateObserver = stateObserver;
    }
}
