package com.zxcv.gonette.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

public abstract class BaseLoaderCallbacks {

    public interface Callbacks {

        LoaderManager getLoaderManager();

    }

    public abstract void initLoader(int id, @Nullable Bundle args);

    public abstract void restartLoader(int id, @Nullable Bundle args);

}
