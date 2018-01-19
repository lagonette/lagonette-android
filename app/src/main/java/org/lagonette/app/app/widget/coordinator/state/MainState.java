package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.BottomSheetUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.design.widget.BottomSheetBehavior.STATE_SETTLING;

public class MainState {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_EXPANDED,
            STATE_COLLAPSED,
            STATE_DRAGGING,
            STATE_SETTLING,
            STATE_HIDDEN
    })
    public @interface BottomSheetState {}

    public enum MapMovement {
        IDLE,
        MOVE
    }

    @Nullable
    public final MainAction action;

    @NonNull
    public final MapMovement mapMovement;

    @BottomSheetBehavior.State
    public final int bottomSheetState;

    public final boolean isFiltersLoaded;

    public final long loadedLocationId;

    public final boolean isLocationDetailLoaded;

    public MainState(
            @Nullable MainAction action,
            @NonNull MapMovement mapMovement,
            @BottomSheetBehavior.State int bottomSheetState,
            boolean isFiltersLoaded,
            boolean isLocationDetailLoaded,
            long loadedLocationId) {
        this.action = action;
        this.mapMovement = mapMovement;
        this.bottomSheetState = bottomSheetState;
        this.isFiltersLoaded = isFiltersLoaded;
        this.isLocationDetailLoaded = isLocationDetailLoaded;
        this.loadedLocationId = loadedLocationId;
    }

    @NonNull
    public MainState action(@NonNull MainAction action) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                loadedLocationId
        );
    }

    @NonNull
    public MainState mapMovement(@NonNull MapMovement mapMovement) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                loadedLocationId
        );
    }

    @NonNull
    public MainState bottomSheetState(@BottomSheetBehavior.State int bottomSheetState) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                loadedLocationId
        );
    }

    @NonNull
    public MainState filtersLoading(boolean isFiltersLoaded) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                loadedLocationId
        );
    }

    @NonNull
    public MainState locationDetailLoading(boolean isLocationDetailLoaded, long loadedLocationId) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                loadedLocationId
        );
    }

    @NonNull
    public MainState locationDetailLoading(boolean isLocationDetailLoaded) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                isLocationDetailLoaded,
                Statement.NO_ID
        );
    }

    @NonNull
    public MainState loadedLocationId(long loadedLocationId) {
        return new MainState(
                action,
                mapMovement,
                bottomSheetState,
                isFiltersLoaded,
                true,
                loadedLocationId
        );
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
        string += BottomSheetUtils.toString(bottomSheetState) + "\n";
        string += "\tBottom sheet fragment state: [\n";
        string += "\t\tis filters loaded: " + isFiltersLoaded + "\n";
        string += "\t\tis location detail loaded: " + isLocationDetailLoaded + "\n";
        string += "\t]\n";
        string += "]";
        return string;
    }
}
