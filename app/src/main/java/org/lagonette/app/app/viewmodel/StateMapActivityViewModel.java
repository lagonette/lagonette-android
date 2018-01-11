package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.repo.Resource;

public class StateMapActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Integer> mWorkStatus;

    public StateMapActivityViewModel(Application application) {
        super(application);

        mSearch = new MutableLiveData<>();
        mWorkStatus = new MutableLiveData<>();

        mSearch.setValue("");
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Integer> getWorkStatus() {
        return mWorkStatus;
    }

    public void setWorkStatus(@Resource.Status int status) {
        mWorkStatus.setValue(status);
    }
}
