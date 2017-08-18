package org.lagonette.android.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import org.lagonette.android.app.DbLiveData;
import org.lagonette.android.locator.DB;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.room.reader.MapPartnerReader;
import org.lagonette.android.room.reader.PartnerDetailReader;
import org.lagonette.android.room.sql.Tables;
import org.lagonette.android.room.statement.Statement;
import org.lagonette.android.util.SearchUtil;
import org.lagonette.android.worker.DataRefreshWorker;

import java.util.concurrent.Executor;

public class MainRepo {

    @NonNull
    private final Executor mExecutor;

    public MainRepo(@NonNull Executor executor) {
        mExecutor = executor;
    }

    public LiveData<MapPartnerReader> getMapPartners(@NonNull LiveData<String> searchLiveData) {
        refreshData();
        return Transformations.switchMap(
                searchLiveData,
                search -> new DbLiveData<>(
                        Tables.TABLES,
                        mExecutor,
                        () -> MapPartnerReader.create(
                                DB
                                        .get()
                                        .mainDao()
                                        .getMapPartner(SearchUtil.formatSearch(search))
                        )
                )
        );
    }

    public LiveData<PartnerDetailReader> getPartnerDetail(@NonNull LiveData<Long> partnerIdLiveData) {
        return Transformations.switchMap(
                partnerIdLiveData,
                partnerId -> new DbLiveData<>(
                        Tables.TABLES,
                        mExecutor,
                        () -> PartnerDetailReader.create(
                                partnerId > Statement.NO_ID
                                        ? DB.get().mainDao().getPartnerDetail(partnerId)
                                        : null
                        )
                )
        );
    }

    public LiveData<FilterReader> getFilters(@NonNull LiveData<String> searchLiveData) {
        return Transformations.switchMap(
                searchLiveData,
                search -> new DbLiveData<>(
                        Tables.TABLES,
                        mExecutor,
                        () -> FilterReader.create(
                                DB
                                        .get()
                                        .mainDao()
                                        .getFilters(SearchUtil.formatSearch(search))
                        )
                )
        );
    }

    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        mExecutor.execute(
                () -> DB.get()
                        .partnerDao()
                        .updatePartnerVisibility(partnerId, isVisible)
        );
    }

    public void setCategoryVisibility(long categoryId, boolean isVisible) {
        mExecutor.execute(
                () -> DB.get()
                        .categoryDao()
                        .updateCategoryVisibility(categoryId, isVisible)
        );
    }

    public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
        mExecutor.execute(
                () -> DB.get()
                        .categoryDao()
                        .updateCategoryCollapsed(categoryId, isCollapsed)
        );
    }

    private void refreshData() {
        mExecutor.execute(new DataRefreshWorker());
    }

}
