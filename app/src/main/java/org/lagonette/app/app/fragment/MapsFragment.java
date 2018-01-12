package org.lagonette.app.app.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
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
import android.support.v4.util.LongSparseArray;
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
import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;
import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.viewmodel.StateMapActivityViewModel;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.maps.PartnerRenderer;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.util.SharedPreferencesUtils;
import org.lagonette.app.util.SnackbarUtils;
import org.lagonette.app.util.UiUtils;

import java.io.IOException;
import java.util.List;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.NOTIFY_MAP_MOVEMENT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.SHOW_FULL_MAP;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.OPEN_LOCATION_ID;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.STOP_MOVING;

public class MapsFragment
        extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    public static final String TAG = "MapsFragment";

    public static final int ANIMATION_LENGTH_LONG = 600;

    public static final int ANIMATION_LENGTH_SHORT = 300;

    public static final int ZOOM_LEVEL_STREET = 15;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    private MapViewModel mViewModel;

    private StateMapActivityViewModel mStateViewModel;

    private MainLiveEventBusViewModel mEventBus;

    private boolean mLocationPermissionGranted = false;

    private GoogleMap mMap;

    private ClusterManager<LocationItem> mClusterManager;

    private int mStatusBarHeight;

    private boolean mConfChanged = false;

    private double mStartLatitude;

    private double mStartLongitude;

    private float mStartZoom;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private boolean mAskFormMyPositionPermission = true;

    private LongSparseArray<LocationItem> mPartnerItems;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mConfChanged = true;
            mAskFormMyPositionPermission = savedInstanceState.getBoolean(
                    STATE_ASK_FOR_MY_LOCATION_PERMISSION
            );
        } else {
            mConfChanged = false;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            mStartLatitude = sharedPref.getFloat(SharedPreferencesUtils.PREFERENCE_START_LATITUDE, SharedPreferencesUtils.DEFAULT_VALUE_START_LATITUDE);
            mStartLongitude = sharedPref.getFloat(SharedPreferencesUtils.PREFERENCE_START_LONGITUDE, SharedPreferencesUtils.DEFAULT_VALUE_START_LONGITUDE);
            mStartZoom = sharedPref.getFloat(SharedPreferencesUtils.PREFERENCE_START_ZOOM, SharedPreferencesUtils.DEFAULT_VALUE_START_ZOOM);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(MapsFragment.this)
                    .addOnConnectionFailedListener(MapsFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mPartnerItems = new LongSparseArray<>();

        mStatusBarHeight = UiUtils.getStatusBarHeight(getResources());

        mViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapViewModel.class);

        mStateViewModel = ViewModelProviders
                .of(getActivity())
                .get(StateMapActivityViewModel.class);

        mEventBus = ViewModelProviders
                .of(getActivity())
                .get(MainLiveEventBusViewModel.class);

        mStateViewModel
                .getSearch()
                .observe(
                        MapsFragment.this,
                        mViewModel.getSearch()::setValue
                );
    }

    //TODO Use firebase to find broken data

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
        // Obtain the SupportMapFragment and create notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsFragment.this);

        mEventBus.subscribe(
                MOVE_TO_MY_LOCATION,
                MapsFragment.this,
                this::moveToMyLocation
        );

        mEventBus.subscribe(
                MOVE_TO_FOOTPRINT,
                MapsFragment.this,
                this::moveToFootprint
        );

        mEventBus.subscribe(
                MOVE_TO_LOCATION,
                MapsFragment.this,
                this::moveToLocation
        );

        mEventBus.subscribe(
                MOVE_TO_CLUSTER,
                MapsFragment.this,
                this::moveToCluster
        );

        mEventBus.subscribe(
                STOP_MOVING,
                MapsFragment.this,
                this::stopMoving
        );

        mEventBus.subscribe(
                OPEN_LOCATION_ID,
                MapsFragment.this,
                this::openLocation
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_ASK_FOR_MY_LOCATION_PERMISSION, mAskFormMyPositionPermission);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        if (mMap != null) {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPref.edit()
                    .putFloat(SharedPreferencesUtils.PREFERENCE_START_LATITUDE, (float) cameraPosition.target.latitude)
                    .putFloat(SharedPreferencesUtils.PREFERENCE_START_LONGITUDE, (float) cameraPosition.target.longitude)
                    .putFloat(SharedPreferencesUtils.PREFERENCE_START_ZOOM, cameraPosition.zoom)
                    .apply();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        setupFootprint();

        mViewModel.getMapPartners().observe(
                MapsFragment.this,
                this::dispatchPartnersResource
        );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //TODO Use LiveData
        // Called when location service is connected and my position available.
        // Do nothing here.
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Called when location service is suspended and my position is not available anymore.
        // Do nothing here.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Called when the connection to the location service fail.
        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
        FirebaseCrash.report(
                new IllegalStateException(
                        "Connection to google location service failed: ("
                                + connectionResult.getErrorCode()
                                + ") "
                                + connectionResult.getErrorMessage()
                )
        );
    }

    //TODO Move this in Activity
    public boolean checkLocationPermission() {
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

    private void setupMap() {
        mMap.setPadding(0, mStatusBarHeight, 0, 0); // TODO Useless ?
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
        mMap.setOnMapClickListener(
                latLng -> mEventBus.publish(SHOW_FULL_MAP)
        );
        mMap.setOnCameraMoveStartedListener(
                reason -> mEventBus.publish(NOTIFY_MAP_MOVEMENT, MainState.MapMovement.MOVE)
        );
        mMap.setOnCameraIdleListener(
                () -> {
                    mClusterManager.onCameraIdle();
                    mEventBus.publish(NOTIFY_MAP_MOVEMENT, MainState.MapMovement.IDLE);
                }
        );
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(
                cluster -> {
                    mEventBus.publish(MOVE_TO_CLUSTER, cluster);
                    return true;
                }
        );
        mClusterManager.setOnClusterItemClickListener(
                item -> {
                    mEventBus.publish(OPEN_LOCATION_ITEM, item);
                    return true;
                }
        );

        if (!mConfChanged) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(
                            mStartLatitude,
                            mStartLongitude
                    ),
                    mStartZoom
            ));
        }
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
            Log.e(TAG, "setupFootprint: " + e.getMessage(), e);
            FirebaseCrash.report(e);
        }
    }

    private void openLocation(long locationId) {
        LocationItem locationItem = retrieveLocationItem(locationId);
        mEventBus.publish(OPEN_LOCATION_ITEM, locationItem);
    }

    private void moveToLocation(@NonNull LocationItem item) {
        LatLng latLng = new LatLng( //TODO Why not just getPosition ?
                item.getPosition().latitude,
                item.getPosition().longitude
        );
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL_STREET),
                ANIMATION_LENGTH_LONG,
                null
        );
    }

    private boolean moveToCluster(@NonNull Cluster<LocationItem> cluster) {
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

    private void moveToMyLocation() {
        Location lastLocation = getLastLocation();
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

    private void moveToFootprint() {
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

    private void stopMoving() {
        mMap.stopAnimation();
    }

    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        boolean permission = checkLocationPermission();
        mMap.setMyLocationEnabled(permission);
        //TODO Check the behavior when MyPosition Fab is not enabled
    }

    public Location getLastLocation() {
        if (mLocationPermissionGranted) {
            //noinspection MissingPermission
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {
                Log.w(TAG, "getLastLocation: Last location is NULL");
            }
            return mLastLocation;
        }
        return null;
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

    private void onLocationPermissionResult(@NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
    }

    public void dispatchPartnersResource(@NonNull Resource<List<LocationItem>> partnerResource) {
        showPartners(partnerResource.data);
        mStateViewModel.setWorkStatus(partnerResource.status);
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

    @Nullable
    private LocationItem retrieveLocationItem(long locationId) {
        return mPartnerItems.get(locationId);
    }

    //TODO
    public void errorGettingPartners(boolean noPartnerAtAll) {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(getActivity()).getChildAt(0),
                        noPartnerAtAll
                                ? R.string.error_getting_partners_first
                                : R.string.error_getting_partners,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

}
