package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.LocationDetail;

public class LocationDetailViewModel extends ViewModel {

    @NonNull
    private LiveData<Resource<LocationDetail>> mLocationDetailResource;

    @NonNull
    private MutableLiveData<Long> mLocationId;

    public LocationDetailViewModel() {
        mLocationId = new MutableLiveData<>();
        mLocationDetailResource = Repo.get().getLocationDetail(mLocationId);
    }

    public void setLocationId(long locationId) {
        mLocationId.postValue(locationId);
    }

    @NonNull
    public LiveData<Resource<LocationDetail>> getLocationDetail() {
        return mLocationDetailResource;
    }
}