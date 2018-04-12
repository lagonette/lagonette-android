package org.lagonette.app.app.widget.performer.landscape;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;

import javax.inject.Inject;

public class LandscapeBottomSheetPerformer
        extends BottomSheetPerformer {

    @Inject
    public LandscapeBottomSheetPerformer(@NonNull Resources resources) {
        super(resources);
    }

    @Override
    public int getOpenBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            closeBottomSheet();
        }
        else {
            super.onStateChanged(bottomSheet, newState);
        }
    }
}
