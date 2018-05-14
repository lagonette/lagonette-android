package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.UiState;
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

	@Override
	public void process(
			@NonNull UiState state,
			@NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_EXPANDED:
				callbacks.closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_SETTLING:
				callbacks.wait.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case MOVE:
						callbacks.wait.run();
						break;

					case IDLE:
						if (shouldMove) {
							shouldMove = false;
							callbacks.moveMapToCluster.accept(cluster);
						}
						else {
							callbacks.finishAction.run();
						}
						break;
				}
				break;
		}
	}
}
