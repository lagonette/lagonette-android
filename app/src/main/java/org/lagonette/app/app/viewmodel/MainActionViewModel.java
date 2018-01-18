package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.lagonette.app.app.widget.coordinator.state.MainAction;

public class MainActionViewModel extends ViewModel {

    private static final String TAG = "MainCoordinatorActionLi";

    @NonNull
    private final MutableLiveData<MainAction> mActionLiveData;

    public MainActionViewModel() {
        mActionLiveData = new MutableLiveData<>();
    }

    public void start(@Nullable MainAction action) {
        mActionLiveData.setValue(action);
    }

    public void finish(@NonNull MainAction action) {
        Log.d(TAG, "Action -> " + action.type.name() + " done.");
        start(action.pendingAction);
    }

    public LiveData<MainAction> getLiveData() {
        return mActionLiveData;
    }
}
