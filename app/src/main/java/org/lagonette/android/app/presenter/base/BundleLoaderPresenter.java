package org.lagonette.android.app.presenter.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

public abstract class BundleLoaderPresenter
        extends BasePresenter {

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            reattachLoaders();
        }
    }

    protected abstract void reattachLoaders();

}
