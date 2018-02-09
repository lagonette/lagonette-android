package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;

public class UiActionStore extends ViewModel {

    private final MutableLiveData<MainAction> mActionLiveData;

    public UiActionStore() {
        mActionLiveData = new MutableLiveData<>();
    }

    public LiveData<MainAction> getAction() {
        return mActionLiveData;
    }

    public void startAction(@NonNull MainAction action) {
        mActionLiveData.setValue(action);
    }

    public void finishAction() {
        mActionLiveData.setValue(null);
    }
}
