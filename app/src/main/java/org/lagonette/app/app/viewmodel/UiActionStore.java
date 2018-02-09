package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import org.lagonette.app.app.widget.coordinator.state.MainAction;

public class UiActionStore extends ViewModel {

    private final MutableLiveData<MainAction> mActionLiveData;

    public UiActionStore() {
        mActionLiveData = new MutableLiveData<>();
    }

    public LiveData<MainAction> getAction() {
        return mActionLiveData;
    }

    public void startAction(@Nullable MainAction action) {
        mActionLiveData.setValue(action);
    }

    public void finishAction() {
        MainAction nextAction = null;
        MainAction currentAction = mActionLiveData.getValue();
        if (currentAction != null) {
            nextAction = currentAction.pendingAction;
        }
        mActionLiveData.setValue(nextAction);
    }
}
