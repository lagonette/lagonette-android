package org.lagonette.app.app.widget.performer.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;
import org.lagonette.app.R;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.maps.PartnerRenderer;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.tools.functions.main.Consumer;
import org.lagonette.app.tools.functions.main.Runnable;
import org.lagonette.app.util.UiUtils;

import java.io.IOException;
import java.util.List;

public class MapPerformer implements OnMapReadyCallback {

    private static final String TAG = "MapPerformer";

    @NonNull
    public Runnable showFullMap = Runnable::doNothing;

    @NonNull
    public Consumer<UiState.MapMovement> notifyMapMovement = Consumer::doNothing;

    @NonNull
    public Consumer<Cluster<LocationItem>> moveToCluster = Consumer::doNothing;

    @NonNull
    public Consumer<LocationItem> openLocationItem = Consumer::doNothing;

    @NonNull
    private final Context mContext;

    @Nullable
    private GoogleMap mMap;

    private ClusterManager<LocationItem> mClusterManager;

    private PartnerRenderer mPartnerRenderer;

    private int mStatusBarHeight;

    private LongSparseArray<LocationItem> mPartnerItems;

    @Nullable
    private Marker mSelectedMarker;

    public MapPerformer(@NonNull Context context) {
        mContext = context;
        mStatusBarHeight = UiUtils.getStatusBarHeight(mContext.getResources());
        mPartnerItems = new LongSparseArray<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setupMap();
        setupFootprint();
    }


    private void setupMap() {
        mMap.setPadding(0, mStatusBarHeight, 0, 0); // TODO Useless ?
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        updateLocationUI();

        mClusterManager = new ClusterManager<>(mContext, mMap);

        mPartnerRenderer = new PartnerRenderer(
                mContext,
                LayoutInflater.from(mContext),
                mMap,
                mClusterManager,
                () -> mSelectedMarker
        );

        mClusterManager.setRenderer(mPartnerRenderer);

        mMap.setOnMapClickListener(
                latLng -> showFullMap()
        );
        mMap.setOnCameraMoveStartedListener(
                reason -> notifyMapMovement.accept(UiState.MapMovement.MOVE)
        );
        mMap.setOnCameraIdleListener(
                () -> {
                    mClusterManager.onCameraIdle();
                    notifyMapMovement.accept(UiState.MapMovement.IDLE);
                }
        );
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(
                cluster -> {
                    moveToCluster.accept(cluster);
                    return true;
                }
        );
        mClusterManager.setOnClusterItemClickListener(
                item -> {
                    openLocationItem.accept(item);
                    return true;
                }
        );
    }

    private void setupFootprint() {
        try {
            GeoJsonLayer footprintLayer = new GeoJsonLayer(mMap, R.raw.footprint, mContext);
            for (GeoJsonFeature feature : footprintLayer.getFeatures()) {
                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
                lineStringStyle.setColor(ContextCompat.getColor(mContext, R.color.map_footprint));
                feature.setLineStringStyle(lineStringStyle);
            }
            footprintLayer.addLayerToMap();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "setupFootprint: " + e.getMessage(), e);
            FirebaseCrash.report(e);
        }
    }

    public void showPartners(@Nullable List<LocationItem> locationItems) {
        mPartnerItems.clear();
        mClusterManager.clearItems();
        if (locationItems != null) {
            for (LocationItem item : locationItems) { //TODO Improve -> Pass the item or keep in the ViewModel
                mPartnerItems.put(item.getId(), item);
            }
            mClusterManager.addItems(locationItems);
        }
        mClusterManager.cluster();
    }

    public void setMapPadding(int left, int top, int right, int bottom) {
        if (mMap != null) {
            mMap.setPadding(
                    left,
                    top,
                    right,
                    bottom
            );
        }
    }

    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        boolean permissionGranted =
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        mMap.setMyLocationEnabled(permissionGranted);
        //TODO Check the behavior when MyPosition Fab is not enabled
    }

    @Nullable
    public LocationItem retrieveLocationItem(long locationId) {
        return mPartnerItems.get(locationId);
    }

    public void selectLocation(@NonNull LocationItem selected) {
        unselectLocation();
        mSelectedMarker = mMap.addMarker(
                mPartnerRenderer.createSelectedMarkerOptions(selected)
        );
    }

    private void unselectLocation() {
        if (mSelectedMarker != null) {
            mSelectedMarker.remove();
            mSelectedMarker = null;
        }
    }

    public void showFullMap() {
        unselectLocation();
        showFullMap.run();
    }
}
