package org.lagonette.android.app.contract;


import android.support.annotation.Nullable;

import com.google.android.gms.maps.OnMapReadyCallback;

import org.lagonette.android.app.contract.base.BaseContract;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsContract {

    public interface Presenter extends BaseContract.BasePresenter, OnMapReadyCallback {

        void loadPartners();

        void loadPartners(@Nullable String search);

    }

    public interface View extends BaseContract.BaseView, OnMapReadyCallback {

        void showPartners(@Nullable MapPartnerReader partnerReader);

        void errorGettingPartners();
    }
}
