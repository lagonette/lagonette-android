package org.lagonette.app.background.client;

import android.support.annotation.NonNull;

import org.lagonette.app.api.response.CategoriesResponse;
import org.lagonette.app.api.response.PartnersResponse;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;
import org.lagonette.app.room.entity.Location;
import org.lagonette.app.room.entity.LocationMetadata;
import org.lagonette.app.room.entity.Partner;
import org.lagonette.app.room.entity.PartnerSideCategory;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.NullFunctions;

import java.util.ArrayList;
import java.util.List;

public class EntityConverter {

    @NonNull
    public Consumer<String> storePartnerSignature = NullFunctions::doNothing;

    @NonNull
    public Consumer<String> storeCategorySignature = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<Partner>> storePartners = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<Location>> storeLocations = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<LocationMetadata>> storeLocationMetadata = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<PartnerSideCategory>> storePartnerSideCategories = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<Category>> storeCategories = NullFunctions::doNothing;

    @NonNull
    public Consumer<List<CategoryMetadata>> storeCategoryMetadata = NullFunctions::doNothing;

    @NonNull
    public Runnable onPartnerConversionFinished = NullFunctions::doNothing;

    @NonNull
    public Runnable onCategoryConversionFinished = NullFunctions::doNothing;

    public void convertEntity(@NonNull PartnersResponse body) {
        List<Partner> partners = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationMetadata> locationMetadataList = new ArrayList<>();
        List<PartnerSideCategory> partnerSideCategories = new ArrayList<>();

        body.prepareInsert(partners, locations, locationMetadataList, partnerSideCategories);

        storePartnerSignature.accept(body.md5Sum);

        storePartners.accept(partners);
        storeLocations.accept(locations);
        storeLocationMetadata.accept(locationMetadataList);
        storePartnerSideCategories.accept(partnerSideCategories);

        onPartnerConversionFinished.run();
    }

    public void convertEntity(@NonNull CategoriesResponse body) {
        List<Category> categories = new ArrayList<>();
        List<CategoryMetadata> categoryMetadataList = new ArrayList<>();

        body.prepareInsert(categories, categoryMetadataList);

        storeCategorySignature.accept(body.md5Sum);

        storeCategories.accept(categories);
        storeCategoryMetadata.accept(categoryMetadataList);

        onCategoryConversionFinished.run();
    }


}
