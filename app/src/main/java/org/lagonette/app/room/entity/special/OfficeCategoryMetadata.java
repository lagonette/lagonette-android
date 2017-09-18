package org.lagonette.app.room.entity.special;

import org.lagonette.app.room.entity.CategoryMetadata;

public class OfficeCategoryMetadata extends CategoryMetadata {

    public OfficeCategoryMetadata() {
        categoryId = OfficeCategory.ID;
        isVisible = true;
        isCollapsed = false;
    }
}
