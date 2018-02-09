package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.coordinator.state.UiAction;

public class UiActionStore extends ViewModel {

    private final MutableLiveData<UiAction> mActionLiveData;

    public UiActionStore() {
        mActionLiveData = new MutableLiveData<>();
    }

    public LiveData<UiAction> getAction() {
        return mActionLiveData;
    }

    public void startAction(@Nullable UiAction action) {
        mActionLiveData.setValue(action);
    }

    public void finishAction() {
        UiAction nextAction = null;
        UiAction currentAction = mActionLiveData.getValue();
        if (currentAction != null) {
            nextAction = currentAction.pendingAction;
        }
        mActionLiveData.setValue(nextAction);
    }
}
