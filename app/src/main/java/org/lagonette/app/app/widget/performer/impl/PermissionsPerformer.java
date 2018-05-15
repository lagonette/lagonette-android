package org.lagonette.app.app.widget.performer.impl;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import org.zxcv.functions.main.BooleanConsumer;

public class PermissionsPerformer {

	private static final int PERMISSIONS_REQUEST_LOCATION = 666;

	@NonNull
	private final Activity mActivity;

	@NonNull
	public BooleanConsumer onFineLocationPermissionResult = BooleanConsumer::doNothing;

	public PermissionsPerformer(@NonNull Activity activity) {
		mActivity = activity;
	}

	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_LOCATION:
				onLocationPermissionResult(grantResults);
				break;
			default:
				throw new IllegalArgumentException("Unknown request code: " + requestCode);
		}
	}

	public void onLocationPermissionResult(@NonNull int[] grantResults) {
		// If request is cancelled, the result arrays are empty.
		onFineLocationPermissionResult.accept(grantResults.length > 0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED);
	}

	public void askForFineLocation() {

		boolean permissionGranted =
				ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

		if (!permissionGranted) {
			ActivityCompat.requestPermissions(
					mActivity,
					new String[]{
							Manifest.permission.ACCESS_FINE_LOCATION
					},
					PERMISSIONS_REQUEST_LOCATION
			);
		}
		else {
			onFineLocationPermissionResult.accept(true);
		}
	}

	public boolean checkForFineLocation() {
		return ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}
}
