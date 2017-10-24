package org.lagonette.app.app.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;
import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.MapsViewModel;
import org.lagonette.app.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.app.app.widget.maps.PartnerRenderer;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.SharedPreferencesUtil;
import org.lagonette.app.util.SnackbarUtil;
import org.lagonette.app.util.UiUtil;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class MapsFragment
        extends Fragment
        implements ClusterManager.OnClusterItemClickListener<PartnerItem>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback {

    public static final int STATE_MOVEMENT_IDLE = 0;

    public static final int STATE_MOVEMENT_MOVE = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_MOVEMENT_IDLE,
            STATE_MOVEMENT_MOVE
    })
    public @interface Movement {
    }

    public interface MapMovementCallback {

        void notifyMapMovementState(@Movement int newState);
    }

    public interface ClusterClickCallback {

        void notifyClusterClick();
    }

    public static final String TAG = "MapsFragment";

    public static final int ANIMATION_LENGTH_LONG = 600;

    public static final int ANIMATION_LENGTH_SHORT = 300;

    public static final int ZOOM_LEVEL_STREET = 15;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    private static final String STATE_SELECTED_MARKER_POSITION = "state:selected_marker_position";

    private static final String STATE_SELECTED_MARKER_ID = "state:selected_marker_id";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    private MapsViewModel mViewModel;

    private SharedMapsActivityViewModel mActivityViewModel;

    private boolean mLocationPermissionGranted = false;

    private GoogleMap mMap;

    private ClusterManager<PartnerItem> mClusterManager;

    private int mStatusBarHeight;

    private boolean mConfChanged = false;

    private double mStartLatitude;

    private double mStartLongitude;

    private float mStartZoom;

    private Marker mSelectedMarker;

    private LatLng mSelectedMarkerPosition = null;

    private long mSelectedMarkerId = Statement.NO_ID;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private boolean mAskFormMyPositionPermission = true;

    private LongSparseArray<PartnerItem> mPartnerItems;

    private MapMovementCallback mMovementCallback;

    private ClusterClickCallback mClusterClickCallback;

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
            mAskFormMyPositionPermission = savedInstanceState.getBoolean(
                    STATE_ASK_FOR_MY_LOCATION_PERMISSION
            );
        } else {
            mConfChanged = false;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            mStartLatitude = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_LATITUDE, SharedPreferencesUtil.DEFAULT_VALUE_START_LATITUDE);
            mStartLongitude = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_LONGITUDE, SharedPreferencesUtil.DEFAULT_VALUE_START_LONGITUDE);
            mStartZoom = sharedPref.getFloat(SharedPreferencesUtil.PREFERENCE_START_ZOOM, SharedPreferencesUtil.DEFAULT_VALUE_START_ZOOM);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(MapsFragment.this)
                    .addOnConnectionFailedListener(MapsFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mPartnerItems = new LongSparseArray<>();

        mStatusBarHeight = UiUtil.getStatusBarHeight(getResources());

        mViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapsViewModel.class);

        mActivityViewModel = ViewModelProviders
                .of(getActivity())
                .get(SharedMapsActivityViewModel.class);

        mActivityViewModel
                .getSearch()
                .observe(
                        MapsFragment.this,
                        search -> mViewModel
                                .getSearch()
                                .send(search)
                );

