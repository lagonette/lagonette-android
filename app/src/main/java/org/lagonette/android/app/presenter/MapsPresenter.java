package org.lagonette.android.app.presenter;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.InvalidationTracker;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.app.contract.MapsContract;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.loader.callbacks.GetPartnersCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.reader.MapPartnerReader;
import org.lagonette.android.util.DB;

import java.util.Set;

public class MapsPresenter
        extends BundleLoaderPresenter<MapsContract.View>
        implements MapsContract.Presenter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        BaseLoaderCallbacks.Callbacks,
        GetPartnersCallbacks.Callbacks {

    private static final String TAG = "MapsPresenter";

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    @Nullable
    private MutableLiveData<MapPartnerReader> mMapPartnerLiveData;

    @NonNull
    private InvalidationTracker.Observer mDbObserver;

    private GetPartnersCallbacks mGetPartnersCallbacks;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private boolean mLocationPermissionGranted = false;

    private boolean mAskFormMyPositionPermission = true;

    public MapsPresenter(@NonNull MapsContract.View view) {
        super(view);
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
            mGoogleApiClient = new GoogleApiClient.Builder(mView.getContext())
                    .addConnectionCallbacks(MapsPresenter.this)
                    .addOnConnectionFailedListener(MapsPresenter.this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGetPartnersCallbacks = new GetPartnersCallbacks(MapsPresenter.this);

        mMapPartnerLiveData = new MutableLiveData<>();

        mDbObserver = new InvalidationTracker.Observer(
                "partner", "partner_metadata", "category", "category_metadata", "partner_side_category"
        ) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                updatePartnerLiveData();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapPartnerLiveData.observe(
                mView.getLifecycleOwner(),
                new Observer<MapPartnerReader>() {
                    @Override
                    public void onChanged(@Nullable MapPartnerReader reader) {
                        mView.showPartners(reader);
                    }
                }
        );

        if (savedInstanceState == null) {
            mGetPartnersCallbacks.getParners();
        }
    }

    @Override
    protected void reattachLoaders() {
        mGetPartnersCallbacks.reattachLoader();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        DB.get(mView.getContext()).getInvalidationTracker().removeObserver(mDbObserver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mView.onMapReady(googleMap);
        loadPartners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_ASK_FOR_MY_LOCATION_PERMISSION, mAskFormMyPositionPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                onLocationPermissionResult(grantResults);
                mView.updateLocationUI();
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

    @Override
    public void loadPartners() {
        loadPartners(null);
    }

    @Override
    public void loadPartners(@Nullable final String search) {
        final LaGonetteDatabase database = DB.get(mView.getContext());
        database.getInvalidationTracker().addObserver(mDbObserver);
        updatePartnerLiveData();
    }

    private void updatePartnerLiveData() {
        mMapPartnerLiveData.postValue(
                MapPartnerReader.create(
                        DB.get(mView.getContext()).mainDao().getMapPartner()
                )
        );
    }

    @Override
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
    public boolean checkLocationPermission() {
        if (!mLocationPermissionGranted) {
            if (ContextCompat.checkSelfPermission(
                    mView.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else if (mAskFormMyPositionPermission) {
                mAskFormMyPositionPermission = false;
                mView.requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        PERMISSIONS_REQUEST_LOCATION
                );
            }
        }

        return mLocationPermissionGranted;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // TODO Use LiveData
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

    @Override
    public void errorGettingPartners() {
        mView.errorGettingPartners();
    }

}
