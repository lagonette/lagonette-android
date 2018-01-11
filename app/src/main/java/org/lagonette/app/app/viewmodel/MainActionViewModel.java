package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.room.entity.statement.LocationItem;

import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.BACK;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_AND_OPEN_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_FOOTPRINT;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.MOVE_TO_MY_LOCATION;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.OPEN_FILTERS;
import static org.lagonette.app.app.widget.coordinator.state.MainAction.ActionType.SHOW_FULL_MAP;

public class MainActionViewModel extends ViewModel {

    private static final String TAG = "MainCoordinatorActionLi";

    //TODO Make it immutable
    @NonNull
    private final MainAction mAction;

    @NonNull
    private final MutableLiveData<MainAction> mActionLiveData;

    public MainActionViewModel() {
        mAction = new MainAction();
        mActionLiveData = new MutableLiveData<>();
    }

    public void exec() {
        mActionLiveData.setValue(mAction);
    }

    public void openFilters() {
        Log.d(TAG, "Action -> OPEN FILTERS");
        mAction.done();
        mAction.type = OPEN_FILTERS;
        mActionLiveData.setValue(mAction);
    }

    public void showFullMap() {
        Log.d(TAG, "Action -> SHOW FULL MAP");
        mAction.done();
        mAction.type = SHOW_FULL_MAP;
        mActionLiveData.setValue(mAction);
    }

    public void moveToMyLocation() {
        Log.d(TAG, "Action -> MOVE TO MY LOCATION");
        mAction.done();
        mAction.type = MOVE_TO_MY_LOCATION;
        mAction.shouldMove = true;
        mActionLiveData.setValue(mAction);
    }

    public void moveToFootprint() {
        Log.d(TAG, "Action -> MOVE TO FOOTPRINT");
        mAction.done();
        mAction.type = MOVE_TO_FOOTPRINT;
        mAction.shouldMove = true;
        mActionLiveData.setValue(mAction);
    }

    public void moveToCluster(@NonNull Cluster<LocationItem> cluster) {
        Log.d(TAG, "Action -> MOVE TO CLUSTER");
        mAction.done();
        mAction.type = MOVE_TO_CLUSTER;
        mAction.cluster = cluster;
        mAction.shouldMove = true;
        mActionLiveData.setValue(mAction);
    }

    public void moveToAndOpenLocation(@NonNull LocationItem item) {
        Log.d(TAG, "Action -> MOVE TO AND OPEN LOCATION ITEM");
        mAction.done();
        mAction.type = MOVE_TO_AND_OPEN_LOCATION;
        mAction.item = item;
        mAction.shouldMove = true;
        mActionLiveData.setValue(mAction);
    }

    //TODO What if locationId <= NO_ID?
    public void moveToAndOpenLocation(long locationId) {
        Log.d(TAG, "Action -> MOVE TO AND OPEN LOCATION ID");
        mAction.done();
        mAction.type = MOVE_TO_AND_OPEN_LOCATION;
        mAction.locationId = locationId;
        mAction.shouldMove = true;
        mActionLiveData.setValue(mAction);
    }

    public void back() {
        Log.d(TAG, "Action -> BACK");
        mAction.done();
        mAction.type = BACK;
        mActionLiveData.setValue(mAction);
    }

    public void markDone() {
        Log.d(TAG, "Action -> DONE");
        mAction.done();
        mActionLiveData.setValue(mAction);
    }

    public LiveData<MainAction> getLiveData() {
        return mActionLiveData;
    }
}
