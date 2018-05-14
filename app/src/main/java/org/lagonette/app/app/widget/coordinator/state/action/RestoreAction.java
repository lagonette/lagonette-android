package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class RestoreAction
		extends OrientedAction {

	public RestoreAction(@Nullable UiAction pendingAction) {
		super(pendingAction);
	}

	@Override
	protected void processForPortrait(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (state.isFiltersLoaded) {
			callbacks.unloadFilters.run();
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.isLocationDetailLoaded) {
						callbacks.finishAction.run();
					}
					else {
						callbacks.closeBottomSheet.run();
					}
					break;

				case BottomSheetBehavior.STATE_SETTLING:
				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.closeBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					if (state.isLocationDetailLoaded) {
						callbacks.unloadLocationDetail.run();
					}
					else {
						callbacks.finishAction.run();
					}
					break;
			}
		}
	}

	@Override
	protected void processForLandscape(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (!state.isFiltersLoaded) {
			callbacks.loadFilters.run();
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_EXPANDED:
					if (!state.isLocationDetailLoaded) {
						callbacks.closeBottomSheet.run();
					}
					else {
						callbacks.finishAction.run();
					}
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
				case BottomSheetBehavior.STATE_SETTLING:
				case BottomSheetBehavior.STATE_COLLAPSED:
					if (!state.isLocationDetailLoaded) {
						callbacks.closeBottomSheet.run();
					}
					else {
						callbacks.openBottomSheet.run();
					}
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					if (state.isLocationDetailLoaded) {
						callbacks.unloadLocationDetail.run();
					}
					else {
						callbacks.finishAction.run();
					}
					break;
			}
		}
	}
}
