package org.lagonette.app.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.embedded.CategoryKey;
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

    @Transaction
    public void insert(@NonNull CategoryEntities categoryEntities, @NonNull PartnerEntities partnerEntities) {
        LaGonetteDatabase database = DB.get();
        insert(database, categoryEntities);
        insert(database, partnerEntities);
        insertHeadquarter(database);
        cleanUp(database);
    }

    private void insert(@NonNull LaGonetteDatabase database, @NonNull PartnerEntities partnerEntities) {
        if (partnerEntities.hasValues) {
            //TODO Make a better clean
            database.partnerDao().deletePartners();
            database.partnerDao().deleteLocations();
            database.partnerDao().deletePartnerSideCategories();

            database.partnerDao().insertLocations(partnerEntities.locations);
            database.partnerDao().insertLocationsMetadatas(partnerEntities.locationMetadata); //TODO Make metadata insert only by SQL
            database.partnerDao().insertPartners(partnerEntities.partners);
            database.partnerDao().insertPartnersSideCategories(partnerEntities.partnerSideCategories);
        }
    }

    private void insert(@NonNull LaGonetteDatabase database, @NonNull CategoryEntities categoryEntities) {
        if (categoryEntities.hasValues) {
            database.categoryDao().deleteCategories();
            database.categoryDao().insertCategories(categoryEntities.categories);
            database.categoryDao().insertCategoriesMetadatas(categoryEntities.categoryMetadata); //TODO Make metadata insert only by SQL
        }
    }

    private void insertHeadquarter(@NonNull LaGonetteDatabase database) {
        Partner headquarter = database.partnerDao().getHeadquarter();

        if (headquarter != null) {

            // Manage headquarter category
            Category headquarterCategory = new Category();
            headquarterCategory.key = new CategoryKey();
            headquarterCategory.key.id = 0; //TODO
            headquarterCategory.key.type = 9999; //TODO
            headquarterCategory.parentId = -1;
            headquarterCategory.parentCategoryType = -1;
            headquarterCategory.label = "Headquarter Category";
            headquarterCategory.icon = headquarter.logo;
            headquarterCategory.displayOrder = -1; //TODO
            headquarterCategory.hidden = true;

            // Manage headquarter category metadata
            CategoryMetadata headquarterCategoryMetadata = new CategoryMetadata();
            headquarterCategoryMetadata.categoryKey = headquarterCategory.key.clone();
            headquarterCategoryMetadata.isCollapsed = false;
            headquarterCategoryMetadata.isVisible = true;

            // Manage headquarter partner
            headquarter.mainCategoryKey = headquarterCategory.key.clone();

            // Insert
            database.categoryDao().insertCategory(headquarterCategory);
            database.categoryDao().insertCategoryMetadata(headquarterCategoryMetadata);
            database.partnerDao().insertPartner(headquarter);
        }
    }

    private void cleanUp(LaGonetteDatabase database) {
        database.partnerDao().cleanLocation();
        database.partnerDao().cleanPartner();
//        db.partnerDao().cleanPartnerMetadata(); //TODO clean partner metadata
//        database.categoryDao().cleanCategories(); //TODO clean categories and metadata
    }

}
