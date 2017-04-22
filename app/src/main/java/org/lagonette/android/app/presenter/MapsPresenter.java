package org.lagonette.android.app.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.crash.FirebaseCrash;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.MapsContract;
import org.lagonette.android.app.presenter.base.BasePresenter;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.loader.GetPartnersLoader;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.BundleLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.CursorLoaderCallbacks;
import org.lagonette.android.content.reader.PartnerReader;

public class MapsPresenter
        extends BasePresenter
        implements MapsContract.Presenter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        CursorLoaderCallbacks.Callbacks,
        BundleLoaderCallbacks.Callbacks {

    private static final String TAG = "MapsPresenter";

    private static final String STATE_ASK_FOR_MY_LOCATION_PERMISSION = "state:ask_for_my_location_permission";

    public static final int PERMISSIONS_REQUEST_LOCATION = 666;

    @NonNull
    private final MapsContract.View mView;

    private CursorLoaderCallbacks mCursorLoaderCallbacks;

    private BundleLoaderCallbacks mBundleLoaderCallbacks;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private boolean mLocationPermissionGranted = false;

    private boolean mAskFormMyPositionPermission = true;

    public MapsPresenter(@NonNull MapsContract.View view) {
        mView = view;
    }

    @NonNull
    @Override
    public LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
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

        mCursorLoaderCallbacks = new CursorLoaderCallbacks(MapsPresenter.this);
        mBundleLoaderCallbacks = new BundleLoaderCallbacks(MapsPresenter.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            mBundleLoaderCallbacks.initLoader(
                    R.id.loader_get_partners,
                    null
            );
        }
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
    public Loader<Cursor> onCreateCursorLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_map_partners:
                String search = PartnerCursorLoaderHelper.getSearch(args);
                return new CursorLoader(
                        mView.getContext(),
                        GonetteContract.Partner.METADATA_CONTENT_URI,
                        new String[]{
                                GonetteContract.Partner.ID,
                                GonetteContract.Partner.NAME,
                                GonetteContract.Partner.DESCRIPTION,
                                GonetteContract.Partner.LATITUDE,
                                GonetteContract.Partner.LONGITUDE
                        },
                        GonetteContract.PartnerMetadata.IS_VISIBLE + " = 1 AND " + GonetteContract.Partner.NAME + " LIKE ?",

                        new String[]{
                                "%" + search + "%"
                        },
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public boolean onCursorLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                onQueryPartnerLoadFinished(cursor);
                return true;
            default:
                return false;
        }
    }

    private void onQueryPartnerLoadFinished(Cursor cursor) {
        mView.showPartners(
                cursor != null
                        ? new PartnerReader(cursor)
                        : null
        );
    }

    @Override
    public boolean onCursorLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_map_partners:
                // Do nothing.
                return true;
            default:
                return false;
        }
    }

    @Override
    public void loadPartners() {
        mCursorLoaderCallbacks.initLoader(
                R.id.loader_query_map_partners,
                null
        );
    }

    @Override
    public void loadPartners(@NonNull String search) {
        mCursorLoaderCallbacks.restartLoader(
                R.id.loader_query_map_partners,
                PartnerCursorLoaderHelper.getArgs(search)
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


    @CallSuper
    @Override
    public Loader<Bundle> onCreateBundleLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_get_partners:
                return new GetPartnersLoader(mView.getContext(), args);
            default:
                return null;
        }
    }

    @CallSuper
    @Override
    public boolean onBundleLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_get_partners:
                onGetPartnersLoaderFinished(data);
                return true;
            default:
                return false;
        }
    }

    private void onGetPartnersLoaderFinished(@NonNull Bundle data) {
        int status = data.getInt(GetPartnersLoader.ARG_STATUS);
        switch (status) {
            case GetPartnersLoader.STATUS_OK:
                break;
            case GetPartnersLoader.STATUS_ERROR:
                mView.errorGettingPartners();
                break;
        }
    }

    @CallSuper
    @Override
    public boolean onBundleLoaderReset(@NonNull Loader<Bundle> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_get_partners:
                // Do nothing
                return true;
            default:
                return false;
        }
    }

    @Override
    public int[] getBundleLoaderIds() {
        return new int[]{
                R.id.loader_get_partners
        };
    }
}
