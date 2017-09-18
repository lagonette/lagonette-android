package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.app.arch.EventShipper;
import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.reader.FilterReader;

public class FiltersViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final LiveData<Resource<FilterReader>> mFiltersResource;

    public FiltersViewModel() {
        mSearch = new MutableLiveData<>();
        mFiltersResource = Repo.get().getFilters(mSearch);
    }

    @NonNull
    public EventShipper.Sender<String> getSearch() {
        return search -> mSearch.postValue(search);
    }

    @NonNull
    public LiveData<Resource<FilterReader>> getFilters() {
        return mFiltersResource;
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

    public void showAllPartners() {
        Repo
                .get()
                .showAllPartners();
    }

    public void showAllExchangeOffices() {
        Repo
                .get()
                .showAllExchangeOffice();
    }
}
