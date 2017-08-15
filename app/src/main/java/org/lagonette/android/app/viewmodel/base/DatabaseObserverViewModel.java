package org.lagonette.android.app.viewmodel.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.room.InvalidationTracker;
import android.support.annotation.NonNull;

import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.sql.Tables;
import org.lagonette.android.util.DB;

import java.util.Set;

public abstract class DatabaseObserverViewModel extends AndroidViewModel {

    @NonNull
    protected final LaGonetteDatabase mDatabase;

    @NonNull
    private final InvalidationTracker.Observer mDbObserver;

    public DatabaseObserverViewModel(@NonNull Application application) {
        super(application);

        mDbObserver = new InvalidationTracker.Observer(Tables.TABLES) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                onDatabaseInvalidated();
            }
        };

        mDatabase = DB.get(application);

        mDatabase.getInvalidationTracker().addObserver(mDbObserver);
    }

    @Override
    protected void onCleared() {
        mDatabase.getInvalidationTracker().removeObserver(mDbObserver);
    }

    protected abstract void onDatabaseInvalidated();

}
