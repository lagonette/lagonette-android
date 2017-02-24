package com.zxcv.gonette.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
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
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.ui.PartnerItem;
import com.zxcv.gonette.app.ui.PartnerRenderer;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.reader.PartnerReader;

import org.json.JSONException;

import java.io.IOException;

public class MapsFragment
        extends Fragment
        implements OnMapReadyCallback,
                   LoaderManager.LoaderCallbacks<Cursor>,
                   ClusterManager.OnClusterClickListener<PartnerItem> {

    public static final String TAG = "MapsFragment";

    public static final int ZOOM_LEVEL_CITY = 10;

    public static final int ANIMATION_LENGTH = 400;

    public static final int CLUSTER_CLICK_ZOOM_IN = 1;

    private GoogleMap mMap;

    private ClusterManager<PartnerItem> mClusterManager;

    public static MapsFragment newInstance() {
        return new MapsFragment();
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        setupFootprint();
        queryPartners();
    }

    private void setupMap() {
        // Add a marker in Lyon and move the camera
        LatLng lyon = new LatLng(45.764043, 4.835659);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon, ZOOM_LEVEL_CITY));

        mClusterManager = new ClusterManager<>(getContext(), mMap);
        mClusterManager.setRenderer(new PartnerRenderer(
                getContext(),
                LayoutInflater.from(getContext()),
                mMap,
                mClusterManager
        ));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
    }

    private void queryPartners() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.query_partners, null, MapsFragment.this);
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
            case R.id.query_partners:
                return new CursorLoader(
                        getContext(),
                        GonetteContract.Partner.CONTENT_URI,
                        new String[]{
                                GonetteContract.Partner.ID,
                                GonetteContract.Partner.NAME,
                                GonetteContract.Partner.DESCRIPTION,
                                GonetteContract.Partner.LATITUDE,
                                GonetteContract.Partner.LONGITUDE
                        },
                        null,
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
            case R.id.query_partners:
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
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.query_partners:
                // Do nothing.
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
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
