package org.lagonette.app.app.widget.coordinator;

import android.location.Location;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.app.widget.coordinator.state.UiAction;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.Logger;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.tools.functions.main.LongConsumer;
import org.lagonette.app.tools.functions.main.Runnable;
import org.lagonette.app.tools.functions.main.Supplier;

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
	public Consumer<LocationItem> moveMapToLocation = Consumer::doNothing;

	@NonNull
	public Consumer<Location> moveMapToMyLocation = Consumer::doNothing;

	@NonNull
	public Runnable stopMapMoving = Runnable::doNothing;

	@NonNull
	public Runnable moveMapToFootprint = Runnable::doNothing;

	@NonNull
	public LongConsumer openLocation = LongConsumer::doNothing;

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
			switch (state.action.type) {

				case BACK:
					computeBack(state.action, state);
					break;

				case RESTORE:
					computeRestore(state.action, state);
					break;

				case OPEN_FILTERS:
					computeFiltersOpening(state.action, state);
					break;

				case MOVE_TO_MY_LOCATION:
					computeMovementToMyLocation(state.action, state);
					break;

				case MOVE_TO_FOOTPRINT:
					computeMovementToFootprint(state.action, state);
					break;

				case MOVE_TO_CLUSTER:
					computeMovementToCluster(state.action, state);
					break;

				case MOVE_TO_AND_OPEN_LOCATION:
					computeMovementToAndOpeningLocation(state.action, state);
					break;

				case SHOW_FULL_MAP:
					computeFullMapShowing(state.action, state);
					break;

				default:
				case IDLE:
					computeIdle(state.action, state);
					break;
			}
		}
	}

	protected abstract void computeRestore(@NonNull UiAction action, @NonNull UiState state);

	protected abstract void computeFiltersOpening(@NonNull UiAction action, @NonNull UiState state);

	private void computeMovementToMyLocation(@NonNull UiAction action, @NonNull UiState state) {
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

	protected abstract void computeIdle(@NonNull UiAction action, @NonNull UiState state);

	private void computeFullMapShowing(@NonNull UiAction action, @NonNull UiState state) {
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

	protected abstract void computeMovementToAndOpeningLocation(
			@NonNull UiAction action,
			@NonNull UiState state);

	private void computeMovementToCluster(@NonNull UiAction action, @NonNull UiState state) {
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

	private void computeMovementToFootprint(@NonNull UiAction action, @NonNull UiState state) {
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

	private void computeBack(@NonNull UiAction action, @NonNull UiState state) {
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
			moveMapToLocation = Logger.log(
					TAG, "INTENT --> moveMapToLocation",
					moveMapToLocation
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
			openLocation = Logger.log(
					TAG, "INTENT --> openLocation",
					openLocation
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