//        mActivityViewModel
//                .getMapMovement()
//                .observe(
//                        MapsFragment.this,
//                        integer -> {
//                            if (integer != null) {
//                                @MapMovement.Movement int mapMovement = integer;
//                                switch (mapMovement) {
//                                    case MapMovement.FOOTPRINT:
//                                        moveOnFootprint();
//                                        break;
//                                    case MapMovement.MY_LOCATION:
//                                    default:
//                                        moveOnMyLocation();
//                                        break;
//                                }
//                            }
//                        }
//                );
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_ASK_FOR_MY_LOCATION_PERMISSION, mAskFormMyPositionPermission);
        if (mSelectedMarker != null) {
            outState.putParcelable(STATE_SELECTED_MARKER_POSITION, mSelectedMarker.getPosition());
            outState.putLong(STATE_SELECTED_MARKER_ID, mSelectedMarkerId);
        }
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
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        setupFootprint();
        mActivityViewModel.callMapIsReady();

        mViewModel.getMapPartners().observe(
                MapsFragment.this,
                this::dispatchPartnersResource
        );

        mActivityViewModel
                .getMapPadding()
                .observe(
                        MapsFragment.this,
                        paddings -> mMap.setPadding(
                                paddings[0],
                                paddings[1],
                                paddings[2],
                                paddings[3]
                        )
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
        mMap.setOnCameraMoveStartedListener(
                reason -> {
                    if (mMovementCallback != null) {
                        mMovementCallback.notifyMapMovementState(STATE_MOVEMENT_MOVE);
                    }
                }
        );
        mMap.setOnCameraIdleListener(
                () -> {
                    mClusterManager.onCameraIdle();
                    if (mMovementCallback != null) {
                        mMovementCallback.notifyMapMovementState(STATE_MOVEMENT_IDLE);
                    }
                }
        );
        mMap.setOnMarkerClickListener(marker -> { //TODO Factorize ?
            // If cluster manager do not manage marker then the user has probably clicked on the selected marker.
            // If so, we simulate a click on the marker behind.
            if (!mClusterManager.onMarkerClick(marker)) {
                if (marker.getId().equals(mSelectedMarker.getId())) {
                    mActivityViewModel.showLocation(mSelectedMarkerId, false);
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        });
        mClusterManager.setOnClusterClickListener(
                cluster -> {
                    if (mClusterClickCallback != null) {
                        mClusterClickCallback.notifyClusterClick();
                        return true;
                    }
                    return false;
                }
        );
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
            Log.e(TAG, "setupFootprint: " + e.getMessage(), e);
            FirebaseCrash.report(e);
        }
    }

//    @Override
//    public boolean onClusterClick(Cluster<PartnerItem> cluster) {
//        mMap.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                        cluster.getPosition(),
//                        mMap.getCameraPosition().zoom + CLUSTER_CLICK_ZOOM_IN
//                ),
//                ANIMATION_LENGTH_LONG,
//                null
//        );
//        showFullMap();
//        return true;
//    }

    @Override
    public boolean onClusterItemClick(PartnerItem partnerItem) {
        mActivityViewModel.showLocation(partnerItem.getId(), false);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showFullMap();
    }

    private void showFullMap() {
        removeSelectedMarker();
        mActivityViewModel.showFullMap();
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
            mSelectedMarkerId = Statement.NO_ID;
        }
    }

    public void moveOnMyLocation() {
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

    public void stopMoving() {
        mMap.stopAnimation();
    }

    public void showPartner(long id, boolean zoom, @Nullable GoogleMap.CancelableCallback callback) {
        removeSelectedMarker();
        PartnerItem partnerItem = mPartnerItems.get(id);
        if (partnerItem != null) {
            LatLng latLng = new LatLng( //TODO Why not just getPosition ?
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

        boolean permission = checkLocationPermission();
        mMap.setMyLocationEnabled(permission);
        mActivityViewModel.setEnableMyPositionFAB(permission);
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

    public void dispatchPartnersResource(@NonNull Resource<List<PartnerItem>> partnerResource) {
        switch (partnerResource.status) {

            case Resource.ERROR:
                //TODO
                mActivityViewModel.setWorkInProgress(false);
                errorGettingPartners(partnerResource.data.size() == 0);
                showPartners(partnerResource.data);
                break;

            case Resource.LOADING:
                //TODO
                mActivityViewModel.setWorkInProgress(true);
                showPartners(partnerResource.data);
                break;

            case Resource.SUCCESS:
                mActivityViewModel.setWorkInProgress(false);
                showPartners(partnerResource.data);
                break;
        }
    }

    public void showPartners(@Nullable List<PartnerItem> partnerItems) {
        mPartnerItems.clear();
        mClusterManager.clearItems();
        if (partnerItems != null) {
            for (PartnerItem item : partnerItems) { //TODO Improve -> Pass the item or keep in the ViewModel
                mPartnerItems.put(item.getId(), item);
            }
            mClusterManager.addItems(partnerItems);
        }
        mClusterManager.cluster();
    }

    //TODO
    public void errorGettingPartners(boolean noPartnerAtAll) {
        Snackbar
                .make(
                        SnackbarUtil.getViewGroup(getActivity()).getChildAt(0),
                        noPartnerAtAll
                                ? R.string.error_getting_partners_first
                                : R.string.error_getting_partners,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void observeMovement(@Nullable MapMovementCallback callback) {
        mMovementCallback = callback;
    }

    public void observeClusterClick(@Nullable ClusterClickCallback callback) {
        mClusterClickCallback = callback;
    }

}
