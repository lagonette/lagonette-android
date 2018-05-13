package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.Nullable;

public class UiAction {

	//TODO Use strategy pattern

	@Nullable
	public final UiAction pendingAction;

	public UiAction() {
		this(null);
	}

	public UiAction(@Nullable UiAction pendingAction) {
		this.pendingAction = pendingAction;
	}
}
