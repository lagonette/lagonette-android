package com.gonette.android.app.contract.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

public abstract class BaseContract {

    public interface BaseView {

        Context getContext();

        Bundle getArguments();

        LoaderManager getLoaderManager();

        void requestPermissions(@NonNull String[] permissions, int requestCode);

    }

    public interface BasePresenter {

        void onCreate(@Nullable Bundle savedInstanceState);

        void onActivityCreated(@Nullable Bundle savedInstanceState);

        void onStart();

        void onStop();

        void onSaveInstanceState(Bundle outState);

        void onRequestPermissionsResult(
                int requestCode,
                @NonNull String permissions[],
                @NonNull int[] grantResults);

    }

}
