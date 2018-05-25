package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.statement.Statement;

public class OpenLocationAction
		extends OrientedAction {

	private final long mSelectedLocationId;

	private boolean mMoved;

	private boolean mHasSelected;

	public OpenLocationAction(long selectedLocationId) {
		super();
		this.mSelectedLocationId = selectedLocationId;
		this.mMoved = false;
		this.mHasSelected = false;
	}

	@Override
	protected void processForPortrait(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (mSelectedLocationId <= Statement.NO_ID) {
			callbacks.finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.selectedLocationId != mSelectedLocationId) {
						if (!mHasSelected) {
							callbacks.selectLocation.accept(mSelectedLocationId);
						}
						else {
							callbacks.wait.run();
						}
					}
					else if (!mMoved) {
						mMoved = true;
						callbacks.moveMapToSelectedLocation.run();
					}
					else if (state.loadedLocationId != mSelectedLocationId) {
						callbacks.loadLocationDetail.accept(mSelectedLocationId);
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
							if (state.selectedLocationId != mSelectedLocationId) {
								if (!mHasSelected) {
									callbacks.selectLocation.accept(mSelectedLocationId);
								}
								else {
									callbacks.wait.run();
								}
							}
							else if (!mMoved) {
								mMoved = true;
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
						callbacks.loadLocationDetail.accept(mSelectedLocationId);
					}
					break;
			}
		}
	}

	@Override
	protected void processForLandscape(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (mSelectedLocationId <= Statement.NO_ID) {
			callbacks.finishAction.run();
		}
		else if (state.isLocationDetailLoaded) {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					if (state.selectedLocationId != mSelectedLocationId) {
						if (!mHasSelected) {
							callbacks.selectLocation.accept(mSelectedLocationId);
						}
						else {
							callbacks.wait.run();
						}
					}
					else if (!mMoved) {
						mMoved = true;
						callbacks.moveMapToSelectedLocation.run();
					}
					else if (state.loadedLocationId != mSelectedLocationId) {
						callbacks.loadLocationDetail.accept(mSelectedLocationId);
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
							if (state.selectedLocationId != mSelectedLocationId) {
								if (!mHasSelected) {
									callbacks.selectLocation.accept(mSelectedLocationId);
								}
								else {
									callbacks.wait.run();
								}
							}
							else if (mMoved) {
								mMoved = false;
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
					callbacks.loadLocationDetail.accept(mSelectedLocationId);
					break;
			}
		}
	}
}
