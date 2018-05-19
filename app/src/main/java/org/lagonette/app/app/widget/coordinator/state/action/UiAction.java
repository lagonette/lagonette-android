package org.lagonette.app.app.widget.coordinator.state.action;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.CrashReporter;
import org.lagonette.app.tools.Logger;
import org.zxcv.functions.main.Consumer;
import org.zxcv.functions.main.LongConsumer;
import org.zxcv.functions.main.Runnable;

public abstract class UiAction {

	public static class Callbacks {

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
		public Runnable loadFilters = Runnable::doNothing;

		@NonNull
		public Runnable unloadFilters = Runnable::doNothing;

		@NonNull
		public LongConsumer loadLocationDetail = LongConsumer::doNothing;

		@NonNull
		public Runnable unloadLocationDetail = Runnable::doNothing;

		@NonNull
		public Runnable wait = Runnable::doNothing;

		public void setupLoggers() {
			if (BuildConfig.DEBUG) {
				finishAction = Logger.log(
						TAG, "INTENT --> finish",
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

	private static final String TAG = "UiAction";

	@Nullable
	public final UiAction pendingAction;

	public UiAction() {
		this(null);
	}

	public UiAction(@Nullable UiAction pendingAction) {
		this.pendingAction = pendingAction;
	}

	public abstract void process(@NonNull UiState state, @NonNull Callbacks callbacks);

	protected void wtf(@NonNull UiState state) {
		CrashReporter.log(Log.ERROR, TAG, state.toString());
		CrashReporter.logException(new IllegalArgumentException("UiAction received a weird state"));
	}
}
