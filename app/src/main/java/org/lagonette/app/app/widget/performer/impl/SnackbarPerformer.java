package org.lagonette.app.app.widget.performer.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.util.SnackbarUtils;

public class SnackbarPerformer {

	private final Activity mActivity;

	//TODO do not use activity
	public SnackbarPerformer(Activity activity) {
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
