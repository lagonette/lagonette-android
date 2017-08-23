package org.lagonette.android.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.worker.BackgroundWorker;
import org.lagonette.android.worker.WorkerResponse;


// ResultType: Type for the Resource data
public abstract class NetworkBoundResource<ResultType, Worker extends BackgroundWorker> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource() {
    }

    @MainThread
    public NetworkBoundResource<ResultType, Worker> start() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(
                dbSource,
                data -> {
                    result.removeSource(dbSource);
                    if (shouldFetch(data)) {
                        result.addSource(
                                dbSource,
                                newData -> result.setValue(Resource.loading(newData))
                        );
                        fetchFromNetwork(dbSource);
                    } else {
                        result.addSource(
                                dbSource,
                                newData -> result.setValue(Resource.success(newData))
                        );
                    }
                }
        );
        return NetworkBoundResource.this;
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        Worker worker = createWorker();
        LiveData<WorkerResponse> workerSource = worker.getWorkerResponseLiveData();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(
                workerSource,
                response -> {
                    result.removeSource(workerSource);
                    result.removeSource(dbSource);
                    //noinspection ConstantConditions
                    if (response.isSuccessful()) {
                        result.addSource(
                                loadFromDb(), // Re init loader
                                newData -> result.setValue(Resource.success(newData))
                        );
                    } else {
                        onFetchFailed();
                        result.addSource( // TODO is onChanged() really called ?
                                dbSource,
                                newData -> result.setValue(
                                        Resource.error(response.getErrorMessage(), newData)
                                )
                        );
                    }
                }
        );

        executeWorker(worker);
    }

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @MainThread
    @NonNull
    protected abstract Worker createWorker();

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract void executeWorker(@NonNull Worker worker);

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }

    // returns a LiveData that represents the resource
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}