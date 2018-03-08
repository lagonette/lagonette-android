package org.lagonette.app.app.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.MapLocationViewModel;
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.app.widget.performer.impl.MapMovementPerformer;
import org.lagonette.app.app.widget.performer.impl.MapPerformer;
import org.lagonette.app.app.widget.performer.impl.SnackbarPerformer;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.LocationItem;

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
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.UPDATE_MAP_LOCATION_UI;

public class MapsFragment
        extends Fragment {

    public static final String TAG = "MapsFragment";

    private MapViewModel mViewModel;

    private DataViewModel mStateViewModel;

    private MainLiveEventBusViewModel mEventBus;

    private SnackbarPerformer mSnackbarPerformer;

    private MapLocationViewModel mMapLocationViewModel;

    private MapMovementPerformer mMapMovementPerformer;

    private MapPerformer mMapPerformer;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapPerformer = new MapPerformer(getContext());

        mMapMovementPerformer = new MapMovementPerformer();

        mMapLocationViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapLocationViewModel.class);

        mViewModel = ViewModelProviders
                .of(MapsFragment.this)
                .get(MapViewModel.class);

        mStateViewModel = ViewModelProviders
                .of(getActivity())
                .get(DataViewModel.class);

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

        mapFragment.getMapAsync(googleMap -> {
            mMapPerformer.onMapReady(googleMap);
            mMapMovementPerformer.onMapReady(googleMap);
            mMapLocationViewModel.getCameraPosition().observe(
                    MapsFragment.this,
                    mMapMovementPerformer::setCameraPosition
            );

            mViewModel.getMapPartners().observe(
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
                mMapMovementPerformer::moveToLocation
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

        mSnackbarPerformer = new SnackbarPerformer(getActivity());
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

    public void setMapPadding(int left, int top, int right, int bottom) {
        mMapPerformer.setMapPadding(left, top, right, bottom);
    }

}
