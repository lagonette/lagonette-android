package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.LocationItem;

public class MoveToClusterAction
		extends UiAction {

	@NonNull
	public final Cluster<LocationItem> cluster;

	public boolean shouldMove;

	public MoveToClusterAction(@NonNull Cluster<LocationItem> cluster) {
		super();
		this.cluster = cluster;
		this.shouldMove = true;

	}
}
