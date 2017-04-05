package com.zxcv.gonette.app.contract;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

public abstract class BaseContract {

    public interface BaseView {

        Context getContext();

        Bundle getArguments();

        LoaderManager getLoaderManager();

    }

    public interface BasePresenter {

        void start(@Nullable Bundle savedInstanceState);

    }

}
