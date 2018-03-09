package org.lagonette.app.app.widget.performer.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.tools.functions.ObjIntConsumer;

public class PermissionsPerformer {

    private static final int PERMISSIONS_REQUEST_LOCATION = 666;

    @NonNull
    public ObjIntConsumer<String[]> requestPermissions = NullFunctions::doNothing;

    @NonNull
    public Runnable onFineLocationPermissionGranted = NullFunctions::doNothing;

    @NonNull
    private final Context mContext;

    public PermissionsPerformer(@NonNull Context context) {
        mContext = context;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onFineLocationPermissionGranted.run();
        }
    }

    //TODO Move this in Activity
    public void askForFineLocation() {

        boolean permissionGranted =
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permissionGranted) {
            requestPermissions.accept(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSIONS_REQUEST_LOCATION
            );
        }
    }
}