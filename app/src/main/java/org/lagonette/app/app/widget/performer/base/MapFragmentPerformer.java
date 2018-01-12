package org.lagonette.app.app.widget.performer.base;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.room.entity.statement.LocationItem;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.NOTIFY_MAP_MOVEMENT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Map.STOP_MOVING;

public abstract class MapFragmentPerformer implements Performer {

    public interface OnMapMovementChangedCommand {

        void notifyMapMovementChanged(@NonNull MainState.MapMovement mapMovement);
    }

    @NonNull
    protected MainLiveEventBusViewModel mEventBus;

    protected MapsFragment mFragment;

    @NonNull
    private final FragmentManager mFragmentManager;

    @Nullable
    private OnMapMovementChangedCommand mMapMovementChangedCommand;

    @NonNull
    protected MainState.MapMovement mMapMovement;

    @IdRes
    protected int mMapFragmentRes;

    public MapFragmentPerformer(@NonNull AppCompatActivity activity, @IdRes int mapFragmentRes) {
        mEventBus = ViewModelProviders.of(activity).get(MainLiveEventBusViewModel.class);
        mFragmentManager = activity.getSupportFragmentManager();
        mMapFragmentRes = mapFragmentRes;
        mMapMovement = MainState.MapMovement.IDLE;

        mEventBus.subscribe(
                NOTIFY_MAP_MOVEMENT,
                activity,
                this::notifyMapMovement
        );
    }

    public void loadFragment() {
        mFragment = MapsFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mFragment, MapsFragment.TAG)
                .commit();
    }

    public void restoreFragment() {
        mFragment = (MapsFragment) mFragmentManager.findFragmentByTag(MapsFragment.TAG);
    }

    public void openLocation(long locationId) {
        mEventBus.publish(MainLiveEventBusViewModel.Map.OPEN_LOCATION_ID, locationId);
    }

    public MainState.MapMovement getMapMovement() {
        return mMapMovement;
    }

    private void notifyMapMovement(@NonNull MainState.MapMovement mapMovement) {
        mMapMovement = mapMovement;
        if (mMapMovementChangedCommand != null) {
            mMapMovementChangedCommand.notifyMapMovementChanged(mMapMovement);
        }
    }

    public void moveToMyLocation() {
        mEventBus.publish(MOVE_TO_MY_LOCATION);
    }

    public boolean moveToFootprint() {
        mEventBus.publish(MOVE_TO_FOOTPRINT);
        return true;
    }

    public void moveToCluster(@NonNull Cluster<LocationItem> cluster) {
        mEventBus.publish(MOVE_TO_CLUSTER, cluster);
    }

    public void moveToLocation(@NonNull LocationItem item) {
        mEventBus.publish(MOVE_TO_LOCATION, item);
    }

    public void stopMoving() {
        mEventBus.publish(STOP_MOVING);
    }

    public void onMapMovementChanged(@Nullable OnMapMovementChangedCommand command) {
        mMapMovementChangedCommand = command;
    }
}
