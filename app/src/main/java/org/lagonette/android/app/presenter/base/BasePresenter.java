package org.lagonette.android.app.presenter.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.contract.base.BaseContract;

public abstract class BasePresenter implements BaseContract.BasePresenter {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Callback, do nothing.
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Callback, do nothing.
    }

    @Override
    public void onStart() {
        // Callback, do nothing.
    }

    @Override
    public void onStop() {
        // Callback, do nothing.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Callback, do nothing.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Callback, do nothing.
    }

}
