package org.lagonette.app.app.viewmodel;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.arch.LiveEventBus;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.entity.statement.LocationItem;

public class MainLiveEventBusViewModel extends LiveEventBusViewModel {

    public static final LiveEventBus.Event<LocationItem> OPEN_LOCATION_ITEM = new LiveEventBus.Event<>();

    public static final LiveEventBus.Event<Cluster<LocationItem>> MOVE_TO_CLUSTER = new LiveEventBus.Event<>();

    public static final LiveEventBus.Event<Void> SHOW_FULL_MAP = new LiveEventBus.Event<>();

    public static final LiveEventBus.Event<Long> OPEN_LOCATION_ID = new LiveEventBus.Event<>();

    public static final LiveEventBus.Event<MainState.MapMovement> NOTIFY_MAP_MOVEMENT = new LiveEventBus.Event<>();
}
