package org.lagonette.app.repo;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.worker.BackgroundWorker;

import java.util.concurrent.Executor;

public class LambdaResourceAlgorithm<ResultType, Worker extends BackgroundWorker>
        extends ResourceAlgorithm<ResultType, Worker> {

    public interface UpdateDecisionMaker {

        boolean shouldUpdate();
    }

    public interface DbLoader<ResultType> {

        LiveData<ResultType> loadFromDb();
    }

    @NonNull
    private Executor mExecutor;

    @NonNull
    private UpdateDecisionMaker mUpdateDecisionMaker;

    @NonNull
    private DbLoader<ResultType> mDbLoader;

    @NonNull
    private BackgroundWorker.Factory<Worker> mWorkerFactory;

    public LambdaResourceAlgorithm(
            @NonNull Executor executor,
            @NonNull UpdateDecisionMaker updateDecisionMaker,
            @NonNull DbLoader<ResultType> dbLoader,
            @NonNull BackgroundWorker.Factory<Worker> workerFactory) {
        super();
        mExecutor = executor;
        mUpdateDecisionMaker = updateDecisionMaker;
        mDbLoader = dbLoader;
        mWorkerFactory = workerFactory;
    }

    @Override
    protected boolean shouldUpdate(@Nullable ResultType data) {
        return mUpdateDecisionMaker.shouldUpdate();
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
