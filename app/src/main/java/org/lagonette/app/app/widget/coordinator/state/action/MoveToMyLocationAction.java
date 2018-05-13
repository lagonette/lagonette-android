package org.lagonette.app.app.widget.coordinator.state.action;

import android.location.Location;
import android.support.annotation.NonNull;

public class MoveToMyLocationAction
		extends UiAction {

	@NonNull
	public final Location mapLocation;

	public boolean shouldMove;

	public MoveToMyLocationAction(@NonNull Location mapLocation) {
		super();
		this.mapLocation = mapLocation;
		this.shouldMove = true;
	}
}
