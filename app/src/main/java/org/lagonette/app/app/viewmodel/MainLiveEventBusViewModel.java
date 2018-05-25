package org.lagonette.app.app.viewmodel;

import android.location.Location;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.arch.LiveEventBus.Event;

public class MainLiveEventBusViewModel
		extends LiveEventBusViewModel {

	public interface Action {

		Event<Long> OPEN_LOCATION_ITEM = new Event<>();

		Event<Cluster<LocationItem>> MOVE_TO_CLUSTER = new Event<>();

		Event<Void> SHOW_FULL_MAP = new Event<>();

		Event<UiState.MapMovement> NOTIFY_MAP_MOVEMENT = new Event<>();

		Event<Void> TOGGLE_BOTTOM_SHEET = new Event<>();
	}

	public interface Map {

		Event<Location> MOVE_TO_MY_LOCATION = new Event<>();

		Event<Void> MOVE_TO_FOOTPRINT = new Event<>();

		Event<Void> MOVE_TO_SELECTED_LOCATION = new Event<>();

		Event<Cluster<LocationItem>> MOVE_TO_CLUSTER = new Event<>();

		Event<Void> STOP_MOVING = new Event<>();

		Event<Void> UPDATE_MAP_LOCATION_UI = new Event<>();

	}
}
