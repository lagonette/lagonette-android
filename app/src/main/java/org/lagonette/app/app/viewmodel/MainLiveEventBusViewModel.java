package org.lagonette.app.app.viewmodel;

import android.location.Location;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.tools.arch.LiveEventBus.Event;
import org.lagonette.app.room.entity.statement.LocationItem;

public class MainLiveEventBusViewModel extends LiveEventBusViewModel {

    public interface Action {

        Event<Long> OPEN_LOCATION_ID = new Event<>();

        Event<LocationItem> OPEN_LOCATION_ITEM = new Event<>();

        Event<Cluster<LocationItem>> MOVE_TO_CLUSTER = new Event<>();

        Event<Void> SHOW_FULL_MAP = new Event<>();

        Event<UiState.MapMovement> NOTIFY_MAP_MOVEMENT = new Event<>();
    }

    public interface Map {

        Event<Long> OPEN_LOCATION_ID = new Event<>();

        Event<Location> MOVE_TO_MY_LOCATION = new Event<>();

        Event<Void> MOVE_TO_FOOTPRINT = new Event<>();

        Event<LocationItem> MOVE_TO_LOCATION = new Event<>();

        Event<Cluster<LocationItem>> MOVE_TO_CLUSTER = new Event<>();

        Event<Void> STOP_MOVING = new Event<>();

        Event<Void> UPDATE_MAP_LOCATION_UI = new Event<>();

    }
}
