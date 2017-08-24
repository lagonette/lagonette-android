package org.lagonette.android.worker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public abstract class BackgroundWorker
        implements Runnable {

    @NonNull
    private MutableLiveData<WorkerResponse> mWorkerResponseLiveData;

    public BackgroundWorker() {
        mWorkerResponseLiveData = new MutableLiveData<>();
    }

    @Override
    public void run() {
        WorkerResponse response = new WorkerResponse();

        doWork(response);

        mWorkerResponseLiveData.postValue(response);
    }

    protected abstract void doWork(@NonNull WorkerResponse response);

    @NonNull
    public LiveData<WorkerResponse> getWorkerResponseLiveData() {
        return mWorkerResponseLiveData;
    }

    public interface Factory<Worker extends BackgroundWorker> {

        Worker create();

    }
}
