package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.Statement;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.BACK;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_AND_OPEN_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.RESTORE;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.SHOW_FULL_MAP;

public class MainAction {

    //TODO Make MainAction an enum not just action type

    public enum  ActionType {
        IDLE,
        RESTORE,
        BACK,
        OPEN_FILTERS,
        MOVE_TO_MY_LOCATION,
        MOVE_TO_FOOTPRINT,
        MOVE_TO_CLUSTER,
        MOVE_TO_AND_OPEN_LOCATION,
        SHOW_FULL_MAP
    }

    @NonNull
    public final ActionType type;

    @Nullable
    public MainAction pendingAction;

    @Nullable
    public final Cluster<LocationItem> cluster;

    public final long locationId;

    @Nullable
    public final LocationItem item;

    public boolean shouldMove;

    public MainAction(
            @NonNull ActionType type,
            @Nullable MainAction pendingAction,
            @Nullable Cluster<LocationItem> cluster,
            @Nullable LocationItem item,
            long locationId,
            boolean shouldMove) {
        this.type = type;
        this.cluster = cluster;
        this.item = item;
        this.locationId = locationId;
        this.shouldMove = shouldMove;
    }

    public static MainAction openFilters() {
        return new MainAction(
                OPEN_FILTERS,
                null,
                null,
                null,
                Statement.NO_ID,
                false
        );
    }

    public static MainAction showFullMap() {
        return new MainAction(
                SHOW_FULL_MAP,
                null,
                null,
                null,
                Statement.NO_ID,
                false
        );
    }

    public static MainAction moveToMyLocation() {
        return new MainAction(
                MOVE_TO_MY_LOCATION,
                null,
                null,
                null,
                Statement.NO_ID,
                true
        );
    }

    public static MainAction moveToFootprint() {
        return new MainAction(
                MOVE_TO_FOOTPRINT,
                null,
                null,
                null,
                Statement.NO_ID,
                true
        );
    }

    public static MainAction moveToCluster(@Nullable Cluster<LocationItem> cluster) {
        return new MainAction(
                MOVE_TO_CLUSTER,
                null,
                cluster,
                null,
                Statement.NO_ID,
                true
        );
    }

    public static MainAction moveToAndOpenLocation(@Nullable LocationItem item) {
        return new MainAction(
                MOVE_TO_AND_OPEN_LOCATION,
                null,
                null,
                item,
                Statement.NO_ID,
                true
        );
    }

    //TODO What if locationId <= NO_ID?
    public static MainAction moveToAndOpenLocation(long locationId) {
        return new MainAction(
                MOVE_TO_AND_OPEN_LOCATION,
                null,
                null,
                null,
                locationId,
                true
        );
    }

    public static MainAction back() {
        return new MainAction(
                BACK,
                null,
                null,
                null,
                Statement.NO_ID,
                false
        );
    }

    public static MainAction restore(@Nullable MainAction pendingAction) {
        return new MainAction(
                RESTORE,
                pendingAction,
                null,
                null,
                Statement.NO_ID,
                false
        );
    }
}
