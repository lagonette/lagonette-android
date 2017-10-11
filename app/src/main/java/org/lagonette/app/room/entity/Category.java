package org.lagonette.app.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(
        tableName = "category",
        primaryKeys = {
                "id",
                "category_type"
        }
)
public class Category {

    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "category_type")
    public long categoryType;

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
