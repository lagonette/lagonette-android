package com.zxcv.gonette.app.presenter.base;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public abstract class LoaderPresenter {


    @NonNull
    private LoaderManager.LoaderCallbacks<Bundle> mBundleLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bundle>() {
        @Override
        public Loader<Bundle> onCreateLoader(int id, Bundle args) {
            return onCreateBundleLoader(id, args);
        }

        @Override
        public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
            getLoaderManager().destroyLoader(loader.getId());
            onBundleLoadFinished(loader, data);
        }

        @Override
        public void onLoaderReset(Loader<Bundle> loader) {
            onBundleLoaderReset(loader);
        }
    };

    @NonNull
    private LoaderManager.LoaderCallbacks<Cursor> mCursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return onCreateCursorLoader(id, args);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            onCursorLoadFinished(loader, cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            onCursorLoaderReset(loader);
        }
    };

    @NonNull
    protected abstract LoaderManager getLoaderManager();

    protected void initCursorLoader(int id, @Nullable Bundle args) {
        getLoaderManager().initLoader(id, args, mCursorLoaderCallbacks);
    }

    protected void restartCursorLoader(int id, @Nullable Bundle args) {
        getLoaderManager().restartLoader(id, args, mCursorLoaderCallbacks);
    }

    protected void initBundleLoader(int id, @Nullable Bundle args) {
        getLoaderManager().initLoader(id, args, mBundleLoaderCallbacks);
    }

    protected void restartBundleLoader(int id, @Nullable Bundle args) {
        getLoaderManager().restartLoader(id, args, mBundleLoaderCallbacks);
    }

    @CallSuper
    protected Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        throw new IllegalArgumentException("Unknown loader Id: " + id);
    }

    @CallSuper
    protected void onCursorLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {
        throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
    }

    @CallSuper
    protected void onCursorLoaderReset(@NonNull Loader<Cursor> loader) {
        throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
    }

    @CallSuper
    protected Loader<Bundle> onCreateBundleLoader(int id, Bundle args) {
        throw new IllegalArgumentException("Unknown loader Id: " + id);
    }

    @CallSuper
    protected void onBundleLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
    }

    @CallSuper
    protected void onBundleLoaderReset(@NonNull Loader<Bundle> loader) {
        throw new IllegalArgumentException("Unknown loader Id: " + loader.getId());
    }

}
