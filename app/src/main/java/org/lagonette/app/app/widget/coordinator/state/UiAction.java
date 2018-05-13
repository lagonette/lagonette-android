package org.lagonette.app.app.widget.coordinator.state;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.Statement;

import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.BACK;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.MOVE_TO_AND_OPEN_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.RESTORE;
import static org.lagonette.app.app.widget.coordinator.state.UiAction.ActionType.SHOW_FULL_MAP;

public class UiAction {

	//TODO Make MainAction an enum not just action type
	//TODO Or Use strategy pattern

	@NonNull
	public final ActionType type;

	@Nullable
	public final Cluster<LocationItem> cluster;

	public final long selectedLocationId;

	@Nullable
	public final Location mapLocation;

	@Nullable
	public UiAction pendingAction;

	public boolean shouldMove;

	public UiAction(
			@NonNull ActionType type,
			@Nullable UiAction pendingAction,
			@Nullable Cluster<LocationItem> cluster,
			long selectedLocationId,
			@Nullable Location mapLocation,
			boolean shouldMove) {
		this.type = type;
		this.pendingAction = pendingAction;
		this.cluster = cluster;
		this.selectedLocationId = selectedLocationId;
		this.mapLocation = mapLocation;
		this.shouldMove = shouldMove;
	}

	public static UiAction openFilters() {
		return new UiAction(
				OPEN_FILTERS,
				null,
				null,
				Statement.NO_ID,
				null,
				false
		);
	}

	public static UiAction showFullMap() {
		return new UiAction(
				SHOW_FULL_MAP,
				null,
				null,
				Statement.NO_ID,
				null,
				false
		);
	}

	public static UiAction moveToMyLocation(Location mapLocation) {
		return new UiAction(
				MOVE_TO_MY_LOCATION,
				null,
				null,
				Statement.NO_ID,
				mapLocation,
				true
		);
	}

	public static UiAction moveToFootprint() {
		return new UiAction(
				MOVE_TO_FOOTPRINT,
				null,
				null,
				Statement.NO_ID,
				null,
				true
		);
	}

	public static UiAction moveToCluster(@Nullable Cluster<LocationItem> cluster) {
		return new UiAction(
				MOVE_TO_CLUSTER,
				null,
				cluster,
				Statement.NO_ID,
				null,
				true
		);
	}

	public static UiAction openLocation(long locationId) {
		return new UiAction(
				MOVE_TO_AND_OPEN_LOCATION,
				null,
				null,
				locationId,
				null,
				true
		);
	}

	public static UiAction back() {
		return new UiAction(
				BACK,
				null,
				null,
				Statement.NO_ID,
				null,
				false
		);
	}

	public static UiAction restore(@Nullable UiAction pendingAction) {
		return new UiAction(
				RESTORE,
				pendingAction,
				null,
				Statement.NO_ID,
				null,
				false
		);
	}

	public enum ActionType {
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
}
