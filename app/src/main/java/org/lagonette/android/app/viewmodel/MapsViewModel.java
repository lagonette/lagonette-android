package org.lagonette.android.app.viewmodel;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.viewmodel.base.DatabaseObserverViewModel;
import org.lagonette.android.room.reader.MapPartnerReader;
import org.lagonette.android.util.SearchUtil;

public class MapsViewModel extends DatabaseObserverViewModel {

    private MutableLiveData<MapPartnerReader> mMapPartnersLiveData;

    @Nullable
    private String mSearch;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        mMapPartnersLiveData = new MutableLiveData<>();
        loadPartners();
    }

    @Override
    protected void onDatabaseInvalidated() {
        updateMapPartners();
    }

    public LiveData<MapPartnerReader> getMapPartners() {
        return mMapPartnersLiveData;
    }

    public void loadPartners() {
        updateMapPartners();
    }

    public void loadPartners(@Nullable String search) {
        mSearch = search;
        updateMapPartners();
    }

    public void updateMapPartners() {
        // TODO use AsyncTask and ensure thread is start only one times
        new Thread(
                () -> mMapPartnersLiveData.postValue(
                        MapPartnerReader.create(
                                mDatabase.mainDao().getMapPartner(SearchUtil.formatSearch(mSearch))
                        )
                )
        ).start();
    }


}
