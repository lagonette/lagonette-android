package org.lagonette.app.app.widget.performer;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class FabButtonsPerformer {

    public interface FiltersObserver {

        void notifyFiltersButtonClick();
    }

    public interface PositionClickObserver {

        void notifyPositionButtonClick();
    }

    public interface PositionLongClickObserver {

        void notifyPositionButtonLongClick();
    }

    @Nullable
    private FiltersObserver mFiltersObserver;

    @Nullable
    private PositionClickObserver mPositionClickObserver;

    @Nullable
    private PositionLongClickObserver mPositionLongClickObserver;

    @IdRes
    private int mPositionButtonRes;

    @IdRes
    private int mFiltersButtonRes;

    public FabButtonsPerformer(int positionRes, int filtersRes) {
        mPositionButtonRes = positionRes;
        mFiltersButtonRes = filtersRes;
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

        FloatingActionButton filtersFab = view.findViewById(mFiltersButtonRes);
        filtersFab.setOnClickListener(
                button -> {
                    if (mFiltersObserver != null) {
                        mFiltersObserver.notifyFiltersButtonClick();
                    }
                }
        );
    }

    public void observeFiltersClick(@Nullable FiltersObserver observer) {
        mFiltersObserver = observer;
    }

    public void observePositionClick(@Nullable PositionClickObserver observer) {
        mPositionClickObserver = observer;
    }

    public void observePositionLongClick(@Nullable PositionLongClickObserver observer) {
        mPositionLongClickObserver = observer;
    }
}
