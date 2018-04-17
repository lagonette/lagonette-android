package org.lagonette.app.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerSideCategory;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class WriterDao {

	public static class PartnerEntities {

		public boolean hasValues;

		@NonNull
		public List<Partner> partners = new ArrayList<>();

		@NonNull
		public List<Location> locations = new ArrayList<>();

		@NonNull
		public List<LocationMetadata> locationMetadata = new ArrayList<>();

		@NonNull
		public List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

	}

	public static class CategoryEntities {

		public boolean hasValues;

		@NonNull
		public List<Category> categories = new ArrayList<>();

		@NonNull
		public List<CategoryMetadata> categoryMetadata = new ArrayList<>();

	}

	@SuppressWarnings("NullableProblems")
	@Transaction
	public void insert(
			@NonNull CategoryEntities categoryEntities,
			@NonNull PartnerEntities partnerEntities) {
		LaGonetteDatabase database = DB.get();
		insert(database, categoryEntities);
		insert(database, partnerEntities);
		cleanUp(database);
	}

	private void insert(
			@NonNull LaGonetteDatabase database,
			@NonNull PartnerEntities partnerEntities) {
		if (partnerEntities.hasValues) {
			database.partnerDao().deletePartners();
			database.partnerDao().deleteLocations();
			database.partnerDao().deletePartnerSideCategories();

			database.partnerDao().insertLocations(partnerEntities.locations);
			// When INSERT query will be available with Room, insert directly default metadata rather than create objects.
			database.partnerDao().insertLocationsMetadatas(partnerEntities.locationMetadata);
			database.partnerDao().insertPartners(partnerEntities.partners);
			database.partnerDao().insertPartnersSideCategories(partnerEntities.partnerSideCategories);
		}
	}

	private void insert(
			@NonNull LaGonetteDatabase database,
			@NonNull CategoryEntities categoryEntities) {
		if (categoryEntities.hasValues) {
			database.categoryDao().deleteCategories();

			database.categoryDao().insertCategories(categoryEntities.categories);
			// When INSERT query will be available with Room, insert directly default metadata rather than create objects.
			database.categoryDao().insertCategoriesMetadatas(categoryEntities.categoryMetadata);
		}
	}

	private void cleanUp(@NonNull LaGonetteDatabase database) {
		database.partnerDao().cleanLocationWithoutPosition();
		database.partnerDao().cleanOrphanLocationMetadata();
		database.partnerDao().cleanPartnerWithoutLocation();
		database.partnerDao().cleanOrphanPartnerSideCategory();

        database.categoryDao().cleanOrphanCategoryMetadata();
	}

}
