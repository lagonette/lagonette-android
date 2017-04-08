package com.zxcv.gonette.app.presenter.base;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.zxcv.gonette.app.contract.base.BaseContract;

public abstract class LoaderPresenter implements BaseContract.BasePresenter {


    @Nullable
    private LoaderManager.LoaderCallbacks<Bundle> mBundleLoaderCallbacks;

    @Nullable
    private LoaderManager.LoaderCallbacks<Cursor> mCursorLoaderCallbacks;

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onReattachBundleLoader();
        }
    }

    protected abstract void onReattachBundleLoader();

    @NonNull
    protected abstract LoaderManager getLoaderManager();

    private LoaderManager.LoaderCallbacks<Cursor> getCursorLoaderCallbacks() {
        if (mCursorLoaderCallbacks == null) {
            mCursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

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
        }
        return mCursorLoaderCallbacks;
    }

    private LoaderManager.LoaderCallbacks<Bundle> getBundleLoaderCallbacks() {
        if (mBundleLoaderCallbacks == null) {
            mBundleLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bundle>() {
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
        }
        return mBundleLoaderCallbacks;
    }

    protected void initCursorLoader(int id, @Nullable Bundle args) {
        // init or reattach
        getLoaderManager().initLoader(id, args, getCursorLoaderCallbacks());
    }

    protected void restartCursorLoader(int id, @Nullable Bundle args) {
        getLoaderManager().restartLoader(id, args, getCursorLoaderCallbacks());
    }

    protected void initBundleLoader(int id, @Nullable Bundle args) {
        // Check if loader exist, if it exist do not reattach it, it is already done
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager.getLoader(id) == null) {
            loaderManager.initLoader(id, args, getBundleLoaderCallbacks());
        }
    }

    protected void restartBundleLoader(int id, @Nullable Bundle args) {
        getLoaderManager().restartLoader(id, args, getBundleLoaderCallbacks());
    }

    protected void reattachBundleLoader(@NonNull LoaderManager loaderManager, @IdRes int id) {
        if (loaderManager.getLoader(id) != null) {
            loaderManager.initLoader(id, null, getBundleLoaderCallbacks());
        }
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
