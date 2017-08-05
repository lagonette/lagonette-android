package org.lagonette.android.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse extends ApiResponse {

    @SerializedName("categories")
    private List<Category> mCategories;

    public void prepareInsert(
            @NonNull List<org.lagonette.android.room.entity.Category> categories,
            @NonNull List<org.lagonette.android.room.entity.CategoryMetadata> categoryMetadataList) {
        for (Category category : mCategories) {
            category.prepareInsert(categories, categoryMetadataList);
        }
    }
}
