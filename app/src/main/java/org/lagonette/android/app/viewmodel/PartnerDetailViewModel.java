package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.PartnerDetailReader;

public class PartnerDetailViewModel extends ViewModel {

    @NonNull
    private LiveData<Resource<PartnerDetailReader>> mPartnerDetailResource;

    @NonNull
    private MutableLiveData<Long> mPartnerId;

    public PartnerDetailViewModel() {
        mPartnerId = new MutableLiveData<>();
        mPartnerDetailResource = Repo.get().getPartnerDetail(mPartnerId);
    }

    public void setPartnerId(long partnerId) {
        mPartnerId.postValue(partnerId);
    }

    @NonNull
    public LiveData<Resource<PartnerDetailReader>> getPartnerDetail() {
        return mPartnerDetailResource;
    }
}
