package org.lagonette.android.content.loader.callbacks.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

public abstract class BaseLoaderCallbacks<C extends BaseLoaderCallbacks.Callbacks> {

    public interface Callbacks {

        Context getContext();

        LoaderManager getLoaderManager();
    }

    protected C mCallbacks;

    public BaseLoaderCallbacks(@NonNull C callbacks) {
        this.mCallbacks = callbacks;
    }

}
