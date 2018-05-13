package org.lagonette.app.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.app.background.worker.DataRefreshWorker;
import org.lagonette.app.background.worker.WorkerState;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.entity.statement.LocationItem;
import org.lagonette.app.room.entity.statement.Shortcut;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.SearchUtils;

import java.util.List;
import java.util.concurrent.Executor;

public class MainRepo {

	@NonNull
	private final Executor mExecutor;

	@NonNull
	private final Context mContext;

	@NonNull
	private final LaGonetteDatabase mDatabase;

	public MainRepo(@NonNull Context context, @NonNull Executor executor) {
		mContext = context;
		mExecutor = executor;
		mDatabase = DB.get();
	}

	public LiveData<WorkerState> updateDatas() {
		DataRefreshWorker worker = new DataRefreshWorker(mContext);
		mExecutor.execute(worker);
		return worker.getWorkerState();
	}

	public LiveData<LocationItem> getMapLocation(@NonNull LiveData<Long> locationIdLiveData) {
		return Transformations.switchMap(
				locationIdLiveData,
				locationId -> mDatabase.uiDao().getMapLocation(locationId)
		);
	}

	public LiveData<List<LocationItem>> getMapLocations(@NonNull LiveData<String> searchLiveData) {
		return Transformations.switchMap(
				searchLiveData,
				search -> mDatabase
						.uiDao()
						.getMapLocations(SearchUtils.formatSearch(search))
		);
	}

	public LiveData<LocationDetail> getLocationDetail(@NonNull LiveData<Long> locationIdLiveData) {
		return Transformations.switchMap(
				locationIdLiveData,
				locationId -> mDatabase.uiDao().getLocationsDetail(locationId)
		);
	}

	public LiveData<PagedList<Filter>> getFilters(@NonNull LiveData<String> searchLiveData) {
		return Transformations.switchMap(
				searchLiveData,
				search -> new LivePagedListBuilder<>(
						mDatabase.uiDao().getFilters(SearchUtils.formatSearch(search)),
						20
				)
						.build()
		);
	}

	public LiveData<Shortcut> getShortcut() {
		return mDatabase.uiDao().getShortcut();
	}

	public void setLocationVisibility(long locationId, boolean isVisible) {
		mExecutor.execute(
				() -> mDatabase
						.partnerDao()
						.updateLocationVisibility(locationId, isVisible)
		);
	}

	public void setCategoryVisibility(long categoryId, boolean isVisible) {
		mExecutor.execute(
				() -> mDatabase
						.categoryDao()
						.updateCategoryVisibility(categoryId, isVisible)
		);
	}

	public void makeVisibleOneCategory(long categoryId) {
		mExecutor.execute(
				() -> {
					try {
						mDatabase.beginTransaction();
						mDatabase
								.categoryDao()
								.makeVisibleOneCategory(categoryId);
						mDatabase
								.partnerDao()
								.updateOrphanPartnerVisibilities(false);
						mDatabase.setTransactionSuccessful();
					}
					finally {
						mDatabase.endTransaction();
					}
				}
		);
	}

	public void setCategoryCollapsed(long categoryId, boolean isCollapsed) {
		mExecutor.execute(
				() -> mDatabase
						.categoryDao()
						.updateCategoryCollapsed(categoryId, isCollapsed)
		);
	}

	public void setAllCategoriesCollapsed(boolean collapsed) {
		mExecutor.execute(
				() -> mDatabase
						.categoryDao()
						.updateAllCategoriesCollapsed(collapsed)
		);
	}

	public void setAllCategoriesVisibility(boolean visibility) {
		mExecutor.execute(
				() -> mDatabase
						.categoryDao()
						.updateCategoryVisibilities(visibility)
		);
	}

	public void showAllLocations() {
		mExecutor.execute(
				() -> {
					try {
						mDatabase.beginTransaction();
						mDatabase.categoryDao()
								.updateCategoryVisibilities(true);
						mDatabase.partnerDao()
								.updateLocationVisibilities(true);
						mDatabase.setTransactionSuccessful();
					}
					finally {
						mDatabase.endTransaction();
					}
				}
		);
	}

	public void showAllExchangeOffice() {
		mExecutor.execute(
				() -> {
					try {
						mDatabase.beginTransaction();
						mDatabase.categoryDao()
								.updateCategoryVisibilities(true);
						mDatabase.partnerDao()
								.updateLocationVisibilities(false);
						mDatabase.partnerDao()
								.updateExchangeOfficeVisibilities(true);
						mDatabase.setTransactionSuccessful();
					}
					finally {
						mDatabase.endTransaction();
					}
				}
		);
	}

	public void resetMetadata() {
		mExecutor.execute(
				() -> mDatabase
						.writerDao()
						.resetMetadata()
		);
	}
}
