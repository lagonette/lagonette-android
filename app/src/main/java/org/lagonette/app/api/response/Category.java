package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    public long id;

    @SerializedName("categoryType")
    public long categoryType;

    @SerializedName("parentId")
    public long parentId;

    @SerializedName("label")
    public String label;

    @SerializedName("icon")
    public String icon;

    @SerializedName("displayOrder")
    public int displayOrder;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {
        org.lagonette.app.room.entity.Category category = new org.lagonette.app.room.entity.Category();
        category.id = id;
        category.categoryType = categoryType;
        category.parentId = parentId;
        category.parentCategoryType = 1; // TODO This value has to come from JSON !
        category.label = label;
        category.icon = icon;
        category.displayOrder = displayOrder;
        categories.add(category);

        org.lagonette.app.room.entity.CategoryMetadata categoryMetadata = new org.lagonette.app.room.entity.CategoryMetadata();
        categoryMetadata.categoryId = category.id;
        categoryMetadata.categoryType = category.categoryType;
        categoryMetadata.isVisible = true;
        categoryMetadata.isCollapsed = true;
        categoryMetadataList.add(categoryMetadata);
    }
}
