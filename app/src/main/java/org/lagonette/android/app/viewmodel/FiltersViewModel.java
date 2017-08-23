package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.FilterReader;

public class FiltersViewModel extends ViewModel {

    @NonNull
    private MutableLiveData<String> mSearchLiveData;

    @NonNull
    private LiveData<Resource<FilterReader>> mFiltersResourceLiveData;

    public FiltersViewModel() {
        mSearchLiveData = new MutableLiveData<>();
        mFiltersResourceLiveData = Repo.get().getFilters(mSearchLiveData);
        filterPartners(null);
    }

    @NonNull
    public LiveData<Resource<FilterReader>> getFilters() {
        return mFiltersResourceLiveData;
    }

    public void filterPartners(@Nullable String search) {
        mSearchLiveData.postValue(search);
    }

    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        Repo
                .get()
                .setPartnerVisibility(partnerId, isVisible);
    }

    public void setCategoryVisibility(long categoryId, boolean isVisible) {
        Repo
                .get()
                .setCategoryVisibility(categoryId, isVisible);
    }

    public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
        Repo
                .get()
                .setCategoryCollapsed(categoryId, isCollapsed);
    }
}
