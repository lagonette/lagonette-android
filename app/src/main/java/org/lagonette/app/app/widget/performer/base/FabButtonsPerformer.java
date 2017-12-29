package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public abstract class FabButtonsPerformer implements Performer {

    public interface OnPositionClickCommand {

        void notifyPositionButtonClick();
    }

    public interface OnPositionLongClickCommand {

        void notifyPositionButtonLongClick();
    }

    @Nullable
    private OnPositionClickCommand mOnPositionClickCommand;

    @Nullable
    private OnPositionLongClickCommand mOnPositionLongClickCommand;

    @IdRes
    private int mPositionButtonRes;

    public FabButtonsPerformer(int positionRes) {
        mPositionButtonRes = positionRes;
    }

    @Override
    public void inject(@NonNull View view) {
        FloatingActionButton positionFab = view.findViewById(mPositionButtonRes);
        positionFab.setOnClickListener(
                button -> {
                    if (mOnPositionClickCommand != null) {
                        mOnPositionClickCommand.notifyPositionButtonClick();
                    }
                }
        );
        positionFab.setOnLongClickListener(
                button -> {
                    if (mOnPositionLongClickCommand != null) {
                        mOnPositionLongClickCommand.notifyPositionButtonLongClick();
                        return true;
                    }
                    return false;
                }
        );
    }

    public void onPositionClick(@Nullable OnPositionClickCommand command) {
        mOnPositionClickCommand = command;
    }

    public void onPositionLongClick(@Nullable OnPositionLongClickCommand command) {
        mOnPositionLongClickCommand = command;
    }
}
