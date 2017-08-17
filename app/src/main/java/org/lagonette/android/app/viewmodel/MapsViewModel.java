package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.locator.Repo;
import org.lagonette.android.room.reader.MapPartnerReader;

public class MapsViewModel extends ViewModel {

    private LiveData<MapPartnerReader> mMapPartnersLiveData;

    private MutableLiveData<String> mSearchLiveData;

    public MapsViewModel() {
        mSearchLiveData = new MutableLiveData<>();
        mMapPartnersLiveData = Repo.get().getMapPartners(mSearchLiveData);
        filterPartners(null);
    }

    @NonNull
    public LiveData<MapPartnerReader> getMapPartners() {
        return mMapPartnersLiveData;
    }

    public void filterPartners(@Nullable String search) {
        mSearchLiveData.postValue(search);
    }

}
