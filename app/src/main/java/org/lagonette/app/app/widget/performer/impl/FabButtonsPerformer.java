package org.lagonette.app.app.widget.performer.impl;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.tools.functions.NullFunctions;

public abstract class FabButtonsPerformer implements ViewPerformer {

    @NonNull
    public Runnable onPositionClick = NullFunctions::doNothing;

    @NonNull
    public Runnable onPositionLongClick = NullFunctions::doNothing;

    @IdRes
    private int mPositionButtonRes;

    public FabButtonsPerformer(int positionRes) {
        mPositionButtonRes = positionRes;
    }

    @Override
    public void inject(@NonNull View view) {
        FloatingActionButton positionFab = view.findViewById(mPositionButtonRes);
        positionFab.setOnClickListener(
                button -> onPositionClick.run()
        );
        positionFab.setOnLongClickListener(
                button -> {
                    onPositionLongClick.run();
                    return true;
                }
        );
    }

}
