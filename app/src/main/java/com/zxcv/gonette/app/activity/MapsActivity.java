package com.zxcv.gonette.app.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

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
import com.zxcv.gonette.app.ui.PartnerItem;
import com.zxcv.gonette.app.ui.PartnerRenderer;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.reader.PartnerReader;

import org.json.JSONException;

import java.io.IOException;

public class MapsActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
                   LoaderManager.LoaderCallbacks<Cursor>,
                   ClusterManager.OnClusterClickListener<PartnerItem> {

    private static final String TAG = "MapsActivity";

    public static final int ZOOM_LEVEL_CITY = 10;

    public static final int ANIMATION_LENGTH = 400;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    private GoogleMap mMap;

    private ClusterManager<PartnerItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (BuildConfig.DEBUG) {
            // GonetteDatabaseOpenHelper.parseData(MapsActivity.this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lyon = new LatLng(45.764043, 4.835659);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon, ZOOM_LEVEL_CITY));

        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new PartnerRenderer(
                MapsActivity.this,
                getLayoutInflater(),
                mMap,
                mClusterManager
        ));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, MapsActivity.this);

        setupFootprint();
    }

    private void setupFootprint() {
        try {
            GeoJsonLayer footprintLayer = new GeoJsonLayer(mMap, R.raw.footprint, this);
            for (GeoJsonFeature feature : footprintLayer.getFeatures()) {
                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
                lineStringStyle.setColor(ContextCompat.getColor(MapsActivity.this, R.color.footprint));
                feature.setLineStringStyle(lineStringStyle);
            }
            footprintLayer.addLayerToMap();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "onMapReady: " + e.getMessage(), e);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                MapsActivity.this,
                GonetteContract.Partner.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            PartnerReader partnerReader = new PartnerReader(cursor);
            while (partnerReader.moveToNext()) {
                PartnerItem item = new PartnerItem(
                        partnerReader.getLatitude(),
                        partnerReader.getLongitude(),
                        partnerReader.getName(),
                        partnerReader.getDescription()
                );
                mClusterManager.addItem(item);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onClusterClick(Cluster<PartnerItem> cluster) {
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        cluster.getPosition(),
                        mMap.getCameraPosition().zoom + CLUSTER_CLICK_ZOOM_IN
                ),
                ANIMATION_LENGTH,
                null
        );
        return true;
    }
}
