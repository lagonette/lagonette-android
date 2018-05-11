package org.lagonette.app.app.widget.performer.impl;

import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.zxcv.functions.main.Consumer;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.NOTIFY_MAP_MOVEMENT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.STOP_MOVING;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.UPDATE_MAP_LOCATION_UI;

public abstract class MapFragmentPerformer
		implements ViewPerformer {

	@NonNull
	private final FragmentManager mFragmentManager;

	@NonNull
	public Consumer<UiState.MapMovement> onMapMovementChanged = Consumer::doNothing;

	@NonNull
	protected MainLiveEventBusViewModel mEventBus;

	protected MapsFragment mFragment;

	@NonNull
	protected UiState.MapMovement mMapMovement;

	public MapFragmentPerformer(@NonNull AppCompatActivity activity) {
		mEventBus = ViewModelProviders.of(activity).get(MainLiveEventBusViewModel.class);
		mFragmentManager = activity.getSupportFragmentManager();
		mMapMovement = UiState.MapMovement.IDLE;

		mEventBus.subscribe(
				NOTIFY_MAP_MOVEMENT,
				activity,
				this::notifyMapMovement
		);
	}

	public void loadFragment() {
		mFragment = MapsFragment.newInstance();
		mFragmentManager.beginTransaction()
				.add(R.id.content, mFragment, MapsFragment.TAG)
				.commit();
	}

	public void restoreFragment() {
		mFragment = (MapsFragment) mFragmentManager.findFragmentByTag(MapsFragment.TAG);
	}

	public void openLocation(long locationId) {
		mEventBus.publish(MainLiveEventBusViewModel.Map.OPEN_LOCATION_ID, locationId);
	}

	@NonNull
	public UiState.MapMovement getMapMovement() {
		return mMapMovement;
	}

	private void notifyMapMovement(@NonNull UiState.MapMovement mapMovement) {
		mMapMovement = mapMovement;
		onMapMovementChanged.accept(mMapMovement);
	}

	public void moveToMyLocation(@Nullable Location location) {
		mEventBus.publish(MOVE_TO_MY_LOCATION, location);
	}

	public boolean moveToFootprint() {
		mEventBus.publish(MOVE_TO_FOOTPRINT);
		return true;
	}

	public void moveToCluster(@NonNull Cluster<LocationItem> cluster) {
		mEventBus.publish(MOVE_TO_CLUSTER, cluster);
	}

	public void moveToLocation(@NonNull LocationItem item) {
		mEventBus.publish(MOVE_TO_LOCATION, item);
	}

	public void stopMoving() {
		mEventBus.publish(STOP_MOVING);
	}

	public void updateLocationUI() {
		mEventBus.publish(UPDATE_MAP_LOCATION_UI);
	}
}
