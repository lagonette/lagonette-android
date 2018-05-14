package org.lagonette.app.app.widget.coordinator.state.action;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class MoveToMyLocationAction
		extends UiAction {

	@NonNull
	public final Location mapLocation;

	public MoveToMyLocationAction(@NonNull Location mapLocation) {
		super();
		this.mapLocation = mapLocation;
	}

	@Override
	public void process(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
			case BottomSheetBehavior.STATE_DRAGGING:
				callbacks.wait.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_EXPANDED:
				callbacks.closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case IDLE:
						callbacks.moveMapToMyLocation.accept(mapLocation);
						break;

					case MOVE:
						callbacks.finishAction.run();
						break;
				}
				break;
		}
	}
}
