package org.lagonette.android.app.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.MapsContract;
import org.lagonette.android.app.presenter.base.BundleLoaderPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.GetPartnersCallbacks;
import org.lagonette.android.content.loader.callbacks.LoadPartnerCallbacks;
import org.lagonette.android.content.loader.callbacks.base.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnerReader;

public class MapsPresenter
        extends BundleLoaderPresenter<MapsContract.View>
        implements MapsContract.Presenter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        CursorLoaderCallbacks.Callbacks,
        GetPartnersCallbacks.Callbacks,
        LoadPartnerCallbacks.Callbacks {

    private static final String TAG = "MapsPresenter";

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    private org.lagonette.android.content.loader.callbacks.LoadPartnerCallbacks mLoadPartnerCallbacks;

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
        mLoadPartnerCallbacks = new LoadPartnerCallbacks(
                MapsPresenter.this,
                R.id.loader_query_map_partners
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        mLoadPartnerCallbacks.loadPartners();
    }

    @Override
    public void loadPartners(@NonNull String search) {
        mLoadPartnerCallbacks.loadPartners(
                PartnerCursorLoaderHelper.createArgs(search)
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

    public void startDirection(double latitude, double longitude) {
        Intent intent = new Intent(
                android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + latitude + "," + longitude)
        );
        PackageManager packageManager = mView.getContext().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            mView.getContext().startActivity(intent);
        } else {
            mView.errorNoDirectionAppFound();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

    @Override
    public void setPartnerReader(@Nullable PartnerReader reader) {
        mView.showPartners(reader);
    }

    @Override
    public CursorLoaderParams getPartnerLoaderParams(@Nullable Bundle args) {
        String search = PartnerCursorLoaderHelper.getSearch(args);
        return new CursorLoaderParams(
                LaGonetteContract.Map.CONTENT_URI,
                mView.getMapColumns()
        )
                .setSelection(!TextUtils.isEmpty(search)
                        ? LaGonetteContract.Map.PartnerMetadata.IS_VISIBLE + " = 1 AND " + LaGonetteContract.Map.Partner.NAME + " LIKE ? AND (" + LaGonetteContract.Map.CategoryMetadata.IS_VISIBLE + " > 0 OR " + LaGonetteContract.Map.SIDE_CATEGORY_METADATA_IS_VISIBLE_SUM + " > 0)"
                        : LaGonetteContract.Map.PartnerMetadata.IS_VISIBLE + " = 1 AND (" + LaGonetteContract.Map.CategoryMetadata.IS_VISIBLE + " > 0 OR " + LaGonetteContract.Map.SIDE_CATEGORY_METADATA_IS_VISIBLE_SUM + " > 0)"
                )
                .setSelectionArgs(!TextUtils.isEmpty(search)
                        ? new String[]{"%" + search + "%"}
                        : null
                )
                .setSortOrder(null);
    }
}
