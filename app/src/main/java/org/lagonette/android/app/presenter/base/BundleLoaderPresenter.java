package org.lagonette.android.app.presenter.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;

public abstract class BundleLoaderPresenter<V extends BaseContract.BaseView>
        extends LoaderPresenter<V> {

    protected BundleLoaderPresenter(@NonNull V view) {
        super(view);
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            reattachLoaders();
        }
    }

    protected abstract void reattachLoaders();

}
