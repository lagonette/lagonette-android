package org.lagonette.app.tools.arch;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<Location> {

    @NonNull
    private final Context mContext;

    @Nullable
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @NonNull
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            setValue(locationResult.getLastLocation());
        }
    };

    public LocationLiveData(@NonNull Context context) {
        mContext = context;
    }

    @Override
    protected void onActive() {
        super.onActive();

        boolean permissionsAreGranted =
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionsAreGranted) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onInactive() {
        removeLocationUpdates();
    }

    @NonNull
    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        }
        return mFusedLocationProviderClient;
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    private void requestLocationUpdates() throws SecurityException {
            FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
            LocationRequest locationRequest = LocationRequest.create();
            Looper looper = Looper.myLooper();
            locationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, looper);
    }

    private void removeLocationUpdates() throws SecurityException {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }
}