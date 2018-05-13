package org.lagonette.app.app.widget.performer.impl;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.util.SharedPreferencesUtils;

public class MapMovementPerformer
		implements OnMapReadyCallback {

	public static final int ANIMATION_LENGTH_LONG = 600;

	public static final int ANIMATION_LENGTH_SHORT = 300;

	public static final int ZOOM_LEVEL_STREET = 15;

	public static final int CLUSTER_CLICK_ZOOM_IN = 1;

	@Nullable
	private GoogleMap mMap;

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
	}

	public void moveToLocation(@Nullable LocationItem item) {
		if (mMap != null && item != null) {
			LatLng latLng = item.getPosition();
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL_STREET);
			mMap.animateCamera(
					cameraUpdate,
					ANIMATION_LENGTH_LONG,
					null
			);
		}
	}

	public boolean moveToCluster(@NonNull Cluster<LocationItem> cluster) {
		if (mMap != null) {
			mMap.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							cluster.getPosition(),
							mMap.getCameraPosition().zoom + CLUSTER_CLICK_ZOOM_IN
					),
					ANIMATION_LENGTH_LONG,
					null
			);
			return true;
		}
		else {
			return false;
		}
	}

	public void moveToMyLocation(@Nullable Location location) {
		if (mMap != null) {
			if (location != null) {
				mMap.animateCamera(
						CameraUpdateFactory.newLatLngZoom(
								new LatLng(
										location.getLatitude(),
										location.getLongitude()
								),
								ZOOM_LEVEL_STREET
						),
						ANIMATION_LENGTH_LONG,
						null
				);
			}
		}
	}

	public void moveToFootprint() {
		if (mMap != null) {
			mMap.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							new LatLng(
									SharedPreferencesUtils.DEFAULT_VALUE_START_LATITUDE,
									SharedPreferencesUtils.DEFAULT_VALUE_START_LONGITUDE
							),
							SharedPreferencesUtils.DEFAULT_VALUE_START_ZOOM
					),
					ANIMATION_LENGTH_SHORT,
					null
			);
		}
	}

	public void stopMoving() {
		if (mMap != null) {
			mMap.stopAnimation();
		}
	}

	@Nullable
	public CameraPosition getCameraPosition() {
		return mMap != null ? mMap.getCameraPosition() : null;
	}

	public void setCameraPosition(@NonNull CameraPosition position) {
		if (mMap != null) {
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
		}
	}
}
