package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.arch.LiveEvent;
import org.lagonette.app.app.arch.MutableLiveEvent;
import org.lagonette.app.util.IntegerUtil;

public class SharedMapsActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Boolean> mWorkInProgress;

    @NonNull
    private final MutableLiveEvent<Void> mMapIsReady;

    @NonNull
    private final MutableLiveData<Boolean> mEnableMyPositionFAB;

    @NonNull
    private final MutableLiveEvent<ShowLocationEvent> mShowLocationRequest;

    @NonNull
    private final ShowLocationEvent mShowLocationEvent;

//    @NonNull
//    private final MutableLiveEvent<Integer> mMapMovement;

    @NonNull
    private final MutableLiveData<Integer> mMapTopPadding;

    @NonNull
    private final MutableLiveData<Integer> mMapBottomPadding;

    @NonNull
    private final MediatorLiveData<int[]> mMapPadding;

    public SharedMapsActivityViewModel(Application application) {
        super(application);
        mWorkInProgress = new MutableLiveData<>();
        mMapIsReady = new MutableLiveEvent<>();
        mEnableMyPositionFAB = new MutableLiveData<>();
        mShowLocationRequest = new MutableLiveEvent<>();
//        mMapMovement = new MutableLiveEvent<>();
        mSearch = new MutableLiveData<>();
        mMapTopPadding = new MutableLiveData<>();
        mMapBottomPadding = new MutableLiveData<>();
        mMapPadding = new MediatorLiveData<>();

        mShowLocationEvent = new ShowLocationEvent();

        mSearch.postValue(""); //TODO Not sure if usefull

        mMapPadding.setValue(new int[] {0,0,0,0});
        mMapPadding.addSource(
                mMapTopPadding,
                topPadding -> {
                    int[] paddings = mMapPadding.getValue();
                    paddings[1] = IntegerUtil.intValue(topPadding);
                    mMapPadding.setValue(paddings);
                }
        );
        mMapPadding.addSource(
                mMapBottomPadding,
                bottomPadding -> {
                    int[] paddings = mMapPadding.getValue();
                    paddings[3] = IntegerUtil.intValue(bottomPadding);
                    mMapPadding.setValue(paddings);
                }
        );
    }

    @NonNull
    public LiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Boolean> getWorkInProgress() {
        return mWorkInProgress;
    }

    @NonNull
    public LiveEvent<Void> getMapIsReady() {
        return mMapIsReady;
    }

    @NonNull
    public LiveData<Boolean> getEnableMyPositionFAB() {
        return mEnableMyPositionFAB;
    }

    @NonNull
    public LiveEvent<ShowLocationEvent> getShowLocationRequest() {
        return mShowLocationRequest;
    }

//    @NonNull
//    public EventShipper.Sender<Integer> getMapMovementSender() {
//        return mMapMovement;
//    }
//
//    @NonNull
//    public LiveEvent<Integer> getMapMovement() {
//        return mMapMovement;
//    }

    @NonNull
    public MutableLiveData<Integer> getMapTopPadding() {
        return mMapTopPadding;
    }

    @NonNull
    public MutableLiveData<Integer> getMapBottomPadding() {
        return mMapBottomPadding;
    }

    @NonNull
    public LiveData<int[]> getMapPadding() {
        return mMapPadding;
    }

    public void search(@NonNull String search) {
        mSearch.setValue(search);
    }

    public void setWorkInProgress(boolean workInProgress) {
        mWorkInProgress.postValue(workInProgress);
    }

    public void callMapIsReady() {
        mMapIsReady.call();
    }

    public void setEnableMyPositionFAB(boolean enable) {
        mEnableMyPositionFAB.postValue(enable);
    }

//    public void showLocation(long locationId, boolean zoom) {
//        mShowLocationEvent.locationId = locationId;
//        mShowLocationEvent.zoom = zoom;
//        mShowLocationRequest.send(mShowLocationEvent);
//    }

//    public void showFullMap() {
//        mShowLocationRequest.call();
//    }

    //TODO Make custom LiveData
    public static class ShowLocationEvent {

        public long locationId;

        public boolean zoom;

    }

}
