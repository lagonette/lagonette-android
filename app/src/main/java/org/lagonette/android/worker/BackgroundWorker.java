package org.lagonette.android.worker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public abstract class BackgroundWorker
        implements Runnable {

    // TODO Make worker instantiate WorkerResponse and pass it to abstract method (so implement run() here)

    @NonNull
    protected MutableLiveData<WorkerResponse> mWorkerResponseLiveData;

    public BackgroundWorker() {
        mWorkerResponseLiveData = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<WorkerResponse> getWorkerResponseLiveData() {
        return mWorkerResponseLiveData;
    }

    public interface Factory<Worker extends BackgroundWorker> {

        Worker create();

    }
}
