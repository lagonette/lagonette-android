package com.zxcv.gonette.app.fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.contract.MapsContract;
import com.zxcv.gonette.app.presenter.MapsPresenter;
import com.zxcv.gonette.app.widget.maps.PartnerItem;
import com.zxcv.gonette.app.widget.maps.PartnerRenderer;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.util.SharedPreferencesUtil;
import com.zxcv.gonette.util.SnackbarUtil;
import com.zxcv.gonette.util.UiUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapsFragment
        extends Fragment
        implements MapsContract.View,
        ClusterManager.OnClusterClickListener<PartnerItem>,
        ClusterManager.OnClusterItemClickListener<PartnerItem>,
        GoogleMap.OnMapClickListener {

    public interface Callback {

        void hideMyLocationButton();

        void showMyLocationButton();

        void showPartner(long partnerId, boolean zoom);

        void showFullMap();

        void onMapReady();

    }

    private MapsPresenter mPresenter;

    public static final String TAG = "MapsFragment";

    public static final int ANIMATION_LENGTH_LONG = 600;

    public static final int ANIMATION_LENGTH_SHORT = 300;

    public static final int ZOOM_LEVEL_STREET = 15;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    private static final String STATE_SELECTED_MARKER_POSITION = "state:selected_marker_position";

    private static final String STATE_SELECTED_MARKER_ID = "state:selected_marker_id";

    private GoogleMap mMap;

    private ClusterManager<PartnerItem> mClusterManager;

    private int mStatusBarHeight;

    private Callback mCallback;

    private Map<Long, PartnerItem> mPartnerItems;

    private boolean mConfChanged = false;

    private double mStartLatitude;

    private double mStartLongitude;

    private float mStartZoom;

    private Marker mSelectedMarker;

    private LatLng mSelectedMarkerPosition = null;

    private long mSelectedMarkerId = GonetteContract.NO_ID;

    private int mTopPadding;

    private int mBottomPadding;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mConfChanged = true;
            mSelectedMarkerPosition = savedInstanceState.getParcelable(
                    STATE_SELECTED_MARKER_POSITION
            );
            mSelectedMarkerId = savedInstanceState.getLong(
                    STATE_SELECTED_MARKER_ID
            );
        } else {
            mConfChanged = false;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            mStartLatitude = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_LATITUDE, SharedPreferencesUtil.DEFAULT_VALUE_START_LATITUDE);
            mStartLongitude = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_LONGITUDE, SharedPreferencesUtil.DEFAULT_VALUE_START_LONGITUDE);
            mStartZoom = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_ZOOM, SharedPreferencesUtil.DEFAULT_VALUE_START_ZOOM);
        }

        mStatusBarHeight = UiUtil.getStatusBarHeight(getResources());

        mPartnerItems = new HashMap<>();

        mPresenter = new MapsPresenter(MapsFragment.this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mPresenter);

        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() + " must implement " + Callback.class);
        }

        mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveInstanceState(outState);
        if (mSelectedMarker != null) {
            outState.putParcelable(STATE_SELECTED_MARKER_POSITION, mSelectedMarker.getPosition());
            outState.putLong(STATE_SELECTED_MARKER_ID, mSelectedMarkerId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onPause() {
        if (mMap != null) {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPref.edit()
                    .putFloat(SharedPreferencesUtil.PREFERENCE_START_LATITUDE, (float) cameraPosition.target.latitude)
                    .putFloat(SharedPreferencesUtil.PREFERENCE_START_LONGITUDE, (float) cameraPosition.target.longitude)
                    .putFloat(SharedPreferencesUtil.PREFERENCE_START_ZOOM, cameraPosition.zoom)
                    .apply();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        setupFootprint();
        mCallback.onMapReady();
    }

    private void setupMap() {
        mMap.setPadding(0, mStatusBarHeight, 0, 0);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        updateLocationUI();

        if (!mConfChanged) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(
                            mStartLatitude,
                            mStartLongitude
                    ),
                    mStartZoom
            ));
        } else if (mSelectedMarkerPosition != null) {
            addSelectedMarker(mSelectedMarkerId, mSelectedMarkerPosition);
            mSelectedMarkerPosition = null;
        }

        mClusterManager = new ClusterManager<>(getContext(), mMap);
        mClusterManager.setRenderer(
                new PartnerRenderer(
                        getContext(),
                        LayoutInflater.from(getContext()),
                        mMap,
                        mClusterManager
                )
        );
        mMap.setOnMapClickListener(MapsFragment.this);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // If cluster manager do not manage marker then the user has probably clicked on the selected marker.
                // If so, we simulate a click on the marker behind.
                if (!mClusterManager.onMarkerClick(marker)) {
                    if (marker.getId().equals(mSelectedMarker.getId())) {
                        mCallback.showPartner(mSelectedMarkerId, false);
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        });
        mClusterManager.setOnClusterClickListener(MapsFragment.this);
        mClusterManager.setOnClusterItemClickListener(MapsFragment.this);
    }

    private void setupFootprint() {
        try {
            GeoJsonLayer footprintLayer = new GeoJsonLayer(mMap, R.raw.footprint, getContext());
            for (GeoJsonFeature feature : footprintLayer.getFeatures()) {
                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
                lineStringStyle.setColor(ContextCompat.getColor(getContext(), R.color.footprint));
                feature.setLineStringStyle(lineStringStyle);
            }
            footprintLayer.addLayerToMap();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "onMapReady: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean onClusterClick(Cluster<PartnerItem> cluster) {
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        cluster.getPosition(),
                        mMap.getCameraPosition().zoom + CLUSTER_CLICK_ZOOM_IN
                ),
                ANIMATION_LENGTH_LONG,
                null
        );
        showFullMap();
        return true;
    }

    @Override
    public boolean onClusterItemClick(PartnerItem partnerItem) {
        mCallback.showPartner(partnerItem.getId(), false);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showFullMap();
    }

    private void showFullMap() {
        removeSelectedMarker();
        mCallback.showFullMap();
    }

    private void addSelectedMarker(long partnerId, @NonNull LatLng position) {
        mSelectedMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(position)
                        .zIndex(1f)
        );
        mSelectedMarkerId = partnerId;
    }

    private void removeSelectedMarker() {
        if (mSelectedMarker != null) {
            mSelectedMarker.remove();
            mSelectedMarker = null;
            mSelectedMarkerId = GonetteContract.NO_ID;
        }
    }

    public void updateMapPaddingTop(int paddingTop) {
        mTopPadding = paddingTop;
        updateMapPadding();
    }

    public void updateMapPaddingBottom(int bottomPadding) {
        mBottomPadding = bottomPadding;
        updateMapPadding();
    }

    private void updateMapPadding() {
        if (mMap != null) {
            mMap.setPadding(
                    0,
                    mTopPadding,
                    0,
                    mBottomPadding
            );
        }
    }

    public void moveOnMyLocation() {
        Location lastLocation = mPresenter.getLastLocation();
        if (lastLocation != null) {
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    lastLocation.getLatitude(),
                                    lastLocation.getLongitude()
                            ),
                            ZOOM_LEVEL_STREET
                    ),
                    ANIMATION_LENGTH_LONG,
                    null
            );
        }
    }

    public void moveOnFootprint() {
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(
                                SharedPreferencesUtil.DEFAULT_VALUE_START_LATITUDE,
                                SharedPreferencesUtil.DEFAULT_VALUE_START_LONGITUDE
                        ),
                        SharedPreferencesUtil.DEFAULT_VALUE_START_ZOOM
                ),
                ANIMATION_LENGTH_SHORT,
                null
        );
    }

    public void showPartner(long id, boolean zoom, GoogleMap.CancelableCallback callback) {
        removeSelectedMarker();
        PartnerItem partnerItem = mPartnerItems.get(id);
        if (partnerItem != null) {
            LatLng latLng = new LatLng(
                    partnerItem.getPosition().latitude,
                    partnerItem.getPosition().longitude
            );
            addSelectedMarker(partnerItem.getId(), latLng);
            if (zoom) {
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL_STREET),
                        ANIMATION_LENGTH_LONG,
                        callback
                );
            } else {
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLng(latLng),
                        ANIMATION_LENGTH_SHORT,
                        callback
                );
            }
        }
    }

    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (mPresenter.checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            mCallback.showMyLocationButton();
        } else {
            mMap.setMyLocationEnabled(false);
            mCallback.hideMyLocationButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startDirection(long partnerId) {
        PartnerItem partnerItem = mPartnerItems.get(partnerId);
        if (partnerItem != null) {
            LatLng position = partnerItem.getPosition();
            mPresenter.startDirection(position.latitude, position.longitude);
        }
    }

    public void filterPartner(@NonNull String search) {
        mPresenter.loadPartners(search);
    }

    @Override
    public void showPartners(@Nullable PartnerReader partnerReader) {
        if (mMap != null) {
            mPartnerItems.clear();
            mClusterManager.clearItems();
            if (partnerReader != null) {
                if (partnerReader.moveToFirst()) {
                    do {
                        PartnerItem item = new PartnerItem(
                                partnerReader.getId(),
                                partnerReader.getLatitude(),
                                partnerReader.getLongitude()
                        );
                        mPartnerItems.put(item.getId(), item);
                        mClusterManager.addItem(item);
                    } while (partnerReader.moveToNext());
                }
                mClusterManager.cluster();
            }
        }
    }

    @Override
    public void errorNoDirectionAppFound() {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_direction_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

}
