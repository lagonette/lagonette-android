package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class ToggleBottomSheetAction
		extends UiAction {

	private boolean mDone = false;

	public ToggleBottomSheetAction() {
		super();
	}

	@Override
	public void process(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (mDone) {
			callbacks.finishAction.run();
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
					mDone = true;
					callbacks.expandBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_EXPANDED:
					mDone = true;
					callbacks.collapseBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					callbacks.wait.run();
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.finishAction.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					callbacks.finishAction.run();
					break;
			}
		}
	}
}
