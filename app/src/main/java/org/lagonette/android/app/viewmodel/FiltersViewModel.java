package org.lagonette.android.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.util.DB;
import org.lagonette.android.util.SearchUtil;

import java.util.Set;

public class FiltersViewModel extends ViewModel {

    @NonNull
    private final InvalidationTracker.Observer mDbObserver;

    @NonNull
    private final LaGonetteDatabase mDatabase;

    private MutableLiveData<FilterReader> mFiltersLiveData;

    @Nullable
    private String mSearch;

    public FiltersViewModel(@NonNull Context context) {

        mFiltersLiveData = new MutableLiveData<>();

        mDbObserver = new InvalidationTracker.Observer( // TODO
                "partner", "partner_metadata", "category", "category_metadata", "partner_side_category"
        ) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                onDatabaseInvalidated();
            }
        };

        mDatabase = DB.get(context);

        mDatabase.getInvalidationTracker().addObserver(mDbObserver);

        updateFilters();
    }

    @Override
    protected void onCleared() {
        mDatabase.getInvalidationTracker().removeObserver(mDbObserver);
    }

    private void onDatabaseInvalidated() {
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

    public static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final Context mContext;

        public Factory(@NonNull Context context) {
            mContext = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FiltersViewModel(mContext);
        }
    }
}
