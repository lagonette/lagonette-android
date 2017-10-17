package org.lagonette.app.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.worker.BackgroundWorker;
import org.lagonette.app.worker.WorkerResponse;


// ResultType: Type for the Resource data
public abstract class ResourceAlgorithm<ResultType, Worker extends BackgroundWorker> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    ResourceAlgorithm() {
    }

    @MainThread
    public ResourceAlgorithm<ResultType, Worker> start() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(
                dbSource,
                data -> {
                    result.removeSource(dbSource);
                    if (shouldUpdate(data)) {
                        result.addSource(
                                dbSource,
                                newData -> result.setValue(Resource.loading(newData))
                        );
                        updateData(dbSource);
                    } else {
                        result.addSource(
                                dbSource,
                                newData -> result.setValue(Resource.success(newData))
                        );
                    }
                }
        );
        return ResourceAlgorithm.this;
    }

    private void updateData(final LiveData<ResultType> dbSource) {
        Worker worker = createWorker();
        LiveData<WorkerResponse> workerSource = worker.getWorkerResponse();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(
                workerSource,
                response -> {
                    result.removeSource(workerSource);
                    result.removeSource(dbSource);
                    //noinspection ConstantConditions
                    if (response.isSuccessful()) {
                        result.addSource( //TODO setValue is never called
                                loadFromDb(), // Re init loader
                                newData -> result.setValue(Resource.success(newData))
                        );
                    } else {
                        onUpdateFailed();
                        result.addSource( //TODO is onChanged() really called ?
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
    protected abstract boolean shouldUpdate(@Nullable ResultType data);

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
    protected void onUpdateFailed() {
    }

    // returns a LiveData that represents the resource
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}