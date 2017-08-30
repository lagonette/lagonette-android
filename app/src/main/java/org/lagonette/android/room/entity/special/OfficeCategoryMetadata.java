package org.lagonette.android.room.entity.special;

import org.lagonette.android.room.entity.CategoryMetadata;

public class OfficeCategoryMetadata extends CategoryMetadata {

    public OfficeCategoryMetadata() {
        categoryId = OfficeCategory.ID;
        isVisible = true;
        isCollapsed = false;
    }
}
