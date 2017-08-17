package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.android.app.locator.Repo;
import org.lagonette.android.room.reader.PartnerDetailReader;

public class PartnerDetailViewModel extends ViewModel {

    @NonNull
    private LiveData<PartnerDetailReader> mPartnerDetailReaderLiveData;

    @NonNull
    private MutableLiveData<Long> mPartnerIdLiveData;

    public PartnerDetailViewModel() {
        mPartnerIdLiveData = new MutableLiveData<>();
        mPartnerDetailReaderLiveData = Repo.get().getPartnerDetail(mPartnerIdLiveData);
    }

    public void setPartnerId(long partnerId) {
        mPartnerIdLiveData.postValue(partnerId);
    }

    @NonNull
    public LiveData<PartnerDetailReader> getPartnerDetailReaderLiveData() {
        return mPartnerDetailReaderLiveData;
    }
}
