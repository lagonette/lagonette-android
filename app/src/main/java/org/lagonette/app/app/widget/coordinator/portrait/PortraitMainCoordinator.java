package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.base.AbstractMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class PortraitMainCoordinator
        extends AbstractMainCoordinator {

    public PortraitMainCoordinator(
            @NonNull DoneMarker doneMarker,
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader,
            @NonNull MapFragmentPerformer mapFragmentPerformer) {
        super(doneMarker, bottomSheetCallback, fragmentLoader, mapFragmentPerformer);
    }

    @Override
    protected void computeFiltersOpening(@NonNull MainStatefulAction statefulAction) {
        switch (statefulAction.state.bottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        openBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                switch (statefulAction.state.bottomSheetFragmentType.getFragmentType()) {

                    case BottomSheetFragmentType.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentType.FRAGMENT_LOCATION:
                        closeBottomSheet();
                        break;
                }
                break;
        }
    }
}
