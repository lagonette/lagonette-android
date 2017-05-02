package org.lagonette.android.content.loader.callbacks.base;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.lagonette.android.content.loader.CursorLoaderParams;

public abstract class CursorLoaderCallbacks<C extends BaseLoaderCallbacks.Callbacks>
        extends BaseLoaderCallbacks<C>
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int mId;

    public CursorLoaderCallbacks(
            @NonNull C callbacks,
            int id) {
        super(callbacks);
        mId = id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoaderParams cursorLoaderParams = getCursorLoaderParams(args);
        return new CursorLoader(
                mCallbacks.getContext(),
                cursorLoaderParams.getUri(),
                cursorLoaderParams.getProjection(),
                cursorLoaderParams.getSelection(),
                cursorLoaderParams.getSelectionArgs(),
                cursorLoaderParams.getSortOrder()
        );
    }

    protected abstract CursorLoaderParams getCursorLoaderParams(@Nullable Bundle args);

    protected void initLoader(@Nullable Bundle args) {
        // init or reattach
        mCallbacks.getLoaderManager().initLoader(mId, args, CursorLoaderCallbacks.this);
    }

    protected void restartLoader(@Nullable Bundle args) {
        mCallbacks.getLoaderManager().restartLoader(mId, args, CursorLoaderCallbacks.this);
    }

}
