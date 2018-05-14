package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.statement.Statement;

public class OpenLocationAction
		extends OrientedAction {

	public final long selectedLocationId;

	public boolean shouldMove;

	public OpenLocationAction(long selectedLocationId) {
		super();
		this.selectedLocationId = selectedLocationId;
		this.shouldMove = true;
	}

	@Override
	protected void processForPortrait(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (selectedLocationId <= Statement.NO_ID) {
			callbacks.finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.selectedLocationId != selectedLocationId) {
						callbacks.selectLocation.accept(selectedLocationId);
					}
					else if (shouldMove) {
						shouldMove = false; //TODO Unidirectional Data flow !
						callbacks.moveMapToSelectedLocation.run();
					}
					else if (state.loadedLocationId != selectedLocationId) {
						callbacks.loadLocationDetail.accept(selectedLocationId);
					}
					else {
						callbacks.finishAction.run();
					}
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					callbacks.wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					switch (state.mapMovement) {

						case MOVE:
							callbacks.wait.run();
							break;

						case IDLE:
							if (state.selectedLocationId != selectedLocationId) {
								callbacks.selectLocation.accept(selectedLocationId);
							}
							else if (shouldMove) { //TODO Use reason to mark action done if the user move something
								shouldMove = false;
								callbacks.moveMapToSelectedLocation.run();
							}
							else {
								callbacks.openBottomSheet.run();
							}
							break;
					}
					break;
			}
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					callbacks.closeBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					callbacks.wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					if (state.isFiltersLoaded) {
						callbacks.unloadFilters.run();
					}
					else {
						callbacks.loadLocationDetail.accept(selectedLocationId);
					}
					break;
			}
		}
	}

	@Override
	protected void processForLandscape(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (selectedLocationId <= Statement.NO_ID) {
			callbacks.finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.selectedLocationId != selectedLocationId) {
						callbacks.selectLocation.accept(selectedLocationId);
					}
					else if (shouldMove) {
						shouldMove = false; //TODO Unidirectional Data flow !
						callbacks.moveMapToSelectedLocation.run();
					}
					else if (state.loadedLocationId != selectedLocationId) {
						callbacks.loadLocationDetail.accept(selectedLocationId);
					}
					else {
						callbacks.finishAction.run();
					}
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					callbacks.wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					switch (state.mapMovement) {

						case MOVE:
							callbacks.wait.run();
							break;

						case IDLE:
							if (state.selectedLocationId != selectedLocationId) {
								callbacks.selectLocation.accept(selectedLocationId);
							}
							else if (shouldMove) { //TODO Use reason to mark action done if the user move something
								shouldMove = false; //TODO Unidirectional Data flow !
								callbacks.moveMapToSelectedLocation.run();
							}
							else {
								callbacks.openBottomSheet.run();
							}
							break;
					}
					break;
			}
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					callbacks.closeBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					callbacks.finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					callbacks.wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					callbacks.loadLocationDetail.accept(selectedLocationId);
					break;
			}
		}
	}
}
