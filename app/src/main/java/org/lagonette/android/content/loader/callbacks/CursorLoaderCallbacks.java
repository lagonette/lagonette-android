package org.lagonette.android.content.loader.callbacks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class CursorLoaderCallbacks
        extends BaseLoaderCallbacks
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        Loader<Cursor> onCreateCursorLoader(int id, Bundle args);

        boolean onCursorLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor);

        boolean onCursorLoaderReset(@NonNull Loader<Cursor> loader);

    }

    @NonNull
    private Callbacks mCallbacks;

    public CursorLoaderCallbacks(@NonNull Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = mCallbacks.onCreateCursorLoader(id, args);
        if (loader == null) {
            throw new IllegalArgumentException("Unknown loader Id: " + id);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!mCallbacks.onCursorLoadFinished(loader, cursor)) {
            throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (!mCallbacks.onCursorLoaderReset(loader)) {
            throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
        }
    }

    @Override
    public void initLoader(int id, @Nullable Bundle args) {
        // init or reattach
        mCallbacks.getLoaderManager().initLoader(id, args, CursorLoaderCallbacks.this);
    }

    @Override
    public void restartLoader(int id, @Nullable Bundle args) {
        mCallbacks.getLoaderManager().restartLoader(id, args, CursorLoaderCallbacks.this);
    }

}
