package org.lagonette.app.app.widget.performer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.util.UiUtil;

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

    public void init(@BottomSheetPerformer.State int initState) {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        mBehavior.setState(initState);
    }

    public void restore(@BottomSheetPerformer.State int restoredState) {
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
    public void onStateChanged(@NonNull View bottomSheet, @BottomSheetPerformer.State int newState) {
        if (mStateObserver != null) {
            mStateObserver.notifyBottomSheetStateChanged(newState);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        // TODO Call an observer, store and pass value through a LiveData, give it to fragment performer, let performer send value to fragment
        if (mBottomSheet != null) {
            int top = mBottomSheet.getTop();
            if (top <= mStatusBarHeight) {
                Log.wtf("XOXO", "onSlide: " + top);
                mBottomSheet.setPadding(
                        0,
                        mStatusBarHeight - top,
                        0,
                        0
                );
            }
            else if (mBottomSheet.getPaddingTop() != 0) {
                mBottomSheet.setPadding(
                        0,
                        0,
                        0,
                        0
                );
            }
        }
    }

    public void observeState(@Nullable StateObserver stateObserver) {
        mStateObserver = stateObserver;
    }

}
