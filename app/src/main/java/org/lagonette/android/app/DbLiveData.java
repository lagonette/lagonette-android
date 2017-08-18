package org.lagonette.android.app;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.InvalidationTracker;
import android.support.annotation.NonNull;

import org.lagonette.android.locator.DB;
import org.lagonette.android.room.sql.Tables;

import java.util.Set;
import java.util.concurrent.Executor;

public class DbLiveData<T>
        extends MutableLiveData<T> {

    public interface DataLoader<T> {

        T loadData();

    }

    @NonNull
    private final InvalidationTracker.Observer mDatabaseObserver;

    @NonNull
    private DataLoader<T> mDataLoader;

    @NonNull
    private final Executor mExecutor;

    public DbLiveData(@NonNull Executor executor, @NonNull DataLoader<T> dataLoader) {
        mExecutor = executor;
        mDataLoader = dataLoader;
        mDatabaseObserver = new InvalidationTracker.Observer(Tables.TABLES) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                loadData();
            }
        };
        loadData();
    }

    @Override
    protected void onActive() {
        super.onActive();
        DB
                .get()
                .getInvalidationTracker()
                .addObserver(mDatabaseObserver);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        DB
                .get()
                .getInvalidationTracker()
                .removeObserver(mDatabaseObserver);
    }

    private void loadData() {
        mExecutor.execute(
                () -> postValue(mDataLoader.loadData())
        );
    }

}
