package org.lagonette.android.app.presenter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.InvalidationTracker;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.android.app.contract.MapsContract;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.loader.callbacks.GetPartnersCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.reader.MapPartnerReader;
import org.lagonette.android.util.DB;

import java.util.Set;

public class MapsPresenter
        extends BundleLoaderPresenter<MapsContract.View>
        implements MapsContract.Presenter,
        BaseLoaderCallbacks.Callbacks,
        GetPartnersCallbacks.Callbacks {

    private static final String TAG = "MapsPresenter";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    @Nullable
    private MutableLiveData<MapPartnerReader> mMapPartnerLiveData;

    @NonNull
    private InvalidationTracker.Observer mDbObserver;

    private GetPartnersCallbacks mGetPartnersCallbacks;

    public MapsPresenter(@NonNull MapsContract.View view) {
        super(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetPartnersCallbacks = new GetPartnersCallbacks(MapsPresenter.this);

        mMapPartnerLiveData = new MutableLiveData<>();

        mDbObserver = new InvalidationTracker.Observer(
                "partner", "partner_metadata", "category", "category_metadata", "partner_side_category"
        ) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                updatePartnerLiveData();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapPartnerLiveData.observe(
                mView.getLifecycleOwner(),
                new Observer<MapPartnerReader>() {
                    @Override
                    public void onChanged(@Nullable MapPartnerReader reader) {
                        mView.showPartners(reader);
                    }
                }
        );

        if (savedInstanceState == null) {
            mGetPartnersCallbacks.getParners();
        }
    }

    @Override
    protected void reattachLoaders() {
        mGetPartnersCallbacks.reattachLoader();
    }

    @Override
    public void onDestroy() {
        DB.get(mView.getContext()).getInvalidationTracker().removeObserver(mDbObserver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mView.onMapReady(googleMap);
        loadPartners();
    }



    @Override
    public void loadPartners() {
        loadPartners(null);
    }

    @Override
    public void loadPartners(@Nullable final String search) {
        final LaGonetteDatabase database = DB.get(mView.getContext());
        database.getInvalidationTracker().addObserver(mDbObserver);
        updatePartnerLiveData();
    }

    private void updatePartnerLiveData() {
        mMapPartnerLiveData.postValue(
                MapPartnerReader.create(
                        DB.get(mView.getContext()).mainDao().getMapPartner()
                )
        );
    }

    @Override
    public void errorGettingPartners() {
        mView.errorGettingPartners();
    }

}
