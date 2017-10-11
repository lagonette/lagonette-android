package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(
        tableName = "category_metadata",
        primaryKeys = {"category_id", "category_type"},
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = {"id", "category_type"},
                childColumns = {"category_id", "category_type"},
                onDelete = ForeignKey.CASCADE
        )
)
public class CategoryMetadata {

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @ColumnInfo(name = "category_type")
    public long categoryType;

    @ColumnInfo(name = "is_visible")
    public boolean isVisible;

    @ColumnInfo(name = "is_collapsed")
    public boolean isCollapsed;

}
