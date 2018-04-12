package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.background.worker.WorkerState;
import org.lagonette.app.locator.Repo;
import org.lagonette.app.tools.arch.LiveEvent;
import org.lagonette.app.tools.arch.MutableLiveEvent;

import static org.lagonette.app.background.worker.WorkerState.ERROR;

public class DataViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final LiveData<Integer> mWorkStatus;

    @NonNull
    private final LiveEvent<Error> mWorkError;

    public DataViewModel(Application application) {
        super(application);

        LiveData<WorkerState> workerState = Repo.get().updateDatas();

        mWorkStatus = Transformations.map(
                workerState,
                state -> state.status
        );

        MutableLiveEvent<Error> workError = new MutableLiveEvent<>();
        workerState.observeForever(state -> {
            if (state != null && state.status == ERROR) {
                workError.sendEvent(state.error);
            }
        });
        mWorkError = workError;

        mSearch = new MutableLiveData<>();
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

    @NonNull
    public LiveData<Error> getWorkError() {
        return mWorkError;
    }

}
