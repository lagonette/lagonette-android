package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.lagonette.app.app.widget.coordinator.state.MainState.MapMovement.IDLE;

public class MainState {

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

    public enum MapMovement {
        IDLE,
        MOVE
    }

    @State
    public int bottomSheetState;

    @NonNull
    public BottomSheetFragmentState bottomSheetFragmentState;

    public MapMovement mapMovement;

    public MainState(@NonNull BottomSheetFragmentState type) {
        bottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
        bottomSheetFragmentState = type;
        mapMovement = IDLE;
    }

    @Override
    public String toString() {
        String string = "MainState: [\n";
        string += "\tMap movement: ";
        switch (mapMovement) {
            case IDLE:
                string += "IDLE\n";
                break;
            case MOVE:
                string += "MOVE\n";
                break;
        }
        string += "\tBottom sheet state: ";
        switch (bottomSheetState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                string += "STATE_COLLAPSED\n";
                break;
            case BottomSheetBehavior.STATE_DRAGGING:
                string += "STATE_DRAGGING\n";
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                string += "STATE_EXPANDED\n";
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                string += "STATE_HIDDEN\n";
                break;
            case BottomSheetBehavior.STATE_SETTLING:
                string += "STATE_SETTLING\n";
                break;
        }
        string += "\tBottom sheet fragment state: [\n";
        string += "\t\tis filters loaded: " + bottomSheetFragmentState.isFiltersLoaded() + "\n";
        string += "\t\tis location detail loaded: " + bottomSheetFragmentState.isLocationDetailLoaded() + "\n";
        string += "\t]\n";
        string += "]";
        return string;
    }
}
