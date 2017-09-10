package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.android.app.arch.EventShipper;
import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsViewModel extends ViewModel {

    private final LiveData<Resource<MapPartnerReader>> mMapPartnersResource;

    private final MutableLiveData<String> mSearch;

    public MapsViewModel() {
        mSearch = new MutableLiveData<>();
        mMapPartnersResource = Repo.get().getMapPartners(mSearch);
    }

    @NonNull
    public LiveData<Resource<MapPartnerReader>> getMapPartners() {
        return mMapPartnersResource;
    }

    @NonNull
    public EventShipper.Sender<String> getSearch() {
        return search -> mSearch.setValue(search);
    }
}
