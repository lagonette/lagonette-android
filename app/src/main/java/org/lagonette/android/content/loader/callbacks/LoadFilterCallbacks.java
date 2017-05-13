package org.lagonette.android.content.loader.callbacks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.FilterReader;

public class LoadFilterCallbacks extends CursorLoaderCallbacks<LoadFilterCallbacks.Callbacks> {


    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        void setFilterReaders(@Nullable FilterReader reader);

        CursorLoaderParams getFilterLoaderParams(@Nullable Bundle args);

    }

    public LoadFilterCallbacks(
            @NonNull Callbacks callbacks,
            int id) {
        super(callbacks, id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCallbacks.setFilterReaders(FilterReader.create(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCallbacks.setFilterReaders(null);
    }

    @Override
    protected CursorLoaderParams getCursorLoaderParams(@Nullable Bundle args) {
        return mCallbacks.getFilterLoaderParams(args);
    }

    public void loadFilters() {
        initLoader(null);
    }

    public void loadFilters(@Nullable Bundle args) {
        restartLoader(args);
    }

}
