package org.lagonette.app.room.dao;

import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;
import android.arch.persistence.room.Transaction;

import org.lagonette.app.background.client.store.EntitiesStore;
import org.lagonette.app.locator.DB;
import org.lagonette.app.room.database.LaGonetteDatabase;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.custom.HiddenCategory;

@Dao
public abstract class WriterDao {

    @Transaction
    public void insert(@NonNull EntitiesStore.CategoryEntities categoryEntities, @NonNull EntitiesStore.PartnerEntities partnerEntities) {
        insert(categoryEntities);
        insert(partnerEntities);
        cleanUp();
    }

    private void insert(@NonNull EntitiesStore.PartnerEntities partnerEntities) {
        LaGonetteDatabase database = DB.get();
        //TODO Make a better clean
        database.partnerDao().deletePartners();
        database.partnerDao().deleteLocations();
        database.partnerDao().deletePartnerSideCategories();

        database.partnerDao().insertLocations(partnerEntities.locations);
        database.partnerDao().insertLocationsMetadatas(partnerEntities.locationMetadata); //TODO Make metadata insert only by SQL
        database.partnerDao().insertPartners(partnerEntities.partners);
        database.partnerDao().insertPartnersSideCategories(partnerEntities.partnerSideCategories);

        // Manage headquarter
        Partner headquarter = database.partnerDao().getHeadquarter();
        Category hidden = database.categoryDao().getHiddenCategory();
        if (headquarter != null && hidden != null) {

            hidden.icon = headquarter.logo;
            headquarter.mainCategoryKey = hidden.key.clone();

            database.categoryDao().insertCategory(hidden);
            database.partnerDao().insertPartner(headquarter);
        }
    }

    private void insert(@NonNull EntitiesStore.CategoryEntities categoryEntities) {
        LaGonetteDatabase database = DB.get();

        database.categoryDao().deleteCategories();
        database.categoryDao().insertCategories(categoryEntities.categories);
        database.categoryDao().insertCategoriesMetadatas(categoryEntities.categoryMetadata); //TODO Make metadata insert only by SQL

        // Manage hidden category
        HiddenCategory hiddenCategory = new HiddenCategory();
        CategoryMetadata categoryMetadata = hiddenCategory.getMetadata();
        database.categoryDao().insertCategory(hiddenCategory);
        database.categoryDao().insertCategoryMetadata(categoryMetadata);
    }

    private void cleanUp() {
        LaGonetteDatabase database = DB.get();
        // Clean up
        database.partnerDao().cleanLocation();
        database.partnerDao().cleanPartner();
//        db.partnerDao().cleanPartnerMetadata(); //TODO clean partner metadata
    }

}
