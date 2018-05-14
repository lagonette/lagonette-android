package org.lagonette.app.app.widget.coordinator.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import org.lagonette.app.util.BottomSheetUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.design.widget.BottomSheetBehavior.STATE_SETTLING;

public class UiState {

	public static class Factory {

		private final int mOrientation;

		public Factory(int orientation) {
			mOrientation = orientation;
		}

		public UiState create(
				@NonNull MapMovement mapMovement,
				@BottomSheetState int bottomSheetState,
				boolean isFiltersLoaded,
				boolean isLocationDetailLoaded,
				long loadedLocationId,
				long selectedLocationId) {
			return new UiState(
					mOrientation,
					mapMovement,
					bottomSheetState,
					isFiltersLoaded,
					isLocationDetailLoaded,
					loadedLocationId,
					selectedLocationId);
		}
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({
			STATE_EXPANDED,
			STATE_COLLAPSED,
			STATE_DRAGGING,
			STATE_SETTLING,
			STATE_HIDDEN
	})
	public @interface BottomSheetState {

	}

	@NonNull
	public final MapMovement mapMovement;

	@BottomSheetState
	public final int bottomSheetState;

	public final boolean isFiltersLoaded;

	public final long loadedLocationId;

	public final boolean isLocationDetailLoaded;

	public final long selectedLocationId;

	public final int orientation;

	public UiState(
			int orientation,
			@NonNull MapMovement mapMovement,
			@BottomSheetState int bottomSheetState,
			boolean isFiltersLoaded,
			boolean isLocationDetailLoaded,
			long loadedLocationId,
			long selectedLocationId) {
		this.orientation = orientation;
		this.mapMovement = mapMovement;
		this.bottomSheetState = bottomSheetState;
		this.isFiltersLoaded = isFiltersLoaded;
		this.isLocationDetailLoaded = isLocationDetailLoaded;
		this.loadedLocationId = loadedLocationId;
		this.selectedLocationId = selectedLocationId;
	}

	@Override
	public String toString() {
		String string = "MainState: [\n";
		string += "\torientation: " + orientation + "\n";
		string += "\tMap movement: ";
		switch (mapMovement) {
			case IDLE:
				string += "IDLE\n";
				break;
			case MOVE:
				string += "MOVE\n";
				break;
		}
		string += "\tBottom sheet state: ";
		string += BottomSheetUtils.toString(bottomSheetState) + "\n";
		string += "\tBottom sheet fragment state: [\n";
		string += "\t\tis filters loaded: " + isFiltersLoaded + "\n";
		string += "\t\tis location detail loaded: " + isLocationDetailLoaded + "\n";
		string += "\t\tloaded location id: " + loadedLocationId + "\n";
		string += "\t\tselected location id: " + selectedLocationId + "\n";
		string += "\t]\n";
		string += "]";
		return string;
	}

	public enum MapMovement {
		IDLE,
		MOVE
	}
}

