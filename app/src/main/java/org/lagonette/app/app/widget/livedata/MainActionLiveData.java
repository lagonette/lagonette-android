package org.lagonette.app.app.widget.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.room.entity.statement.PartnerItem;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ACTION_SHOW_FULL_MAP;

public class MainActionLiveData extends LiveData<MainAction> {

    private static final String TAG = "MainCoordinatorActionLi";

    @NonNull
    private final MainAction mAction;

    public MainActionLiveData(@NonNull MainAction action) {
        mAction = action;
    }

    @NonNull
    @Override
    public MainAction getValue() {
        MainAction action = super.getValue();
        if (action == null) {
            action = mAction;
        }
        return action;
    }

    public void openFilters() {
        Log.d(TAG, "Action -> OPEN FILTERS");
        mAction.type = ACTION_OPEN_FILTERS;
        setValue(mAction);
    }

    public void showFullMap() {
        Log.d(TAG, "Action -> SHOW FULL MAP");
        mAction.type = ACTION_SHOW_FULL_MAP;
        setValue(mAction);
    }

    public void moveToMyLocation() {
        Log.d(TAG, "Action -> MOVE TO MY LOCATION");
        mAction.type = ACTION_MOVE_TO_MY_LOCATION;
        mAction.shouldMove = true;
        setValue(mAction);
    }

    public void moveToFootprint() {
        Log.d(TAG, "Action -> MOVE TO FOOTPRINT");
        mAction.type = ACTION_MOVE_TO_FOOTPRINT;
        mAction.shouldMove = true;
        setValue(mAction);
    }

    public void moveToCluster(@NonNull Cluster<PartnerItem> cluster) {
        Log.d(TAG, "Action -> MOVE TO CLUSTER");
        mAction.type = ACTION_MOVE_TO_CLUSTER;
        mAction.cluster = cluster;
        mAction.shouldMove = true;
        setValue(mAction);
    }

    public void moveToLocation(@NonNull PartnerItem item) {
        Log.d(TAG, "Action -> MOVE TO PARTNER ITEM");
        mAction.type = ACTION_MOVE_TO_LOCATION;
        mAction.item = item;
        mAction.shouldMove = true;
        setValue(mAction);
    }

    public void markDone() {
        Log.d(TAG, "Action -> DONE");
        mAction.done();
        setValue(mAction);
    }
}
