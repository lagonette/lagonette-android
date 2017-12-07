package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import org.lagonette.app.app.widget.performer.base.FabButtonsPerformer;

public class PortraitFabButtonsPerformer extends FabButtonsPerformer {

    public interface FiltersObserver {

        void notifyFiltersButtonClick();
    }

    @Nullable
    private FiltersObserver mFiltersObserver;

    @IdRes
    private int mFiltersButtonRes;

    public PortraitFabButtonsPerformer(int positionRes, int filtersRes) {
        super(positionRes);
        mFiltersButtonRes = filtersRes;
    }

    @Override
    public void inject(@NonNull View view) {
        super.inject(view);

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
}
