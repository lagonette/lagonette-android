package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.PartnerDetail;

public class PartnerDetailViewModel extends ViewModel {

    @NonNull
    private LiveData<Resource<PartnerDetail>> mPartnerDetailResource;

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
    public LiveData<Resource<PartnerDetail>> getPartnerDetail() {
        return mPartnerDetailResource;
    }
}
