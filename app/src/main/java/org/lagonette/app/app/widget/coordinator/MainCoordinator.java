package org.lagonette.app.app.widget.coordinator;

import android.location.Location;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.coordinator.state.action.BackAction;
import org.lagonette.app.app.widget.coordinator.state.action.IdleAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToClusterAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToFootprintAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToMyLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenFiltersAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.RestoreAction;
import org.lagonette.app.app.widget.coordinator.state.action.ShowFullMapAction;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.Logger;
import org.zxcv.functions.main.Consumer;
import org.zxcv.functions.main.LongConsumer;
import org.zxcv.functions.main.Runnable;
import org.zxcv.functions.main.Supplier;

public abstract class MainCoordinator {

	private static final String TAG = "MainCoordinator";

	@NonNull
	public Supplier<UiState> getCurrentState;

	@NonNull
	public Runnable finishAction = Runnable::doNothing;

	@NonNull
	public Runnable openBottomSheet = Runnable::doNothing;

	@NonNull
	public Runnable closeBottomSheet = Runnable::doNothing;

	@NonNull
	public Consumer<Cluster<LocationItem>> moveMapToCluster = Consumer::doNothing;

	@NonNull
	public Runnable moveMapToSelectedLocation = Runnable::doNothing;

	@NonNull
	public LongConsumer selectLocation = LongConsumer::doNothing;

	@NonNull
	public Consumer<Location> moveMapToMyLocation = Consumer::doNothing;

	@NonNull
	public Runnable stopMapMoving = Runnable::doNothing;

	@NonNull
	public Runnable moveMapToFootprint = Runnable::doNothing;

	@NonNull
	public Runnable loadMap = Runnable::doNothing;

	@NonNull
	public Runnable restoreMap = Runnable::doNothing;

	@NonNull
	public Runnable restoreFilters = Runnable::doNothing;

	@NonNull
	public Runnable loadFilters = Runnable::doNothing;

	@NonNull
	public Runnable unloadFilters = Runnable::doNothing;

	@NonNull
	public Runnable restoreLocationDetail = Runnable::doNothing;

	@NonNull
	public LongConsumer loadLocationDetail = LongConsumer::doNothing;

	@NonNull
	public Runnable unloadLocationDetail = Runnable::doNothing;

	@NonNull
	protected Runnable wait = Runnable::doNothing;

	public void init(@NonNull UiState state) {
		loadMap.run();

		if (state.bottomSheetState != BottomSheetBehavior.STATE_HIDDEN) {
			closeBottomSheet.run();
		}
	}

	@CallSuper
	public void restore() {
		restoreMap.run();
		restoreFilters.run();
		restoreLocationDetail.run();
	}

	public void process() {
		process(getCurrentState.get());
	}

	private void process(@NonNull UiState state) {
		if (state.action != null) {
			if (state.action instanceof BackAction) {
				computeBack((BackAction) state.action, state);
			}
			else if (state.action instanceof RestoreAction) {
				computeRestore((RestoreAction) state.action, state);
			}
			else if (state.action instanceof OpenFiltersAction) {
				computeFiltersOpening((OpenFiltersAction) state.action, state);
			}
			else if (state.action instanceof MoveToMyLocationAction) {
				computeMovementToMyLocation((MoveToMyLocationAction) state.action, state);
			}
			else if (state.action instanceof MoveToFootprintAction) {
				computeMovementToFootprint((MoveToFootprintAction) state.action, state);
			}
			else if (state.action instanceof MoveToClusterAction) {
				computeMovementToCluster((MoveToClusterAction) state.action, state);
			}
			else if (state.action instanceof OpenLocationAction) {
				computeLocationOpening((OpenLocationAction) state.action, state);
			}
			else if (state.action instanceof ShowFullMapAction) {
				computeFullMapShowing((ShowFullMapAction) state.action, state);
			}
			else {
				computeIdle((IdleAction) state.action, state);
			}
		}
	}

	protected abstract void computeRestore(@NonNull RestoreAction action, @NonNull UiState state);

	protected abstract void computeFiltersOpening(
			@NonNull OpenFiltersAction action,
			@NonNull UiState state);

