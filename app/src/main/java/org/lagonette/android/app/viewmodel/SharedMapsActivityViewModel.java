package org.lagonette.android.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class SharedMapsActivityViewModel extends AndroidViewModel {

    @NonNull
    private MutableLiveData<Boolean> mWorkInProgressLiveData;

    @NonNull
    private MutableLiveData<Void> mMapIsReadyLiveData;

    @NonNull
    private MutableLiveData<Boolean> mEnableMyPositionFABLiveData;

    @NonNull
    private MutableLiveData<ShowPartnerRequest> mShowPartnerRequestLiveData;

    @NonNull
    private ShowPartnerRequest mShowPartnerRequest;

    public SharedMapsActivityViewModel(Application application) {
        super(application);
        mWorkInProgressLiveData = new MutableLiveData<>();
        mMapIsReadyLiveData = new MutableLiveData<>();
        mEnableMyPositionFABLiveData = new MutableLiveData<>();
        mShowPartnerRequestLiveData = new MutableLiveData<>();

        mShowPartnerRequest = new ShowPartnerRequest();
    }

    @NonNull
    public LiveData<Boolean> getWorkInProgressLiveData() {
        return mWorkInProgressLiveData;
    }

    @NonNull
    public LiveData<Void> getMapIsReadyLiveData() {
        return mMapIsReadyLiveData;
    }

    @NonNull
    public LiveData<Boolean> getEnableMyPositionFABLiveData() {
        return mEnableMyPositionFABLiveData;
    }

    @NonNull
    public LiveData<ShowPartnerRequest> getShowPartnerRequestLiveData() {
        return mShowPartnerRequestLiveData;
    }

    public void setWorkInProgress(boolean workInProgress) {
        mWorkInProgressLiveData.postValue(workInProgress);
    }

    public void notifyMapIsReady() {
        mMapIsReadyLiveData.postValue(null);
    }

    public void setEnableMyPositionFAB(boolean enable) {
        mEnableMyPositionFABLiveData.postValue(enable);
    }

    public void showPartner(long partnerId, boolean zoom) {
        mShowPartnerRequest.partnerId = partnerId;
        mShowPartnerRequest.zoom = zoom;
        mShowPartnerRequestLiveData.postValue(mShowPartnerRequest);
    }

    public void showFullMap() {
        mShowPartnerRequestLiveData.postValue(null);
    }

    public static class ShowPartnerRequest {

        public long partnerId;

        public boolean zoom;

    }

}
