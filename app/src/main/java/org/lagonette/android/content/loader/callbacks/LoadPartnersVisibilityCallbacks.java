package org.lagonette.android.content.loader.callbacks;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnersVisibilityReader;

public class LoadPartnersVisibilityCallbacks
        extends CursorLoaderCallbacks<LoadPartnersVisibilityCallbacks.Callbacks> {

    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        void setPartnersVisibilityReader(@Nullable PartnersVisibilityReader reader);

        CursorLoaderParams getPartnersVisibilityLoaderParams(@Nullable Bundle args);

    }

    public LoadPartnersVisibilityCallbacks(@NonNull Callbacks callbacks, int id) {
        super(callbacks, id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCallbacks.setPartnersVisibilityReader(
                PartnersVisibilityReader.create(cursor)
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCallbacks.setPartnersVisibilityReader(null);
    }

    @Override
    protected CursorLoaderParams getCursorLoaderParams(@Nullable Bundle args) {
        return mCallbacks.getPartnersVisibilityLoaderParams(args);
    }

    public void loadPartnersVisibility() {
        initLoader(null);
    }
}
