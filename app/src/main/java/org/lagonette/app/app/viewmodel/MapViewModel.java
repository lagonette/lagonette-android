package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.PartnerItem;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final LiveData<Resource<List<PartnerItem>>> mMapPartnersResource;

    private final MutableLiveData<String> mSearch;

    public MapViewModel() {
        mSearch = new MutableLiveData<>();
        mMapPartnersResource = Repo.get().getMapPartners(mSearch);
    }

    @NonNull
    public LiveData<Resource<List<PartnerItem>>> getMapPartners() {
        return mMapPartnersResource;
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }
}