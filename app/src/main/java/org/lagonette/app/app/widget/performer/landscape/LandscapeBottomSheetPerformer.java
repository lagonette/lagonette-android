package org.lagonette.app.app.widget.performer.landscape;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;

public class LandscapeBottomSheetPerformer
        extends BottomSheetPerformer {

    public LandscapeBottomSheetPerformer(
            @NonNull Resources resources,
            @IdRes int bottomSheetRes) {
        super(resources, bottomSheetRes);
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, @MainState.State int newState) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            closeBottomSheet();
        }
        else {
            super.onStateChanged(bottomSheet, newState);
        }
    }
}
