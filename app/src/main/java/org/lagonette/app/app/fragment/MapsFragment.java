package org.lagonette.app.app.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.MapLocationViewModel;
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.widget.performer.impl.MapMovementPerformer;
import org.lagonette.app.app.widget.performer.impl.MapPerformer;
import org.lagonette.app.room.entity.statement.LocationItem;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.NOTIFY_MAP_MOVEMENT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.SHOW_FULL_MAP;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.OPEN_LOCATION_ID;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.STOP_MOVING;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.UPDATE_MAP_LOCATION_UI;

public class MapsFragment
        extends BaseFragment {

    public static final String TAG = "MapsFragment";

    // --- View Models --- //

    private MapViewModel mMapViewModel;

    private DataViewModel mDataViewModel;

    private MainLiveEventBusViewModel mEventBus;

    private MapLocationViewModel mMapLocationViewModel;

    // --- Performers --- //

    private MapMovementPerformer mMapMovementPerformer;

    private MapPerformer mMapPerformer;

    // --- Fragments --- //

    private SupportMapFragment mMapFragment;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    //TODO Use firebase to find broken data

    @Override
    protected void construct() {
        mMapPerformer = new MapPerformer(getContext());

        mMapMovementPerformer = new MapMovementPerformer();

        mMapLocationViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapLocationViewModel.class);

        mMapViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapViewModel.class);

        mDataViewModel = ViewModelProviders
                .of(getActivity())
                .get(DataViewModel.class);

        mEventBus = ViewModelProviders
                .of(getActivity())
                .get(MainLiveEventBusViewModel.class);

        mDataViewModel
                .getSearch()
                .observe(
                        MapsFragment.this,
                        mMapViewModel.getSearch()::setValue
                );
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_maps;
    }

    @Override
    protected void inject(@NonNull View view) {
        // Obtain the SupportMapFragment and create notification when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
    }

    @Override
    protected void construct(@NonNull FragmentActivity activity) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected void onConstructed() {
        mMapFragment.getMapAsync(googleMap -> {
            mMapPerformer.onMapReady(googleMap);
            mMapMovementPerformer.onMapReady(googleMap);
            mMapLocationViewModel.getCameraPosition().observe(
                    MapsFragment.this,
                    mMapMovementPerformer::setCameraPosition
            );

            mMapViewModel.getMapPartners().observe(
                    MapsFragment.this,
                    mMapPerformer::showPartners
            );
        });

        mMapPerformer.showFullMap       = ()       -> mEventBus.publish(SHOW_FULL_MAP                );
        mMapPerformer.notifyMapMovement = movement -> mEventBus.publish(NOTIFY_MAP_MOVEMENT, movement);
        mMapPerformer.moveToCluster     = cluster  -> mEventBus.publish(MOVE_TO_CLUSTER,     cluster );
        mMapPerformer.openLocationItem  = item     -> mEventBus.publish(OPEN_LOCATION_ITEM,  item    );

        mEventBus.subscribe(
                MOVE_TO_MY_LOCATION,
                MapsFragment.this,
                mMapMovementPerformer::moveToMyLocation
        );

        mEventBus.subscribe(
                MOVE_TO_FOOTPRINT,
                MapsFragment.this,
                mMapMovementPerformer::moveToFootprint
        );

        mEventBus.subscribe(
                MOVE_TO_LOCATION,
                MapsFragment.this,
                locationItem -> {
                    mMapPerformer.selectLocation(locationItem);
                    mMapMovementPerformer.moveToLocation(locationItem);
                }
        );

        mEventBus.subscribe(
                MOVE_TO_CLUSTER,
                MapsFragment.this,
                mMapMovementPerformer::moveToCluster
        );

        mEventBus.subscribe(
                STOP_MOVING,
                MapsFragment.this,
                mMapMovementPerformer::stopMoving
        );

        mEventBus.subscribe(
                OPEN_LOCATION_ID,
                MapsFragment.this,
                this::openLocation
        );

        mEventBus.subscribe(
                UPDATE_MAP_LOCATION_UI,
                MapsFragment.this,
                mMapPerformer::updateLocationUI
        );

    }

    @Override
    public void onPause() {
        CameraPosition cameraPosition = mMapMovementPerformer.getCameraPosition();
        if (cameraPosition != null) {
            mMapLocationViewModel.save(cameraPosition);
        }
        super.onPause();
    }

    private void openLocation(long locationId) {
        LocationItem locationItem = mMapPerformer.retrieveLocationItem(locationId);
        mEventBus.publish(OPEN_LOCATION_ITEM, locationItem);
    }

    /**
     * Do not pass through LiveData for optimisation purpose.
     */
    public void setMapPadding(int left, int top, int right, int bottom) {
        mMapPerformer.setMapPadding(left, top, right, bottom);
    }

}
