package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    public static Builder build() {
        return new Builder();
    }

    public static Builder build(@Nullable MainState state) {
        return state != null ? state.buildUppon() : new Builder();
    }

    @Nullable
    public final MainAction action;

    @NonNull
    public final MapMovement mapMovement;

    @BottomSheetState
    public final int bottomSheetState;

    public final boolean isFiltersLoaded;

    public final long loadedLocationId;

    public final boolean isLocationDetailLoaded;

    public MainState(
            @Nullable MainAction action,
            @NonNull MapMovement mapMovement,
            @BottomSheetState  int bottomSheetState,
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

    public Builder buildUppon() {
        return new Builder(this);
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

    public static class Builder {

        @Nullable
        public MainAction action;

        @NonNull
        public MapMovement mapMovement = MapMovement.IDLE;

        @BottomSheetState
        public int bottomSheetState;

        public boolean isFiltersLoaded;

        public long loadedLocationId = Statement.NO_ID;

        public boolean isLocationDetailLoaded;

        public Builder() {
        }

        public Builder(@NonNull MainState state) {
            this.action = state.action;
            this.mapMovement = state.mapMovement;
            this.bottomSheetState = state.bottomSheetState;
            this.isFiltersLoaded = state.isFiltersLoaded;
            this.isLocationDetailLoaded = state.isLocationDetailLoaded;
            this.loadedLocationId = state.loadedLocationId;
        }

        public Builder setAction(@Nullable MainAction action) {
            this.action = action;
            return this;
        }

        public Builder setMapMovement(@NonNull MapMovement mapMovement) {
            this.mapMovement = mapMovement;
            return this;
        }

        public Builder setBottomSheetState(@BottomSheetState int bottomSheetState) {
            this.bottomSheetState = bottomSheetState;
            return this;
        }

        public Builder setFiltersLoaded(boolean filtersLoaded) {
            isFiltersLoaded = filtersLoaded;
            return this;
        }

        public Builder setLoadedLocationId(long loadedLocationId) {
            this.loadedLocationId = loadedLocationId;
            return this;
        }

        public Builder clearLoadedLocationId() {
            this.loadedLocationId = Statement.NO_ID;
            return this;
        }

        public Builder setLocationDetailLoaded(boolean locationDetailLoaded) {
            isLocationDetailLoaded = locationDetailLoaded;
            return this;
        }

        public MainState build() {
            return new MainState(
                    action,
                    mapMovement,
                    bottomSheetState,
                    isFiltersLoaded,
                    isLocationDetailLoaded,
                    loadedLocationId
            );
        }
    }
}

