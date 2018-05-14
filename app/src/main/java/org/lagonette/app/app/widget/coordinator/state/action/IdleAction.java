package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class IdleAction
		extends OrientedAction {

	public IdleAction() {
		super();
	}

	@Override
	protected void processForPortrait(@NonNull UiState state, @NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
			case BottomSheetBehavior.STATE_SETTLING:
				if (!state.isLocationDetailLoaded && !state.isFiltersLoaded) {
					callbacks.closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				if (state.isFiltersLoaded) {
					callbacks.unloadFilters.run();
				}
				else if (state.isLocationDetailLoaded) {
					callbacks.unloadLocationDetail.run();
				}
				break;
		}
	}

	@Override
	protected void processForLandscape(@NonNull UiState state, @NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
				callbacks.closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_SETTLING:
				callbacks.wait.run();
				break;

			case BottomSheetBehavior.STATE_EXPANDED:
				if (!state.isLocationDetailLoaded) {
					callbacks.closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				if (state.isLocationDetailLoaded) {
					callbacks.unloadLocationDetail.run();
				}
				break;
		}
	}
}
