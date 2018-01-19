package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import org.lagonette.app.room.embedded.CategoryKey;

import java.util.List;

public class Category {

    @Json(name = "id")
    public long id;

    @Json(name = "categoryType")
    public long categoryType;

    @Json(name = "parentId")
    public long parentId;

    @Json(name = "parentCategoryType")
    public long parentCategoryType;

    @Json(name = "label")
    public String label;

    @Json(name = "icon")
    public String icon;

    @Json(name = "displayOrder")
    public int displayOrder;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {
        org.lagonette.app.room.entity.Category category = new org.lagonette.app.room.entity.Category();
        category.key = new CategoryKey();
        category.key.id = id;
        category.key.type = categoryType;
        category.parentId = parentId;
        category.parentCategoryType = parentCategoryType;
        category.label = label;
        category.icon = icon;
        category.displayOrder = displayOrder;
        categories.add(category);

        org.lagonette.app.room.entity.CategoryMetadata categoryMetadata = new org.lagonette.app.room.entity.CategoryMetadata();
        categoryMetadata.categoryKey = category.key.clone();
        categoryMetadata.isVisible = true;
        categoryMetadata.isCollapsed = true;
        categoryMetadataList.add(categoryMetadata);
    }
}
