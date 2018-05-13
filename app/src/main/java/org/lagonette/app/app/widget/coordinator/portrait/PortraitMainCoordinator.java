package org.lagonette.app.app.widget.coordinator.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.coordinator.state.action.IdleAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenFiltersAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.RestoreAction;
import org.lagonette.app.room.statement.Statement;

public class PortraitMainCoordinator
		extends MainCoordinator {

	@Override
	protected void computeRestore(@NonNull RestoreAction action, @NonNull UiState state) {
		if (state.isFiltersLoaded) {
			unloadFilters.run();
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.isLocationDetailLoaded) {
						finishAction.run();
					}
					else {
						closeBottomSheet.run();
					}
					break;

				case BottomSheetBehavior.STATE_SETTLING:
				case BottomSheetBehavior.STATE_DRAGGING:
					closeBottomSheet.run();
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
	protected void computeFiltersOpening(
			@NonNull OpenFiltersAction action,
			@NonNull UiState state) {

		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_HIDDEN:

				if (state.isLocationDetailLoaded) {
					unloadLocationDetail.run();
				}
				else if (state.isFiltersLoaded) {
					openBottomSheet.run();
				}
				else {
					loadFilters.run();
				}
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
				if (state.isLocationDetailLoaded) {
					closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					finishAction.run();
				}
				else {
					wtf(state);
					closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_EXPANDED:
				if (state.isLocationDetailLoaded) {
					closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					finishAction.run();
				}
				else {
					wtf(state);
					closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
				if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
					wtf(state);
					closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					finishAction.run();
				}
				else if (state.isLocationDetailLoaded) {
					finishAction.run();
				}
				else {
					wtf(state);
					closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_SETTLING:
				if (state.isFiltersLoaded && state.isLocationDetailLoaded) {
					wtf(state);
					closeBottomSheet.run();
				}
				else if (state.isFiltersLoaded) {
					wait.run();
				}
				else if (state.isLocationDetailLoaded) {
					closeBottomSheet.run();
				}
				else {
					wtf(state);
					closeBottomSheet.run();
				}
				break;
		}
	}

	@Override
	protected void computeIdle(@NonNull IdleAction action, @NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
			case BottomSheetBehavior.STATE_SETTLING:
				if (!state.isLocationDetailLoaded && !state.isFiltersLoaded) {
					closeBottomSheet.run();
				}
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				if (state.isFiltersLoaded) {
					unloadFilters.run();
				}
				else if (state.isLocationDetailLoaded) {
					unloadLocationDetail.run();
				}
				break;
		}
	}

	@Override
	protected void computeLocationOpening(
			@NonNull OpenLocationAction action,
			@NonNull UiState state) {
		if (action.selectedLocationId <= Statement.NO_ID) {
			finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.selectedLocationId != action.selectedLocationId) {
						selectLocation.accept(action.selectedLocationId);
					}
					else if (action.shouldMove) {
						action.shouldMove = false; //TODO Unidirectional Data flow !
						moveMapToSelectedLocation.run();
					}
					else if (state.loadedLocationId != action.selectedLocationId) {
						loadLocationDetail.accept(action.selectedLocationId);
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
							if (state.selectedLocationId != action.selectedLocationId) {
								selectLocation.accept(action.selectedLocationId);
							}
							else if (action.shouldMove) { //TODO Use reason to mark action done if the user move something
								action.shouldMove = false;
								moveMapToSelectedLocation.run();
							}
							else {
								openBottomSheet.run();
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
					if (state.isFiltersLoaded) {
						unloadFilters.run();
					}
					else {
						loadLocationDetail.accept(action.selectedLocationId);
					}
					break;
			}
		}
	}

}
