package org.lagonette.app.app.widget.performer.portrait;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;

public class PortraitBottomSheetPerformer
        extends BottomSheetPerformer {

    //TODO Useless ?
    public PortraitBottomSheetPerformer(
            @NonNull Resources resources,
            @IdRes int bottomSheetRes,
            @DimenRes int searchBarHeightRes) {
        super(resources, bottomSheetRes, searchBarHeightRes);
    }

}
