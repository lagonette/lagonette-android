package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

import org.lagonette.app.room.embedded.CategoryKey;

@Entity(
        tableName = "category",
        primaryKeys = {
                "id",
                "type"
        }
)
public class Category {

    @Embedded
    public CategoryKey key;

    @ColumnInfo(name = "parent_id")
    public long parentId;

    @ColumnInfo(name = "parent_category_type")
    public long parentCategoryType;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "icon")
    public String icon;

    @ColumnInfo(name = "display_order")
    public int displayOrder;

}
