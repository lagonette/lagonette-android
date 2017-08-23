package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsViewModel extends ViewModel {

    private LiveData<Resource<MapPartnerReader>> mMapPartnersResourceLiveData;

    private MutableLiveData<String> mSearchLiveData;

    public MapsViewModel() {
        mSearchLiveData = new MutableLiveData<>();
        mMapPartnersResourceLiveData = Repo.get().getMapPartners(mSearchLiveData);
        filterPartners(null);
    }

    @NonNull
    public LiveData<Resource<MapPartnerReader>> getMapPartners() {
        return mMapPartnersResourceLiveData;
    }

    public void filterPartners(@Nullable String search) {
        mSearchLiveData.postValue(search);
    }

}
