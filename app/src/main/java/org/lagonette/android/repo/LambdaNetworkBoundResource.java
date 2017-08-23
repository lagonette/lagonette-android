package org.lagonette.android.repo;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.worker.BackgroundWorker;

import java.util.concurrent.Executor;

public class LambdaNetworkBoundResource<ResultType, Worker extends BackgroundWorker>
        extends NetworkBoundResource<ResultType, Worker> {

    public interface FetchChooser {

        boolean shouldFetch();
    }

    public interface DbLoader<ResultType> {

        LiveData<ResultType> loadFromDb();
    }

    @NonNull
    private Executor mExecutor;

    @NonNull
    private FetchChooser mFetchChooser;

    @NonNull
    private DbLoader<ResultType> mDbLoader;

    @NonNull
    private BackgroundWorker.Factory<Worker> mWorkerFactory;

    public LambdaNetworkBoundResource(
            @NonNull Executor executor,
            @NonNull FetchChooser fetchChooser,
            @NonNull DbLoader<ResultType> dbLoader,
            @NonNull BackgroundWorker.Factory<Worker> workerFactory) {
        super();
        mExecutor = executor;
        mFetchChooser = fetchChooser;
        mDbLoader = dbLoader;
        mWorkerFactory = workerFactory;
    }

    @Override
    protected boolean shouldFetch(@Nullable ResultType data) {
        return mFetchChooser.shouldFetch();
    }

    @NonNull
    @Override
    protected Worker createWorker() {
        return mWorkerFactory.create();
    }

    @Override
    protected void executeWorker(@NonNull Worker worker) {
        mExecutor.execute(worker);
    }

    @NonNull
    @Override
    protected LiveData<ResultType> loadFromDb() {
        return mDbLoader.loadFromDb();
    }
}
