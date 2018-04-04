package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(
        tableName = "category_metadata",
        primaryKeys = {"category_id"}
)
public class CategoryMetadata {

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @ColumnInfo(name = "is_visible")
    public boolean isVisible;

    @ColumnInfo(name = "is_collapsed")
    public boolean isCollapsed;

}
