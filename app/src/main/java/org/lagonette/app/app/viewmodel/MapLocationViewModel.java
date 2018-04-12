package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.lagonette.app.util.SharedPreferencesUtils;

public class MapLocationViewModel
		extends AndroidViewModel {

	@NonNull
	private final MutableLiveData<CameraPosition> mCameraPosition;

	@NonNull
	private final SharedPreferences mSharedPref;

	public MapLocationViewModel(@NonNull Application application) {
		super(application);
		mCameraPosition = new MutableLiveData<>();
		mSharedPref = PreferenceManager.getDefaultSharedPreferences(application);

		mCameraPosition.setValue(
				CameraPosition.builder()
						.target(new LatLng(
								(double) mSharedPref.getFloat(
										SharedPreferencesUtils.PREFERENCE_START_LATITUDE,
										SharedPreferencesUtils.DEFAULT_VALUE_START_LATITUDE
								),
								(double) mSharedPref.getFloat(
										SharedPreferencesUtils.PREFERENCE_START_LONGITUDE,
										SharedPreferencesUtils.DEFAULT_VALUE_START_LONGITUDE
								)
						))
						.zoom(mSharedPref.getFloat(
								SharedPreferencesUtils.PREFERENCE_START_ZOOM,
								SharedPreferencesUtils.DEFAULT_VALUE_START_ZOOM
						))
						.tilt(mSharedPref.getFloat(
								SharedPreferencesUtils.PREFERENCE_START_TILT,
								SharedPreferencesUtils.DEFAULT_VALUE_START_TILT
						))
						.bearing(mSharedPref.getFloat(
								SharedPreferencesUtils.PREFERENCE_START_BEARING,
								SharedPreferencesUtils.DEFAULT_VALUE_START_BEARING
						))
						.build()
		);
	}

	public void save(@NonNull CameraPosition cameraPosition) {
		mSharedPref.edit()
				.putFloat(SharedPreferencesUtils.PREFERENCE_START_LATITUDE, (float) cameraPosition.target.latitude)
				.putFloat(SharedPreferencesUtils.PREFERENCE_START_LONGITUDE, (float) cameraPosition.target.longitude)
				.putFloat(SharedPreferencesUtils.PREFERENCE_START_ZOOM, cameraPosition.zoom)
				.putFloat(SharedPreferencesUtils.PREFERENCE_START_TILT, cameraPosition.tilt)
				.putFloat(SharedPreferencesUtils.PREFERENCE_START_BEARING, cameraPosition.bearing)
				.apply();

		mCameraPosition.setValue(cameraPosition);
	}

	public LiveData<CameraPosition> getCameraPosition() {
		return mCameraPosition;
	}

}
