package org.lagonette.app.app.widget.performer.impl;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.util.SnackbarUtils;

import javax.inject.Inject;

public class SnackbarPerformer {

    private final AppCompatActivity mActivity;

    //TODO do not use activity
    @Inject
    public SnackbarPerformer(@NonNull AppCompatActivity activity) {
        mActivity = activity;
    }

    public void show(@NonNull Error error) {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(mActivity).getChildAt(0),
                        error.stringRes,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

}
