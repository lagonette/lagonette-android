package org.lagonette.android.room.entity.special;

import org.lagonette.android.room.entity.Category;


public class OfficeCategory extends Category {

    public static final long ID = Long.MAX_VALUE;

    public OfficeCategory() {
        id = ID;
        label = "";
        icon = ""; // TODO find icon
    }
}
