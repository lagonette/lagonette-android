package org.lagonette.app.background.client.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.tools.functions.BiObjConsumer;
import org.lagonette.app.tools.functions.NullFunctions;

import java.util.List;

public class EntitiesStore {

    public static class PartnerEntities {

        @Nullable
        public List<Partner> partners;

        @Nullable
        public List<Location> locations;

        @Nullable
        public List<LocationMetadata> locationMetadata;

        @Nullable
        public List<PartnerSideCategory> partnerSideCategories;

    }

    public static class CategoryEntities {

        @Nullable
        public List<Category> categories;

        @Nullable
        public List<CategoryMetadata> categoryMetadata;

    }

    @NonNull
    public Runnable entitiesSaved = NullFunctions::doNothing;

    @NonNull
    public BiObjConsumer<CategoryEntities, PartnerEntities> saveEntities = NullFunctions::doNothing;

    private final PartnerEntities mPartnerEntities = new PartnerEntities();

    private final CategoryEntities mCategoryEntities = new CategoryEntities();

    public void storePartners(@Nullable List<Partner> partners) {
        mPartnerEntities.partners = partners;
    }

    public void storeLocations(@Nullable List<Location> locations) {
        mPartnerEntities.locations = locations;
    }

    public void storeLocationMetadata(@Nullable List<LocationMetadata> locationMetadata) {
        mPartnerEntities.locationMetadata = locationMetadata;
    }

    public void storePartnerSideCategories(@Nullable List<PartnerSideCategory> partnerSideCategories) {
        mPartnerEntities.partnerSideCategories = partnerSideCategories;
    }

    public void storeCategories(@Nullable List<Category> categories) {
        mCategoryEntities.categories = categories;
    }

    public void storeCategoryMetadata(@Nullable List<CategoryMetadata> categoryMetadata) {
        mCategoryEntities.categoryMetadata = categoryMetadata;
    }

    public void saveEntities() {
        saveEntities.accept(mCategoryEntities, mPartnerEntities);
        entitiesSaved.run();
    }
}
