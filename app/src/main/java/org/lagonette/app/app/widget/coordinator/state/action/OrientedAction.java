package org.lagonette.app.app.widget.coordinator.state.action;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.coordinator.state.UiState;

public abstract class OrientedAction
		extends UiAction {

	public OrientedAction() {
	}

	public OrientedAction(@Nullable UiAction pendingAction) {
		super(pendingAction);
	}

	@Override
	public void process(
			@NonNull UiState state, @NonNull Callbacks callbacks) {
		if (state.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			processForLandscape(state, callbacks);
		}
		else {
			processForPortrait(state, callbacks);
		}
	}

	protected abstract void processForPortrait(
			@NonNull UiState state,
			@NonNull Callbacks callbacks);

	protected abstract void processForLandscape(
			@NonNull UiState state,
			@NonNull Callbacks callbacks);
}
