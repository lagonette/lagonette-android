package org.lagonette.app.api.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    private long mId;

    @SerializedName("label")
    private String mLabel;

    @SerializedName("icon")
    private String mIcon;

    public void prepareInsert(
            @NonNull List<org.lagonette.app.room.entity.Category> categories,
            @NonNull List<org.lagonette.app.room.entity.CategoryMetadata> categoryMetadataList) {
        org.lagonette.app.room.entity.Category category = new org.lagonette.app.room.entity.Category();
        category.id = mId;
        category.label = mLabel;
        category.icon = mIcon;
        categories.add(category);

        org.lagonette.app.room.entity.CategoryMetadata categoryMetadata = new org.lagonette.app.room.entity.CategoryMetadata();
        categoryMetadata.categoryId = category.id;
        categoryMetadata.isVisible = true;
        categoryMetadata.isCollapsed = true;
        categoryMetadataList.add(categoryMetadata);
    }
}
