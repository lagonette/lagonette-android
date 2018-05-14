package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class MoveToFootprintAction
		extends UiAction {

	public boolean shouldMove;

	public MoveToFootprintAction() {
		super();
		this.shouldMove = true;
	}

	@Override
	public void process(
			@NonNull UiState state,
			@NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
				callbacks.stopMapMoving.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
				callbacks.stopMapMoving.run();
				callbacks.closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case IDLE:
						callbacks.moveMapToFootprint.run();
						break;

					case MOVE:
						callbacks.finishAction.run();
						break;
				}
				break;
		}
	}
}
