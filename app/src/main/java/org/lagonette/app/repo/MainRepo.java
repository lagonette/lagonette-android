package org.lagonette.app.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.app.background.worker.WorkerState;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.tools.arch.CursorLiveData;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.reader.FilterReader;
import org.lagonette.app.room.sql.Tables;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.SearchUtils;
import org.lagonette.app.background.worker.DataRefreshWorker;

import java.util.List;
import java.util.concurrent.Executor;

public class MainRepo {

    @NonNull
    private Context mContext;

    @NonNull
    private final Executor mExecutor;

    public MainRepo(@NonNull Context context, @NonNull Executor executor) {
        mContext = context;
        mExecutor = executor;
    }

    public LiveData<WorkerState> updateDatas() {
        DataRefreshWorker worker = new DataRefreshWorker(mContext);
        mExecutor.execute(worker);
        return worker.getWorkerState();
    }

    public LiveData<List<LocationItem>> getMapPartners(@NonNull LiveData<String> searchLiveData) {
        return Transformations.switchMap(
                searchLiveData,
                search -> DB
                        .get()
                        .uiDao()
                        .getMapLocations(SearchUtils.formatSearch(search))
        );
    } //TODO Fix "Application did not close the cursor or database object that was opened here" issue

    public LiveData<LocationDetail> getLocationDetail(@NonNull LiveData<Long> locationIdLiveData) {
        return Transformations.switchMap(
                locationIdLiveData,
                locationId -> locationId > Statement.NO_ID
                        ? DB.get().uiDao().getLocationsDetail(locationId)
                        : null
        );
    }

    public LiveData<FilterReader> getFilters(@NonNull LiveData<String> searchLiveData) {
        return Transformations.map(
                Transformations.switchMap(
                        searchLiveData,
                        search -> new CursorLiveData(
                                Tables.TABLES,
                                mExecutor,
                                () -> DB
                                        .get()
                                        .uiDao()
                                        .getFilters(SearchUtils.formatSearch(search))
                        )
                ),
                FilterReader::create
        );
    }

    public LiveData<HeadquarterShortcut> getHeadquarterShortcut() {
        return DB.get().uiDao().getHeadquarterShortcut();
    }

    public void setLocationVisibility(long locationId, boolean isVisible) {
        mExecutor.execute(
                () -> DB.get()
                        .partnerDao()
                        .updateLocationVisibility(locationId, isVisible)
        );
    }

    public void setCategoryVisibility(@NonNull CategoryKey key, boolean isVisible) {
        mExecutor.execute(
                () -> DB.get()
                        .categoryDao()
                        .updateCategoryVisibility(key.id, key.type, isVisible)
        );
    }

    public void setCategoryCollapsed(@NonNull CategoryKey key, boolean isCollapsed) {
        mExecutor.execute(
                () -> DB.get()
                        .categoryDao()
                        .updateCategoryCollapsed(key.id, key.type, isCollapsed)
        );
    }

    public void showAllLocations() {
        mExecutor.execute(
                () -> {
                    LaGonetteDatabase database = DB.get();
                    database.beginTransaction();
                    database.categoryDao()
                            .updateCategoryVisibilities(true);
                    database.partnerDao()
                            .updateLocationVisibilities(true);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }
        );
    }

    public void showAllExchangeOffice() {
        mExecutor.execute(
                () -> {
                    LaGonetteDatabase database = DB.get();
                    database.beginTransaction();
                    database.categoryDao()
                            .updateCategoryVisibilities(true);
                    database.partnerDao()
                            .updateLocationVisibilities(false);
                    database.partnerDao()
                            .updateExchangeOfficeVisibilities(true);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }
        );
    }

}
