package org.lagonette.android.worker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

public abstract class BackgroundWorker
        implements Runnable {

    @NonNull
    private final Context mContext;

    @NonNull
    private MutableLiveData<WorkerResponse> mWorkerResponseLiveData;

    public BackgroundWorker(@NonNull Context context) {
        mContext = context;
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
    protected Context getContext() {
        return mContext;
    }

    @NonNull
    public LiveData<WorkerResponse> getWorkerResponse() {
        return mWorkerResponseLiveData;
    }

    public interface Factory<Worker extends BackgroundWorker> {

        Worker create();

    }
}
