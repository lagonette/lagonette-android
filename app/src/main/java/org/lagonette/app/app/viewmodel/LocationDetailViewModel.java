package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.PrimitiveUtils;

public class LocationDetailViewModel
		extends ViewModel {

	@NonNull
	private LiveData<LocationDetail> mLocationDetail;

	@NonNull
	private MutableLiveData<Long> mLocationId;

	public LocationDetailViewModel() {
		mLocationId = new MutableLiveData<>();
		mLocationDetail = Repo.get().getLocationDetail(mLocationId);
	}

	public void setLocationId(long locationId) {
		mLocationId.setValue(locationId);
	}

	@NonNull
	public LiveData<LocationDetail> getLocationDetail() {
		return mLocationDetail;
	}

	public long getLoadedLocationId() {
		return PrimitiveUtils.unbox(mLocationId.getValue(), Statement.NO_ID);
	}
}
