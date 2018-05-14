package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public class OpenFiltersAction
		extends OrientedAction {

	public OpenFiltersAction() {
		super();
	}

	@Override
	protected void processForPortrait(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_HIDDEN:

				if (state.isLocationDetailLoaded) {
					callbacks.unloadLocationDetail.run();
				}
				else if (state.isFiltersLoaded) {
					callbacks.openBottomSheet.run();
				}
				else {
					callbacks.loadFilters.run();
				}
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
				if (state.isLocationDetailLoaded) {
					callbacks.closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					callbacks.finishAction.run();
				}
				else {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_EXPANDED:
				if (state.isLocationDetailLoaded) {
					callbacks.closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					callbacks.finishAction.run();
				}
				else {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
				if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					callbacks.finishAction.run();
				}
				else if (state.isLocationDetailLoaded) {
					callbacks.finishAction.run();
				}
				else {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_SETTLING:
				if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					callbacks.wait.run();
				}
				else if (state.isLocationDetailLoaded) {
					callbacks.closeBottomSheet.run();
				}
				else {
					wtf(state);
					callbacks.closeBottomSheet.run();
				}
				break;
		}
	}

	@Override
	protected void processForLandscape(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		callbacks.finishAction.run();
	}
}
