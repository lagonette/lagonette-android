package org.lagonette.android.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.lagonette.android.app.arch.LiveEvent;
import org.lagonette.android.app.arch.MutableLiveEvent;

public class SharedMapsActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Boolean> mWorkInProgressLiveData;

    @NonNull
    private final MutableLiveEvent<Void> mMapIsReadyLiveEvent;

    @NonNull
    private final MutableLiveData<Boolean> mEnableMyPositionFABLiveData;

    @NonNull
    private final MutableLiveEvent<ShowPartnerEvent> mShowPartnerRequestLiveEvent;

    @NonNull
    private final ShowPartnerEvent mShowPartnerEvent;

    public SharedMapsActivityViewModel(Application application) {
        super(application);
        mWorkInProgressLiveData = new MutableLiveData<>();
        mMapIsReadyLiveEvent = new MutableLiveEvent<>();
        mEnableMyPositionFABLiveData = new MutableLiveData<>();
        mShowPartnerRequestLiveEvent = new MutableLiveEvent<>();

        mShowPartnerEvent = new ShowPartnerEvent();
        mSearch = new MutableLiveData<>();
        mSearch.setValue(null);
    }

    @NonNull
    public LiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Boolean> getWorkInProgress() {
        return mWorkInProgressLiveData;
    }

    @NonNull
    public LiveEvent<Void> getMapIsReady() {
        return mMapIsReadyLiveEvent;
    }

    @NonNull
    public LiveData<Boolean> getEnableMyPositionFAB() {
        return mEnableMyPositionFABLiveData;
    }

    @NonNull
    public LiveEvent<ShowPartnerEvent> getShowPartnerRequest() {
        return mShowPartnerRequestLiveEvent;
    }

    public void search(@NonNull String search) {
        mSearch.setValue(search);
    }

    public void setWorkInProgress(boolean workInProgress) {
        mWorkInProgressLiveData.postValue(workInProgress);
    }

    public void callMapIsReady() {
        mMapIsReadyLiveEvent.call();
    }

    public void setEnableMyPositionFAB(boolean enable) {
        mEnableMyPositionFABLiveData.postValue(enable);
    }

    public void showPartner(long partnerId, boolean zoom) {
        mShowPartnerEvent.partnerId = partnerId;
        mShowPartnerEvent.zoom = zoom;
        mShowPartnerRequestLiveEvent.send(mShowPartnerEvent);
    }

    public void showFullMap() {
        mShowPartnerRequestLiveEvent.call();
    }

    public static class ShowPartnerEvent {

        public long partnerId;

        public boolean zoom;

    }

}
