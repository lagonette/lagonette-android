package org.lagonette.android.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.viewmodel.base.DatabaseObserverViewModel;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.util.SearchUtil;

public class FiltersViewModel extends DatabaseObserverViewModel {

    private MutableLiveData<FilterReader> mFiltersLiveData;

    @Nullable
    private String mSearch;

    public FiltersViewModel(@NonNull Application application) {
        super(application);

        mFiltersLiveData = new MutableLiveData<>();

        updateFilters();
    }

    @Override
    protected void onDatabaseInvalidated() {
        updateFilters();
    }

    public LiveData<FilterReader> getFilters() {
        return mFiltersLiveData;
    }


    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        mDatabase.partnerDao().updatePartnerVisibility(partnerId, isVisible);
    }

    public void setCategoryVisibility(long categoryId, boolean isVisible) {
        mDatabase.categoryDao().updateCategoryVisibility(categoryId, isVisible);
    }

    public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
        mDatabase.categoryDao().updateCategoryCollapsed(categoryId, isCollapsed);
    }

    private void updateFilters() {
        // TODO use AsyncTask and ensure thread is start only one times
        new Thread(
                () -> mFiltersLiveData.postValue(
                        FilterReader.create(
                                mDatabase.mainDao().getFilters(SearchUtil.formatSearch(mSearch))
                        )
                )
        ).start();
    }

    public void filterPartners(@Nullable String search) {
        mSearch = search;
        updateFilters();
    }
}
