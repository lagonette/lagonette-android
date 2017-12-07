package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public class LandscapeMainCoordinator
        extends AbstractMainCoordinator {

    public LandscapeMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(doneMarker, bottomSheetCallback, fragmentLoader, mapFragmentPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        markPendingActionDone();
    }
}