	private void computeMovementToMyLocation(
			@NonNull MoveToMyLocationAction action,
			@NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
			case BottomSheetBehavior.STATE_DRAGGING:
				wait.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_EXPANDED:
				closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case IDLE:
						moveMapToMyLocation.accept(action.mapLocation);
						break;

					case MOVE:
						finishAction.run();
						break;
				}
				break;
		}
	}

	protected abstract void computeIdle(@NonNull IdleAction action, @NonNull UiState state);

	private void computeFullMapShowing(@NonNull ShowFullMapAction action, @NonNull UiState state) {
		if (state.selectedLocationId > Statement.NO_ID) {
			selectLocation.accept(Statement.NO_ID);
		}
		else {
			switch (state.bottomSheetState) {

				case BottomSheetBehavior.STATE_COLLAPSED:
				case BottomSheetBehavior.STATE_EXPANDED:
					closeBottomSheet.run();
					break;

				case BottomSheetBehavior.STATE_SETTLING:
					wait.run();
					break;

				case BottomSheetBehavior.STATE_DRAGGING:
					finishAction.run();
					break;

				case BottomSheetBehavior.STATE_HIDDEN:
					finishAction.run();
					break;
			}
		}
	}

	protected abstract void computeLocationOpening(
			@NonNull OpenLocationAction action,
			@NonNull UiState state);

	private void computeMovementToCluster(
			@NonNull MoveToClusterAction action,
			@NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_EXPANDED:
				closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_SETTLING:
				wait.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case MOVE:
						wait.run();
						break;

					case IDLE:
						if (action.cluster != null && action.shouldMove) {
							action.shouldMove = false;
							moveMapToCluster.accept(action.cluster);
						}
						else {
							finishAction.run();
						}
						break;
				}
				break;
		}
	}

	private void computeMovementToFootprint(
			@NonNull MoveToFootprintAction action,
			@NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
				stopMapMoving.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
				stopMapMoving.run();
				closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				switch (state.mapMovement) {

					case IDLE:
						moveMapToFootprint.run();
						break;

					case MOVE:
						finishAction.run();
						break;
				}
				break;
		}
	}

	private void computeBack(@NonNull BackAction action, @NonNull UiState state) {
		switch (state.bottomSheetState) {

			case BottomSheetBehavior.STATE_SETTLING:
				finishAction.run();
				break;

			case BottomSheetBehavior.STATE_COLLAPSED:
			case BottomSheetBehavior.STATE_DRAGGING:
			case BottomSheetBehavior.STATE_EXPANDED:
				closeBottomSheet.run();
				break;

			case BottomSheetBehavior.STATE_HIDDEN:
				finishAction.run();
				break;
		}
	}

	protected void wtf(@NonNull UiState state) {
		Crashlytics.log(Log.ERROR, TAG, state.toString());
		Crashlytics.logException(new IllegalArgumentException("Coordinator received a weird state"));
	}

	public void setupLoggers() {
		if (BuildConfig.DEBUG) {
			finishAction = Logger.log(
					TAG, "INTENT --> finishAction",
					finishAction
			);

			openBottomSheet = Logger.log(
					TAG, "INTENT --> openBottomSheet",
					openBottomSheet
			);
			closeBottomSheet = Logger.log(
					TAG, "INTENT --> closeBottomSheet",
					closeBottomSheet
			);

			moveMapToCluster = Logger.log(
					TAG, "INTENT --> moveMapToCluster",
					moveMapToCluster
			);
			moveMapToSelectedLocation = Logger.log(
					TAG, "INTENT --> moveMapToSelectedLocation",
					moveMapToSelectedLocation
			);
			selectLocation = Logger.log(
					TAG, "INTENT --> selectLocation",
					selectLocation
			);
			moveMapToMyLocation = Logger.log(
					TAG, "INTENT --> moveMapToMyLocation",
					moveMapToMyLocation
			);
			stopMapMoving = Logger.log(
					TAG, "INTENT --> stopMapMoving",
					stopMapMoving
			);
			moveMapToFootprint = Logger.log(
					TAG, "INTENT --> moveMapToFootprint",
					moveMapToFootprint
			);

			loadFilters = Logger.log(
					TAG, "INTENT --> loadFilters",
					loadFilters
			);
			unloadFilters = Logger.log(
					TAG, "INTENT --> unloadFilters",
					unloadFilters
			);

			loadLocationDetail = Logger.log(
					TAG, "INTENT --> loadLocationDetail",
					loadLocationDetail
			);
			unloadLocationDetail = Logger.log(
					TAG, "INTENT --> unloadLocationDetail",
					unloadLocationDetail
			);

			wait = Logger.log(
					TAG, "INTENT --X wait",
					Runnable::doNothing
			);
		}
	}

}
