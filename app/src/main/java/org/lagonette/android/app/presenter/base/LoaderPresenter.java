package org.lagonette.android.app.presenter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;

public abstract class LoaderPresenter<V extends BaseContract.BaseView>
        extends BasePresenter<V>
        implements BaseLoaderCallbacks.Callbacks {

    protected LoaderPresenter(@NonNull V view) {
        super(view);
    }

    @Override
    public Context getContext() {
        return mView.getContext();
    }

    @Override
    public LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
    }

}
