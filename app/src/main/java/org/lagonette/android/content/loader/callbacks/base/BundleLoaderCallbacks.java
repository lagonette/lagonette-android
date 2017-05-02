package org.lagonette.android.content.loader.callbacks.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public abstract class BundleLoaderCallbacks<C extends BaseLoaderCallbacks.Callbacks>
        extends BaseLoaderCallbacks<C>
        implements LoaderManager.LoaderCallbacks<Bundle> {

    public BundleLoaderCallbacks(@NonNull C callbacks) {
        super(callbacks);
    }

    @CallSuper
    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle cursor) {
        mCallbacks.getLoaderManager().destroyLoader(loader.getId());
    }

    protected void initLoader(int id, @Nullable Bundle args) {
        // init or reattach
        mCallbacks.getLoaderManager().initLoader(id, args, BundleLoaderCallbacks.this);
    }

    protected void restartLoader(int id, @Nullable Bundle args) {
        mCallbacks.getLoaderManager().restartLoader(id, args, BundleLoaderCallbacks.this);
    }

    public abstract void reattachLoader();

    protected void reattachLoader(int id) {
        LoaderManager loaderManager = mCallbacks.getLoaderManager();
        if (loaderManager.getLoader(id) != null) {
            loaderManager.initLoader(id, null, BundleLoaderCallbacks.this);
        }
    }

}
