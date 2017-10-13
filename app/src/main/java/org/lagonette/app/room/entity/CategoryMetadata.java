package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import org.lagonette.app.room.embedded.CategoryKey;

@Entity(
        tableName = "category_metadata",
        primaryKeys = {"category_id", "category_type"},
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = {"id", "type"},
                childColumns = {"category_id", "category_type"},
                onDelete = ForeignKey.CASCADE
        )
)
public class CategoryMetadata {

    @Embedded(prefix = "category_")
    public CategoryKey categoryKey;

    @ColumnInfo(name = "is_visible")
    public boolean isVisible;

    @ColumnInfo(name = "is_collapsed")
    public boolean isCollapsed;

}
