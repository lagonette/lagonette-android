package org.lagonette.app.app.widget.coordinator.state.action;

import android.support.annotation.Nullable;

public class RestoreAction
		extends UiAction {

	public RestoreAction(@Nullable UiAction pendingAction) {
		super(pendingAction);
	}
}
