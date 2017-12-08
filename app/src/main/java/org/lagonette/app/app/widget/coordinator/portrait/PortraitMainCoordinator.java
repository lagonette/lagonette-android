package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class PortraitMainCoordinator
        extends AbstractMainCoordinator {

    public PortraitMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetPerformer bottomSheetPerformer,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(doneMarker, bottomSheetPerformer, mapFragmentPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        mBottomSheetPerformer.loadFiltersFragment();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        mBottomSheetPerformer.expandBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        mDoneMarker.markPendingActionAsDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        mDoneMarker.markPendingActionAsDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        mBottomSheetPerformer.closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        mDoneMarker.markPendingActionAsDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        mBottomSheetPerformer.closeBottomSheet();
                        break;
                }
                break;
        }
    }
}
