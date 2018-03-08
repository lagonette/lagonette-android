package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.room.entity.statement.LocationItem;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final LiveData<List<LocationItem>> mMapPartners;

    private final MutableLiveData<String> mSearch;

    public MapViewModel() {
        mSearch = new MutableLiveData<>();
        mMapPartners = Repo.get().getMapPartners(mSearch);
    }

    @NonNull
    public LiveData<List<LocationItem>> getMapPartners() {
        return mMapPartners;
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }
}
