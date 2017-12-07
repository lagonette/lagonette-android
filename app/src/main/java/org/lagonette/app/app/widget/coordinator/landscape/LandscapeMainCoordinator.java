package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentPerformer;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public class LandscapeMainCoordinator
        extends AbstractMainCoordinator {

    public LandscapeMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull BottomSheetFragmentPerformer bottomSheetFragmentPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(doneMarker, bottomSheetPerformer, bottomSheetFragmentPerformer, mapFragmentPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        mDoneMarker.markPendingActionAsDone();
    }
}
