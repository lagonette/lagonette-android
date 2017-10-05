package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.lagonette.app.room.entity.CategoryMetadata;

import java.util.List;

public class CategoriesResponse extends ApiResponse {

    @SerializedName("categories")
    private List<Category> mCategories;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {
        addOfficeCategory(categories, categoryMetadataList);
        for (Category category : mCategories) {
            category.prepareInsert(categories, categoryMetadataList);
        }
    }

    // TODO custom entity should be added from TypeAdapter
    private void addOfficeCategory(@NonNull List<org.lagonette.app.room.entity.Category> categories, @NonNull List<CategoryMetadata> categoryMetadataList) {
//        categories.add(new OfficeCategory());
//        categoryMetadataList.add(new OfficeCategoryMetadata());
    }
}
