package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.room.reader.FilterReader;

public class FiltersViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final LiveData<FilterReader> mFilters;

    @NonNull
    private final LiveData<HeadquarterShortcut> mHeadquarterShortcut;

    public FiltersViewModel() {
        mSearch = new MutableLiveData<>();
        mFilters = Repo.get().getFilters(mSearch);
        mHeadquarterShortcut = Repo.get().getHeadquarterShortcut();
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<FilterReader> getFilters() {
        return mFilters;
    }

    @NonNull
    public LiveData<HeadquarterShortcut> getHeadquarterShortcut() {
        return mHeadquarterShortcut;
    }

    public void changeLocationVisibility(long locationId, boolean isVisible) {
        Repo
                .get()
                .setLocationVisibility(locationId, isVisible);
    }

    public void changeCategoryVisibility(@NonNull CategoryKey categoryKey, boolean isVisible) {
        Repo
                .get()
                .setCategoryVisibility(categoryKey, isVisible);
    }

    public void changeCategoryCollapsed(@NonNull CategoryKey categoryKey, boolean isCollapsed) {
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
