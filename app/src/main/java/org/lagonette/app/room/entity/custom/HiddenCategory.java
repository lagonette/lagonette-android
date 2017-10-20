package org.lagonette.app.room.entity.custom;

import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.entity.Category;
import org.lagonette.app.room.entity.CategoryMetadata;

public class HiddenCategory
        extends Category {

    private CategoryMetadata mMetadata;

    public HiddenCategory() {
        key = new CategoryKey();
        key.id = 0; //TODO
        key.type = 9999; //TODO
        parentId = -1;
        parentCategoryType = -1;
        label = "Hidden";
        icon = null;
        displayOrder = -1; //TODO


        mMetadata = new CategoryMetadata();
        mMetadata.categoryKey = key.clone();
        mMetadata.isCollapsed = false;
        mMetadata.isVisible = true;
    }

    public CategoryMetadata getMetadata() {
        return mMetadata;
    }
}
