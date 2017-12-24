package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainState {

    public static final int STATE_MOVEMENT_IDLE = 0;

    public static final int STATE_MOVEMENT_MOVE = 1;

    @Retention(SOURCE)
    @IntDef({
            BottomSheetBehavior.STATE_DRAGGING,
            BottomSheetBehavior.STATE_SETTLING,
            BottomSheetBehavior.STATE_EXPANDED,
            BottomSheetBehavior.STATE_COLLAPSED,
            BottomSheetBehavior.STATE_HIDDEN
    })
    public @interface State {

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_MOVEMENT_IDLE,
            STATE_MOVEMENT_MOVE
    })
    public @interface Movement {
    }

    @State
    public int bottomSheetState;

    @NonNull
    public BottomSheetFragmentState bottomSheetFragmentState;

    @Movement
    public int mapMovement;

    public MainState(@NonNull BottomSheetFragmentState type) {
        bottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
        bottomSheetFragmentState = type;
        mapMovement = STATE_MOVEMENT_IDLE;
    }
}
