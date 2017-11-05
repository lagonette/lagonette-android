package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.embedded.CategoryKey;
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
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Resource<FilterReader>> getFilters() {
        return mFiltersResource;
    }

    public void setLocationVisibility(long locationId, boolean isVisible) {
        Repo
                .get()
                .setLocationVisibility(locationId, isVisible);
    }

    public void setCategoryVisibility(@NonNull CategoryKey categoryKey, boolean isVisible) {
        Repo
                .get()
                .setCategoryVisibility(categoryKey, isVisible);
    }

    public void setCategoryCollapsed(@NonNull CategoryKey categoryKey, boolean isCollapsed) {
        Repo
                .get()
                .setCategoryCollapsed(categoryKey, isCollapsed);
    }

    public void showAllLocations() {
        Repo
                .get()
                .showAllLocations();
    }

    public void showAllExchangeOffices() {
        Repo
                .get()
                .showAllExchangeOffice();
    }
}
