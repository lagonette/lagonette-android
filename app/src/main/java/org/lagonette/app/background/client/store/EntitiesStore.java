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

import java.util.ArrayList;
import java.util.List;

public class EntitiesStore {

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
        public List<CategoryMetadata> categoryMetadata= new ArrayList<>();

    }

    @NonNull
    public Runnable entitiesSaved = NullFunctions::doNothing;

    @NonNull
    public BiObjConsumer<CategoryEntities, PartnerEntities> saveEntities = NullFunctions::doNothing;

    private final PartnerEntities mPartnerEntities = new PartnerEntities();

    private final CategoryEntities mCategoryEntities = new CategoryEntities();

    public void storePartners(@Nullable List<Partner> partners) {
        mPartnerEntities.hasValues = true;
        mPartnerEntities.partners = partners;
    }

    public void storeLocations(@Nullable List<Location> locations) {
        mCategoryEntities.hasValues = true;
        mPartnerEntities.locations = locations;
    }

    public void storeLocationMetadata(@Nullable List<LocationMetadata> locationMetadata) {
        mCategoryEntities.hasValues = true;
        mPartnerEntities.locationMetadata = locationMetadata;
    }

    public void storePartnerSideCategories(@Nullable List<PartnerSideCategory> partnerSideCategories) {
        mCategoryEntities.hasValues = true;
        mPartnerEntities.partnerSideCategories = partnerSideCategories;
    }

    public void storeCategories(@Nullable List<Category> categories) {
        mCategoryEntities.hasValues = true;
        mCategoryEntities.categories = categories;
    }

    public void storeCategoryMetadata(@Nullable List<CategoryMetadata> categoryMetadata) {
        mCategoryEntities.hasValues = true;
        mCategoryEntities.categoryMetadata = categoryMetadata;
    }

    public void saveEntities() {
        saveEntities.accept(mCategoryEntities, mPartnerEntities);
        entitiesSaved.run();
    }
}
