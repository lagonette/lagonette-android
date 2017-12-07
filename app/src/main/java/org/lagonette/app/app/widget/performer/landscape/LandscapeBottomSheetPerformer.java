package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;

public class LandscapeBottomSheetPerformer
        extends BottomSheetPerformer {

    public LandscapeBottomSheetPerformer(@IdRes int bottomSheetRes) {
        super(bottomSheetRes);
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
