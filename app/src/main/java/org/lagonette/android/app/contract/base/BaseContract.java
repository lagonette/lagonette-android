package org.lagonette.android.app.contract.base;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

public abstract class BaseContract {

    public interface BaseView {

        @NonNull
        LifecycleOwner getLifecycleOwner();

        @NonNull
        Context getContext();

        @NonNull
        Bundle getArguments();

        @NonNull
        LoaderManager getLoaderManager();

        void requestPermissions(@NonNull String[] permissions, int requestCode);

    }

    public interface BasePresenter {

        void onCreate(@Nullable Bundle savedInstanceState);

        void onActivityCreated(@Nullable Bundle savedInstanceState);

        void onStart();

        void onStop();

        void onDestroy();

        void onSaveInstanceState(Bundle outState);

        void onRequestPermissionsResult(
                int requestCode,
                @NonNull String permissions[],
                @NonNull int[] grantResults);

    }

}
