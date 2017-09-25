package org.lagonette.app.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.app.app.arch.CursorLiveData;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.statement.PartnerDetail;
import org.lagonette.app.room.entity.statement.PartnerItem;
import org.lagonette.app.room.reader.FilterReader;
import org.lagonette.app.room.sql.Tables;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.SearchUtil;
import org.lagonette.app.worker.DataRefreshWorker;

import java.util.List;
import java.util.concurrent.Executor;

public class MainRepo {

    @NonNull
    private Context mContext;

    @NonNull
    private final Executor mExecutor;

    private boolean mShouldFetch;

    public MainRepo(@NonNull Context context, @NonNull Executor executor) {
        mContext = context;
        mExecutor = executor;
        mShouldFetch = true;
    }

    public LiveData<Resource<List<PartnerItem>>> getMapPartners(@NonNull LiveData<String> searchLiveEvent) {
        return new LambdaResourceAlgorithm<>(
                mExecutor,
                this::shouldFetch,
                () -> Transformations.switchMap(
                        searchLiveEvent,
                        search -> DB
                                .get()
                                .mainDao()
                                .getMapPartner(SearchUtil.formatSearch(search))
                ),
                () -> new DataRefreshWorker(mContext)
        )
                .start()
                .getAsLiveData();
    } // TODO Fix "Application did not close the cursor or database object that was opened here" issue

    public LiveData<Resource<PartnerDetail>> getPartnerDetail(@NonNull LiveData<Long> partnerIdLiveData) {
        return new LambdaResourceAlgorithm<>(
                mExecutor,
                this::shouldFetch,
                () -> Transformations.switchMap(
                        partnerIdLiveData,
                        partnerId -> partnerId > Statement.NO_ID
                                ? DB.get().mainDao().getPartnerDetail(partnerId)
                                : null
                ),
                () -> new DataRefreshWorker(mContext)
        )
                .start()
                .getAsLiveData();
    }

    public LiveData<Resource<FilterReader>> getFilters(@NonNull LiveData<String> searchLiveData) {
        return new LambdaResourceAlgorithm<>(
                mExecutor,
                this::shouldFetch,
                () -> Transformations.map(
                        Transformations.switchMap(
                                searchLiveData,
                                search -> new CursorLiveData(
                                        Tables.TABLES,
                                        mExecutor,
                                        () -> DB
                                                .get()
                                                .mainDao()
                                                .getFilters(SearchUtil.formatSearch(search))
                                )
                        ),
                        FilterReader::create
                ),
                () -> new DataRefreshWorker(mContext)
        )
                .start()
                .getAsLiveData();
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

    public void showAllPartners() {
        mExecutor.execute(
                () -> {
                    LaGonetteDatabase database = DB.get();
                    database.beginTransaction();
                    database.categoryDao()
                            .updateCategoryVisibilities(true);
                    database.partnerDao()
                            .updatePartnerVisibilities(true);
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
                            .updatePartnerVisibilities(false);
                    database.partnerDao()
                            .updateExchangeOfficeVisibilities(true);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }
        );
    }

    private boolean shouldFetch() {
        if (mShouldFetch) {
            mShouldFetch = false;
            return true;
        } else {
            return false;
        }
    }
}
