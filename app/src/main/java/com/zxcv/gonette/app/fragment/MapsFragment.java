package com.zxcv.gonette.app.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.zxcv.gonette.BuildConfig;
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.widget.maps.PartnerItem;
import com.zxcv.gonette.app.widget.maps.PartnerRenderer;
import com.zxcv.gonette.content.GonetteContentProviderHelper;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.database.GonetteDatabaseOpenHelper;
import com.zxcv.gonette.util.UiUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapsFragment
        extends Fragment
        implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>,
        ClusterManager.OnClusterClickListener<PartnerItem>,
        ClusterManager.OnClusterItemClickListener<PartnerItem>,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public interface Callback {

        void hideMyLocationButton();

        void showMyLocationButton();

        void showPartner(long partnerId, boolean zoom);

        void showFullMap();

    }

    private Location mLastLocation;

    private GoogleApiClient mGoogleApiClient;

    public static final String TAG = "MapsFragment";

    public static final int ANIMATION_LENGTH_LONG = 600;

    public static final int ANIMATION_LENGTH_SHORT = 300;

    public static final int ZOOM_LEVEL_STREET = 15;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    private GoogleMap mMap;

    private ClusterManager<PartnerItem> mClusterManager;

    private boolean mLocationPermissionGranted = false;

    private boolean mAskFormMyPositionPermission = true;

    private int mStatusBarHeight;

    private Callback mCallback;

//    private PartnerItem mSelectedPartnerItem;

    private Map<Long, PartnerItem> mPartnerItems;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAskFormMyPositionPermission = savedInstanceState.getBoolean(
                    STATE_ASK_FOR_MY_LOCATION_PERMISSION
            );
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(MapsFragment.this)
                    .addOnConnectionFailedListener(MapsFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mStatusBarHeight = UiUtil.getStatusBarHeight(getResources());

        mPartnerItems = new HashMap<>();
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
        mapFragment.getMapAsync(this);

        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() + " must implement " + Callback.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_ASK_FOR_MY_LOCATION_PERMISSION, mAskFormMyPositionPermission);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        setupFootprint();
        queryPartners();
    }

    private void setupMap() {
        mMap.setPadding(0, mStatusBarHeight, 0, 0);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        updateLocationUI();

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
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(MapsFragment.this);
        mClusterManager.setOnClusterItemClickListener(MapsFragment.this);
    }

    private void queryPartners() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_map_partners, null, MapsFragment.this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_map_partners:
                return new CursorLoader(
                        getContext(),
                        GonetteContract.Partner.METADATA_CONTENT_URI,
                        new String[]{
                                GonetteContract.Partner.ID,
                                GonetteContract.Partner.NAME,
                                GonetteContract.Partner.DESCRIPTION,
                                GonetteContract.Partner.LATITUDE,
                                GonetteContract.Partner.LONGITUDE
                        },
                        GonetteContentProviderHelper.getIsVisibleSelection(),
                        null,
                        null
                );
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                onQueryPartnerLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void onQueryPartnerLoadFinished(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            mPartnerItems.clear();
            mClusterManager.clearItems();
            cursor.moveToFirst();
            PartnerReader partnerReader = new PartnerReader(cursor);
            do {
                PartnerItem item = new PartnerItem(
                        partnerReader.getId(),
                        partnerReader.getLatitude(),
                        partnerReader.getLongitude()
                );
                mPartnerItems.put(item.getId(), item);
                mClusterManager.addItem(item);
            } while (partnerReader.moveToNext());
            mClusterManager.cluster();
        } else if (cursor != null && BuildConfig.DEBUG) {
            GonetteDatabaseOpenHelper.parseData(getContext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                // Do nothing.
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    @Override
    public boolean onClusterClick(Cluster<PartnerItem> cluster) {
//        mSelectedPartnerItem = null;
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        cluster.getPosition(),
                        mMap.getCameraPosition().zoom + CLUSTER_CLICK_ZOOM_IN
                ),
                ANIMATION_LENGTH_LONG,
                null
        );
        mCallback.showFullMap();
        return true;
    }

    @Override
    public boolean onClusterItemClick(PartnerItem partnerItem) {
//        mSelectedPartnerItem = partnerItem;
        mCallback.showPartner(partnerItem.getId(), false);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        mSelectedPartnerItem = null;
        mCallback.showFullMap();
    }

    public void processParallaxTranslation(float translationY) {
        int parallaxPadding = -(int) translationY;
        int topPadding = mStatusBarHeight + parallaxPadding;
        mMap.setPadding(0, topPadding, 0, parallaxPadding);
    }


    public void moveOnMyLocation() {
        if (mLocationPermissionGranted) {
            //noinspection MissingPermission
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(
                                        mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude()
                                ),
                                ZOOM_LEVEL_STREET
                        ),
                        ANIMATION_LENGTH_LONG,
                        null
                );
            } else {
                Log.e(TAG, "moveOnMyLocation: Last location is NULL");
            }
        }
    }

    public void showPartner(long id, boolean zoom, GoogleMap.CancelableCallback callback) {
        PartnerItem partnerItem = mPartnerItems.get(id);
        if (partnerItem != null) {
            LatLng latLng = new LatLng(
                    partnerItem.getPosition().latitude,
                    partnerItem.getPosition().longitude
            );
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

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //TODO
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO snackbar
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //TODO snackbar
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            mCallback.showMyLocationButton();
        } else {
            mMap.setMyLocationEnabled(false);
            mCallback.hideMyLocationButton();
        }
    }

    private boolean checkLocationPermission() {
        if (!mLocationPermissionGranted) {
            if (ContextCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else if (mAskFormMyPositionPermission) {
                mAskFormMyPositionPermission = false;
                requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        PERMISSIONS_REQUEST_LOCATION
                );
            }
        }

        return mLocationPermissionGranted;
    }

    private void onLocationPermissionResult(@NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                onLocationPermissionResult(grantResults);
                updateLocationUI();
                break;
            default:
                throw new IllegalArgumentException("Unknown request code: " + requestCode);
        }
    }

    public void startDirection(long partnerId) {
        PartnerItem partnerItem = mPartnerItems.get(partnerId);
        if (partnerItem != null) {
            Intent intent = new Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" + partnerItem.getPosition().latitude + "," + partnerItem.getPosition().longitude)
            );
            startActivity(intent);
        }
    }

}
