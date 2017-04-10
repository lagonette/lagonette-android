package org.lagonette.android.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class BundleLoaderCallbacks
        extends BaseLoaderCallbacks
        implements LoaderManager.LoaderCallbacks<Bundle> {

    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        Loader<Bundle> onCreateBundleLoader(int id, Bundle args);

        boolean onBundleLoadFinished(@NonNull Loader<Bundle> loader, @Nullable Bundle cursor);

        boolean onBundleLoaderReset(@NonNull Loader<Bundle> loader);

        int[] getBundleLoaderIds();

    }

    @NonNull
    private Callbacks mCallbacks;

    public BundleLoaderCallbacks(@NonNull Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        Loader<Bundle> loader = mCallbacks.onCreateBundleLoader(id, args);
        if (loader == null) {
            throw new IllegalArgumentException("Unknown loader Id: " + id);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle cursor) {
        if (!mCallbacks.onBundleLoadFinished(loader, cursor)) {
            throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
        }
        mCallbacks.getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {
        if (!mCallbacks.onBundleLoaderReset(loader)) {
            throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
        }
    }

    @Override
    public void initLoader(int id, @Nullable Bundle args) {
        // init or reattach
        mCallbacks.getLoaderManager().initLoader(id, args, BundleLoaderCallbacks.this);
    }

    @Override
    public void restartLoader(int id, @Nullable Bundle args) {
        mCallbacks.getLoaderManager().restartLoader(id, args, BundleLoaderCallbacks.this);
    }

    public void reattachLoaders() {
        LoaderManager loaderManager = mCallbacks.getLoaderManager();
        int[] ids = mCallbacks.getBundleLoaderIds();
        for (int id : ids) {
            if (loaderManager.getLoader(id) != null) {
                loaderManager.initLoader(id, null, BundleLoaderCallbacks.this);
            }
        }

    }

}
