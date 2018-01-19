package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import java.util.List;

public class CategoriesResponse extends ApiResponse {

    @Json(name = "categories")
    private List<Category> mCategories;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {
        for (Category category : mCategories) {
            category.prepareInsert(categories, categoryMetadataList);
        }
    }
}
