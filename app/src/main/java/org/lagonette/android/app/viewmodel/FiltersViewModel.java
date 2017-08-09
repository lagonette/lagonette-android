package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.viewmodel.base.DatabaseObserverViewModel;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.util.SearchUtil;

public class FiltersViewModel extends DatabaseObserverViewModel {

    private MutableLiveData<FilterReader> mFiltersLiveData;

    @Nullable
    private String mSearch;

    public FiltersViewModel(@NonNull Context context) {
        super(context);

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
        mFiltersLiveData.postValue(
                FilterReader.create(
                        mDatabase.mainDao().getFilters(SearchUtil.formatSearch(mSearch))
                )
        );
    }

    public void filterPartners(@Nullable String search) {
        mSearch = search;
        updateFilters();
    }
}
