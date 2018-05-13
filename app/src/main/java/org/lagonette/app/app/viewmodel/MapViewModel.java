package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.statement.Statement;

import java.util.List;

public class MapViewModel
		extends ViewModel {

	private final LiveData<List<LocationItem>> mLocations;

	private final LiveData<LocationItem> mSelectedLocation;

	private final MutableLiveData<Long> mLocationId;

	private final MutableLiveData<String> mSearch;

	public MapViewModel() {
		mSearch = new MutableLiveData<>();
		mLocationId = new MutableLiveData<>();
		mLocations = Repo.get().getMapLocations(mSearch);
		mSelectedLocation = Repo.get().getMapLocation(mLocationId);
	}

	@NonNull
	public LiveData<List<LocationItem>> getLocations() {
		return mLocations;
	}

	@NonNull
	public MutableLiveData<String> getSearch() {
		return mSearch;
	}

	@NonNull
	public LiveData<LocationItem> getSelectedLocation() {
		return mSelectedLocation;
	}

	public long getSelectedLocationId() {
		LocationItem item = mSelectedLocation.getValue();
		return item != null ? item.getId() : Statement.NO_ID;
	}

	public void selectLocation(long locationId) {
		mLocationId.setValue(locationId);
	}
}
