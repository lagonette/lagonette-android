package com.zxcv.gonette.app.contract;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

public abstract class Contract {

    public interface BaseView<P extends Presenter> {

        Context getContext();

        Bundle getArguments();

        LoaderManager getLoaderManager();

        void setPresenter(P presenter);

    }

    public interface Presenter {

        void onCreate(@Nullable Bundle savedInstanceState);

        void onActivityCreated(@Nullable Bundle savedInstanceState);

    }

}
