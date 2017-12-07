package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public abstract class FabButtonsPerformer {

    public interface PositionClickObserver {

        void notifyPositionButtonClick();
    }

    public interface PositionLongClickObserver {

        void notifyPositionButtonLongClick();
    }

    @Nullable
    private PositionClickObserver mPositionClickObserver;

    @Nullable
    private PositionLongClickObserver mPositionLongClickObserver;

    @IdRes
    private int mPositionButtonRes;

    public FabButtonsPerformer(int positionRes) {
        mPositionButtonRes = positionRes;
    }

    public void inject(@NonNull View view) {
        FloatingActionButton positionFab = view.findViewById(mPositionButtonRes);
        positionFab.setOnClickListener(
                button -> {
                    if (mPositionClickObserver != null) {
                        mPositionClickObserver.notifyPositionButtonClick();
                    }
                }
        );
        positionFab.setOnLongClickListener(
                button -> {
                    if (mPositionLongClickObserver != null) {
                        mPositionLongClickObserver.notifyPositionButtonLongClick();
                        return true;
                    }
                    return false;
                }
        );
    }

    public void observePositionClick(@Nullable PositionClickObserver observer) {
        mPositionClickObserver = observer;
    }

    public void observePositionLongClick(@Nullable PositionLongClickObserver observer) {
        mPositionLongClickObserver = observer;
    }
}
