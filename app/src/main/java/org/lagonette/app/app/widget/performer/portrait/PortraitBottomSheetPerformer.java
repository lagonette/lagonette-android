package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;

public class PortraitBottomSheetPerformer
        extends BottomSheetPerformer {

    public PortraitBottomSheetPerformer(@IdRes int bottomSheetRes) {
        super(bottomSheetRes);
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
