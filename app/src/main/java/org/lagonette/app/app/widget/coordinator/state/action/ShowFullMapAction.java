package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.statement.Statement;

public class ShowFullMapAction
		extends UiAction {

	public ShowFullMapAction() {
		super();
	}

	@Override
	public void process(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (state.selectedLocationId > Statement.NO_ID) {
			callbacks.selectLocation.accept(Statement.NO_ID);
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					callbacks.closeBottomSheet.run();
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
