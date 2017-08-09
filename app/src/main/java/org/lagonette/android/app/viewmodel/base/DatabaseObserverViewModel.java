package org.lagonette.android.app.viewmodel.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.util.DB;

import java.util.Set;

public abstract class DatabaseObserverViewModel extends ViewModel {

    @NonNull
    protected final LaGonetteDatabase mDatabase;
    @NonNull
    private final InvalidationTracker.Observer mDbObserver;

    public DatabaseObserverViewModel(@NonNull Context context) {

        mDbObserver = new InvalidationTracker.Observer( // TODO
                "partner", "partner_metadata", "category", "category_metadata", "partner_side_category"
        ) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                onDatabaseInvalidated();
            }
        };

        mDatabase = DB.get(context);

        mDatabase.getInvalidationTracker().addObserver(mDbObserver);
    }

    @Override
    protected void onCleared() {
        mDatabase.getInvalidationTracker().removeObserver(mDbObserver);
    }

    protected abstract void onDatabaseInvalidated();

}
