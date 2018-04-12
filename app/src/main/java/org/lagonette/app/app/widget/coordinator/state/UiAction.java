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

	public final long locationId;

	@Nullable
	public final LocationItem item;

	@Nullable
	public final Location mapLocation;

	@Nullable
	public UiAction pendingAction;

	public boolean shouldMove;

	public UiAction(
			@NonNull ActionType type,
			@Nullable UiAction pendingAction,
			@Nullable Cluster<LocationItem> cluster,
			@Nullable LocationItem item,
			@Nullable Location mapLocation,
			long locationId,
			boolean shouldMove) {
		this.type = type;
		this.pendingAction = pendingAction;
		this.cluster = cluster;
		this.item = item;
		this.mapLocation = mapLocation;
		this.locationId = locationId;
		this.shouldMove = shouldMove;
	}

	public static UiAction openFilters() {
		return new UiAction(
				OPEN_FILTERS,
				null,
				null,
				null,
				null,
				Statement.NO_ID,
				false
		);
	}

	public static UiAction showFullMap() {
		return new UiAction(
				SHOW_FULL_MAP,
				null,
				null,
				null,
				null,
				Statement.NO_ID,
				false
		);
	}

	public static UiAction moveToMyLocation(Location mapLocation) {
		return new UiAction(
				MOVE_TO_MY_LOCATION,
				null,
				null,
				null,
				mapLocation,
				Statement.NO_ID,
				true
		);
	}

	public static UiAction moveToFootprint() {
		return new UiAction(
				MOVE_TO_FOOTPRINT,
				null,
				null,
				null,
				null,
				Statement.NO_ID,
				true
		);
	}

	public static UiAction moveToCluster(@Nullable Cluster<LocationItem> cluster) {
		return new UiAction(
				MOVE_TO_CLUSTER,
				null,
				cluster,
				null,
				null,
				Statement.NO_ID,
				true
		);
	}

	public static UiAction moveToAndOpenLocation(@Nullable LocationItem item) {
		return new UiAction(
				MOVE_TO_AND_OPEN_LOCATION,
				null,
				null,
				item,
				null,
				Statement.NO_ID,
				true
		);
	}

	//TODO What if locationId <= NO_ID?
	public static UiAction moveToAndOpenLocation(long locationId) {
		return new UiAction(
				MOVE_TO_AND_OPEN_LOCATION,
				null,
				null,
				null,
				null,
				locationId,
				true
		);
	}

	public static UiAction back() {
		return new UiAction(
				BACK,
				null,
				null,
				null,
				null,
				Statement.NO_ID,
				false
		);
	}

	public static UiAction restore(@Nullable UiAction pendingAction) {
		return new UiAction(
				RESTORE,
				pendingAction,
				null,
				null,
				null,
				Statement.NO_ID,
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
