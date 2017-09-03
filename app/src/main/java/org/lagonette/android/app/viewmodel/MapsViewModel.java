package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.android.app.arch.EventShipper;
import org.lagonette.android.app.arch.MutableLiveEvent;
import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsViewModel extends ViewModel {

    private LiveData<Resource<MapPartnerReader>> mMapPartnersResourceLiveData;

    private EventShipper.Sender<String> mSearchSender;

    public MapsViewModel() {
        MutableLiveEvent<String> searchLiveEvent = new MutableLiveEvent<>();
        mSearchSender = searchLiveEvent;
        mMapPartnersResourceLiveData = Repo.get().getMapPartners(searchLiveEvent);
    }

    @NonNull
    public LiveData<Resource<MapPartnerReader>> getMapPartners() {
        return mMapPartnersResourceLiveData;
    }

    public EventShipper.Sender<String> getSearchSender() {
        return mSearchSender;
    }
}
