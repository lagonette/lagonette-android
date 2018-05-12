package org.lagonette.app.app.widget.coordinator.landscape;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.UiAction;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.statement.Statement;

public class LandscapeMainCoordinator
		extends MainCoordinator {

	@Override
	public void init(@NonNull UiState state) {
		super.init(state);

		if (!state.isFiltersLoaded) {
			loadFilters.run();
		}
	}

	@Override
	protected void computeRestore(@NonNull UiAction action, @NonNull UiState state) {
		if (!state.isFiltersLoaded) {
			loadFilters.run();
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_EXPANDED:
					if (!state.isLocationDetailLoaded) {
						closeBottomSheet.run();
					}
					else {
						finishAction.run();
					}
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
				case BottomSheetBehavior.STATE_SETTLING:
				case BottomSheetBehavior.STATE_COLLAPSED:
					if (!state.isLocationDetailLoaded) {
						closeBottomSheet.run();
					}
					else {
						openBottomSheet.run();
					}
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					if (state.isLocationDetailLoaded) {
						unloadLocationDetail.run();
					}
					else {
						finishAction.run();
					}
					break;
			}
		}
	}

	@Override
	protected void computeFiltersOpening(@NonNull UiAction action, @NonNull UiState state) {
		finishAction.run();
	}

	@Override
	protected void computeIdle(@NonNull UiAction action, @NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
				closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_SETTLING:
				wait.run();
				break;

			case BottomSheetBehavior.STATE_EXPANDED:
				if (!state.isLocationDetailLoaded) {
					closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				if (state.isLocationDetailLoaded) {
					unloadLocationDetail.run();
				}
				break;
		}
	}

	@Override
	protected void computeMovementToAndOpeningLocation(
			@NonNull UiAction action,
			@NonNull UiState state) {
		if (action.item == null) {
			finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					long selectedId = action.item.getId();
					if (action.shouldMove) {
						action.shouldMove = false; //TODO Unidirectional Data flow !
						moveMapToLocation.accept(action.item);
					}
					else if (state.loadedLocationId != selectedId) {
						loadLocationDetail.accept(action.item.getId());
					}
					else {
						finishAction.run();
					}
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					switch (state.mapMovement) {

						case MOVE:
							wait.run();
							break;

						case IDLE:
							if (action.item != null) {
								if (action.shouldMove) { //TODO Use reason to mark action done if the user move something
									action.shouldMove = false; //TODO Unidirectional Data flow !
									moveMapToLocation.accept(action.item);
								}
								else {
									openBottomSheet.run();
								}
							}
							else {
								finishAction.run();
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
					closeBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					finishAction.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					wait.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					loadLocationDetail.accept(action.item.getId());
					break;
			}
		}
	}
}
