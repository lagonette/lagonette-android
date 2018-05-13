package org.lagonette.app.app.widget.coordinator.state.action;

public class OpenLocationAction
		extends UiAction {

	public final long selectedLocationId;

	public boolean shouldMove;

	public OpenLocationAction(long selectedLocationId) {
		super();
		this.selectedLocationId = selectedLocationId;
		this.shouldMove = true;
	}
}
