package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.coordinator.state.action.IdleAction;
import org.lagonette.app.app.widget.coordinator.state.action.UiAction;

import static org.lagonette.app.tools.PrimitiveUtils.ensure;

public class UiActionStore
		extends ViewModel {

	private final MutableLiveData<UiAction> mActionLiveData;

	public UiActionStore() {
		mActionLiveData = new MutableLiveData<>();
	}

	public LiveData<UiAction> getAction() {
		return mActionLiveData;
	}

	public void start(@Nullable UiAction action) {
		mActionLiveData.setValue(action);
	}

	public void process(@NonNull UiState state, @NonNull UiAction.Callbacks callbacks) {
		ensure(
				mActionLiveData.getValue(),
				IdleAction::new
		).process(
				state,
				callbacks
		);
	}

	public void finish() {
		UiAction nextAction = null;
		UiAction currentAction = mActionLiveData.getValue();
		if (currentAction != null) {
			nextAction = currentAction.pendingAction;
		}
		mActionLiveData.setValue(nextAction);
	}
}
