package org.lagonette.app.app.widget.performer.portrait;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;

import javax.inject.Inject;

public class PortraitBottomSheetPerformer
        extends BottomSheetPerformer {

    @Inject
    public PortraitBottomSheetPerformer(@NonNull Resources resources) {
        super(resources);
    }

    @Override
    public int getOpenBottomSheetState() {
        return BottomSheetBehavior.STATE_COLLAPSED;
    }

}
