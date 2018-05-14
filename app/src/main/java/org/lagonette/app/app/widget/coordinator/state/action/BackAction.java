package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class BackAction
		extends UiAction {

	public BackAction() {
		super();
	}

	public void process(@NonNull UiState state, @NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
				callbacks.finishAction.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
				callbacks.closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				callbacks.finishAction.run();
				break;
		}
	}
}
