package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.lagonette.app.room.embedded.CategoryKey;

@Entity(
        tableName = "category_metadata",
        primaryKeys = {"category_id", "category_type"}
        //TODO Do not use foreign, if not metadata is deleted on category insert with Replace strategy
)
public class CategoryMetadata {

    @NonNull
    @Embedded(prefix = "category_")
    public CategoryKey categoryKey;

    @ColumnInfo(name = "is_visible")
    public boolean isVisible;

    @ColumnInfo(name = "is_collapsed")
    public boolean isCollapsed;

}
