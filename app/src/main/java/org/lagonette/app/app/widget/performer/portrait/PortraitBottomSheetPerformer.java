package org.lagonette.app.app.widget.performer.portrait;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;

public class PortraitBottomSheetPerformer
        extends BottomSheetPerformer {

    public PortraitBottomSheetPerformer(
            @NonNull Resources resources,
            @IdRes int bottomSheetRes,
            @DimenRes int searchBarHeightRes) {
        super(resources, bottomSheetRes, searchBarHeightRes);
    }

    @Override
    public void openBottomSheet() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}
