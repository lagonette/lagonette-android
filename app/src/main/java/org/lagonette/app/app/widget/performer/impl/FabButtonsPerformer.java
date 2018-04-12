package org.lagonette.app.app.widget.performer.impl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.tools.functions.main.Runnable;

public abstract class FabButtonsPerformer implements ViewPerformer {

    @NonNull
    public Consumer<Location> onPositionClick = Consumer::doNothing;

    @NonNull
    public Runnable askForFineLocationPermission = Runnable::doNothing;

    @NonNull
    public Runnable onPositionLongClick = Runnable::doNothing;

    @IdRes
    private int mPositionButtonRes;

    @Nullable
    private FloatingActionButton mPositionFab;

    public FabButtonsPerformer(int positionRes) {
        mPositionButtonRes = positionRes;
    }

    @Override
    public void inject(@NonNull View view) {
        mPositionFab = view.findViewById(mPositionButtonRes);
        if (mPositionFab != null) {
            updatePositionFabClickListener();
            mPositionFab.setOnLongClickListener(
                    button -> {
                        onPositionLongClick.run();
                        return true;
                    }
            );
        }
    }

    private void updatePositionFabClickListener() {
        if (mPositionFab != null) {
            boolean permissionGranted =
                    ActivityCompat.checkSelfPermission(mPositionFab.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (permissionGranted) {
                mPositionFab.setOnClickListener(
                        button -> onPositionClick.accept((Location) button.getTag())
                );
            }
            else {
                mPositionFab.setOnClickListener(
                        button -> askForFineLocationPermission.run()
                );
            }
        }
    }

    public void updateLocation(@Nullable Location location) {
        if (mPositionFab != null) {
            mPositionFab.setTag(location);
        }
    }

    public void notifyFineLocationgranted() {
        updatePositionFabClickListener();
    }
}
