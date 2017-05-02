package org.lagonette.android.content.loader.callbacks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnerReader;

public class LoadPartnerCallbacks extends CursorLoaderCallbacks<LoadPartnerCallbacks.Callbacks> {


    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        void setPartnerReader(@Nullable PartnerReader reader);

        CursorLoaderParams getPartnerLoaderParams(@Nullable Bundle args);

    }

    public LoadPartnerCallbacks(
            @NonNull Callbacks callbacks,
            int id) {
        super(callbacks, id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCallbacks.setPartnerReader(PartnerReader.create(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCallbacks.setPartnerReader(null);
    }

    @Override
    protected CursorLoaderParams getCursorLoaderParams(@Nullable Bundle args) {
        return mCallbacks.getPartnerLoaderParams(args);
    }

    public void loadPartners() {
        initLoader(null);
    }

    public void loadPartners(@Nullable Bundle args) {
        restartLoader(args);
    }

}
