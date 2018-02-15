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
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;
import org.lagonette.app.util.UiUtils;

import java.io.IOException;
import java.util.List;

public class MapPerformer implements OnMapReadyCallback {

    private static final String TAG = "MapPerformer";

    @NonNull
    public Runnable showFullMap = NullFunctions::doNothing;

    @NonNull
    public Consumer<UiState.MapMovement> notifyMapMovement = NullFunctions::doNothing;

    @NonNull
    public Consumer<Cluster<LocationItem>> moveToCluster = NullFunctions::doNothing;

    @NonNull
    public Consumer<LocationItem> openLocationItem = NullFunctions::doNothing;

    @NonNull
    private final Context mContext;

    @Nullable
    private GoogleMap mMap;

    private ClusterManager<LocationItem> mClusterManager;

    private int mStatusBarHeight;

    private LongSparseArray<LocationItem> mPartnerItems;

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
        mClusterManager.setRenderer(
                new PartnerRenderer(
                        mContext,
                        LayoutInflater.from(mContext),
                        mMap,
                        mClusterManager
                )
        );
        mMap.setOnMapClickListener(
                latLng -> showFullMap.run()
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
                lineStringStyle.setColor(ContextCompat.getColor(mContext, R.color.footprint));
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
}
