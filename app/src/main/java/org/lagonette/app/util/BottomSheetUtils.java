package org.lagonette.app.util;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class BottomSheetUtils {

    @NonNull
    public static String toString(@UiState.BottomSheetState int state) {
        switch (state) {

            case BottomSheetBehavior.STATE_COLLAPSED:
                return "STATE_COLLAPSED";

            case BottomSheetBehavior.STATE_DRAGGING:
                return "STATE_DRAGGING";

            case BottomSheetBehavior.STATE_EXPANDED:
                return "STATE_EXPANDED";

            case BottomSheetBehavior.STATE_HIDDEN:
                return "STATE_HIDDEN";

            case BottomSheetBehavior.STATE_SETTLING:
                return "STATE_SETTLING";

        }

        return "";
    }

}
