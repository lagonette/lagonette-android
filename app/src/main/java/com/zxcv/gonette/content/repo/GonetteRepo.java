package com.zxcv.gonette.content.repo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public abstract class GonetteRepo<C extends GonetteRepo.Callback> {

    public interface Callback {

        @NonNull
        Context getContext();

        @NonNull
        LoaderManager getLoaderManager();

    }

    @NonNull
    protected final C mCallback;

    public GonetteRepo(@NonNull C callback) {
        mCallback = callback;
    }

    @NonNull
    private LoaderManager.LoaderCallbacks<Bundle> mBundleLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bundle>() {
        @Override
        public Loader<Bundle> onCreateLoader(int id, Bundle args) {
            return onCreateBundleLoader(id, args);
        }

        @Override
        public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
            mCallback.getLoaderManager().destroyLoader(loader.getId());
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

    protected void initCursorLoader(int id, @Nullable Bundle args) {
        mCallback.getLoaderManager().initLoader(id, args, mCursorLoaderCallbacks);
    }

    protected void restartCursorLoader(int id, @Nullable Bundle args) {
        mCallback.getLoaderManager().restartLoader(id, args, mCursorLoaderCallbacks);
    }

    protected void initBundleLoader(int id, @Nullable Bundle args) {
        mCallback.getLoaderManager().initLoader(id, args, mBundleLoaderCallbacks);
    }

    protected void restartBundleLoader(int id, @Nullable Bundle args) {
        mCallback.getLoaderManager().restartLoader(id, args, mBundleLoaderCallbacks);
    }

    protected abstract Loader<Cursor> onCreateCursorLoader(int id, Bundle args);

    protected abstract void onCursorLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor);

    protected abstract void onCursorLoaderReset(@NonNull Loader<Cursor> loader);

    protected abstract Loader<Bundle> onCreateBundleLoader(int id, Bundle args);

    protected abstract void onBundleLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data);

    protected abstract void onBundleLoaderReset(@NonNull Loader<Bundle> loader);


}
