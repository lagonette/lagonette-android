package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import static org.lagonette.app.app.widget.coordinator.state.MainState.MapMovement.IDLE;

public class MainState {

    public enum MapMovement {
        IDLE,
        MOVE
    }

    @BottomSheetBehavior.State
    public int bottomSheetState;

    public boolean isFiltersLoaded;

    public boolean isLocationDetailLoaded;

    @NonNull
    public MapMovement mapMovement;

    @Nullable
    public MainAction action;

    public MainState() {
        bottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
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
        string += "\t\tis filters loaded: " + isFiltersLoaded + "\n";
        string += "\t\tis location detail loaded: " + isLocationDetailLoaded + "\n";
        string += "\t]\n";
        string += "]";
        return string;
    }
}
