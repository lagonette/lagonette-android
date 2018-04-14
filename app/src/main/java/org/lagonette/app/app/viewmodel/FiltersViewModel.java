package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.Repo;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;

public class FiltersViewModel
		extends ViewModel {

	@NonNull
	private final MutableLiveData<String> mSearch;

	@NonNull
	private final LiveData<PagedList<Filter>> mFilters;

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
	public LiveData<PagedList<Filter>> getFilters() {
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

	public void changeCategoryVisibility(long categoryId, boolean isVisible) {
		Repo
				.get()
				.setCategoryVisibility(categoryId, isVisible);
	}

	public void makeVisibleOneCategory(long categoryId) {
		Repo
				.get()
				.makeVisibleOneCategory(categoryId);
	}

	public void changeCategoryCollapsed(long categoryId, boolean isCollapsed) {
		Repo
				.get()
				.setCategoryCollapsed(categoryId, isCollapsed);
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
