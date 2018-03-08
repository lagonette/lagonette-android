package org.lagonette.app.background.worker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

public abstract class BackgroundWorker
        implements Runnable {

    @NonNull
    private final Context mContext;

    @NonNull
    private MutableLiveData<WorkerState> mWorkerState;

    public BackgroundWorker(@NonNull Context context) {
        mContext = context;
        mWorkerState = new MutableLiveData<>();
    }

    @Override
    public void run() {
        mWorkerState.postValue(WorkerState.loading());

        WorkerState workerState = doWork();

        mWorkerState.postValue(workerState);
    }

    @NonNull
    protected abstract WorkerState doWork();

    @NonNull
    protected Context getContext() {
        return mContext;
    }

    @NonNull
    public LiveData<WorkerState> getWorkerState() {
        return mWorkerState;
    }

}
