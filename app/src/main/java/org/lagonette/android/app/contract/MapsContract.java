package org.lagonette.android.app.contract;


import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.OnMapReadyCallback;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsContract {

    public interface Presenter extends BaseContract.BasePresenter, OnMapReadyCallback {

        void loadPartners();

        void loadPartners(@NonNull String search);

        Location getLastLocation();

        boolean checkLocationPermission();

    }

    public interface View extends BaseContract.BaseView, OnMapReadyCallback {

        void updateLocationUI();

        void showPartners(@Nullable MapPartnerReader partnerReader);

        void errorGettingPartners();
    }
}
