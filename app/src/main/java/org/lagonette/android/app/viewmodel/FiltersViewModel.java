package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.android.app.arch.MutableLiveEvent;
import org.lagonette.android.locator.Repo;
import org.lagonette.android.repo.Resource;
import org.lagonette.android.room.reader.FilterReader;

public class FiltersViewModel extends ViewModel {

    @NonNull
    private final MutableLiveEvent<String> mSearchLiveEvent;

    @NonNull
    private final LiveData<Resource<FilterReader>> mFiltersResourceLiveData;

    public FiltersViewModel() {
        mSearchLiveEvent = new MutableLiveEvent<>();
        mFiltersResourceLiveData = Repo.get().getFilters(mSearchLiveEvent);
    }

    @NonNull
    public MutableLiveEvent<String> getSearchLiveEvent() {
        return mSearchLiveEvent;
    }

    @NonNull
    public LiveData<Resource<FilterReader>> getFilters() {
        return mFiltersResourceLiveData;
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
